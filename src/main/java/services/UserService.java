/*
 * ManagerService.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package services;

import java.util.Collection;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.UserRepository;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Actor;
import domain.Article;
import domain.Chirp;
import domain.Newspaper;
import domain.User;
import forms.ActorRegisterForm;

@Service
@Transactional
public class UserService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private UserRepository		userRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private UserAccountService	userAccountService;
	@Autowired
	private ActorService		actorService;
	@Autowired
	private ArticleService		articleService;
	@Autowired
	private FolderService		folderService;


	// Constructors -----------------------------------------------------------

	public UserService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public User create() {
		UserAccount useraccount;
		final User result = new User();

		useraccount = this.userAccountService.createAsUser();

		result.setUserAccount(useraccount);
		final Collection<Newspaper> newspapers = new LinkedList<>();
		final Collection<Article> articles = new LinkedList<>();
		final Collection<Chirp> chirps = new LinkedList<>();

		result.setNewspapers(newspapers);
		result.setArticles(articles);
		result.setChirps(chirps);

		return result;
	}

	public Collection<User> findAll() {
		Collection<User> result;

		result = this.userRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public User findOne(final int userId) {
		User result;

		result = this.userRepository.findOne(userId);
		Assert.notNull(result);

		return result;
	}

	public User save(final User user) {
		Assert.notNull(user);
		User result;

		Boolean create = false;

		if (user.getId() == 0) {
			create = true;
			Md5PasswordEncoder encoder;

			encoder = new Md5PasswordEncoder();

			user.getUserAccount().setPassword(encoder.encodePassword(user.getUserAccount().getPassword(), null));
		}

		result = this.userRepository.save(user);
		if (create)
			this.folderService.createSystemFolders(result);

		return result;
	}

	public void delete(final User user) {
		Assert.notNull(user);
		Assert.isTrue(user.getId() != 0);

		this.userRepository.delete(user);
	}

	public void flush() {
		this.userRepository.flush();
	}

	// Other business methods -------------------------------------------------

	public User findByPrincipal() {
		User result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findByUserAccount(userAccount);
		Assert.isTrue(actor instanceof User);
		result = (User) actor;
		Assert.notNull(result);

		return result;
	}

	public User reconstruct(final ActorRegisterForm userForm) {
		User res;
		if (userForm.getId() == 0)
			res = this.create();
		else
			res = this.userRepository.findOne(userForm.getId());

		res.setName(userForm.getName());
		res.setPhone(userForm.getPhone());
		res.setSurname(userForm.getSurname());
		res.setEmail(userForm.getEmail());
		res.setAddress(userForm.getAddress());
		res.getUserAccount().setUsername(userForm.getUsername());
		res.getUserAccount().setPassword(userForm.getPassword());

		return res;
	}

	public void follow(final int userId) {
		final User userPrincipal = this.findByPrincipal();
		Assert.notNull(userPrincipal);
		final User userFollow = this.findOne(userId);
		Assert.notNull(userFollow, "User does not exist");
		Assert.isTrue(!userPrincipal.getFolloweds().contains(userFollow), "You are following this user");

		userPrincipal.getFolloweds().add(userFollow);
		this.save(userPrincipal);
	}

	public void unfollow(final int userId) {
		final User userPrincipal = this.findByPrincipal();
		Assert.notNull(userPrincipal);
		final User userFollow = this.findOne(userId);
		Assert.notNull(userFollow, "User does not exist");
		Assert.isTrue(userPrincipal.getFolloweds().contains(userFollow), "Do not follow this user");

		userPrincipal.getFolloweds().remove(userFollow);
		this.save(userPrincipal);
	}

	public Collection<User> findFollowersByUser(final User user) {
		Assert.notNull(user);
		final Collection<User> followers = this.userRepository.findFollowersByUser(user);
		Assert.notNull(followers);

		return followers;
	}

	public User findOneByArticle(final int articleId) {
		Assert.notNull(articleId);
		final User user = this.userRepository.findOneByArticle(articleId);
		Assert.notNull(user);
		return user;
	}

	public User findByChirp(final Integer chirpId) {
		Assert.notNull(chirpId);
		final User user = this.userRepository.findByChirp(chirpId);
		return user;
	}

	public User findOneByNewspaper(final Integer id) {
		return this.userRepository.findOneByNewspaper(id);

	}

	public Collection<Object> sumAvgAndCountfNewspPerUser() {
		return this.userRepository.sumAvgAndCountfNewspPerUser();
	}

	public Collection<Object> sumAvgAndCountfArticlesPerUser() {
		return this.userRepository.sumAvgAndCountfArticlesPerUser();
	}

	// The ratio of users who have posted above 75% the average number of chirps
	// per user
	public Double ratioOfUsersAbove75ChirpsPerUser() {
		return this.userRepository.ratioOfUsersAbove75ChirpsPerUser();
	}

	public Collection<Object> countsForRatioOfNewspaperWriters() {
		return this.userRepository.countsForRatioOfNewspaperWriters();
	}

	public Collection<Object> countsForRatioOfArticleWriters() {
		return this.userRepository.countsForRatioOfArticleWriters();
	}
}
