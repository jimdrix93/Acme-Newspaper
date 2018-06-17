
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.NewspaperRepository;
import domain.Actor;
import domain.Administrator;
import domain.Advertisement;
import domain.Agent;
import domain.Article;
import domain.Customer;
import domain.Newspaper;
import domain.Subscription;
import domain.User;
import forms.NewspaperEditForm;

@Service
@Transactional
public class NewspaperService {

	// Managed repository ----------------------------------------------------
	@Autowired
	private NewspaperRepository		newspaperRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private UserService				userService;

	@Autowired
	private SubscriptionService		suscriptionService;

	@Autowired
	private ArticleService			articleService;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private AdministratorService	administratorService;
	@Autowired
	private AgentService			agentService;
	@Autowired
	private AdvertisementService	advertisementService;
	@Autowired
	private VolumeService			volumeService;
	@Autowired
	private CustomerService			customerService;
	@Autowired
	private SubscriptionService		subscriptionService;


	// Constructors -----------------------------------------------------------

	public NewspaperService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Newspaper create() {
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);
		final Newspaper res = new Newspaper();
		final Collection<Article> articles = new LinkedList<Article>();
		res.setArticles(articles);

		return res;
	}
	public Newspaper save(final Newspaper newspaper) {
		Newspaper savedNewspaper;
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof User);

		if (newspaper.getId() != 0) {
			//			this.checkPrincipal(newspaper);
			savedNewspaper = this.newspaperRepository.findOne(newspaper.getId());
			if (savedNewspaper.getIsPublicated()) {
				newspaper.setIsPublicated(savedNewspaper.getIsPublicated());
				newspaper.setPublicationDate(savedNewspaper.getPublicationDate());
			}
		}

		if (newspaper.getIsPublicated() == true) {

			if (newspaper.getPublicationDate() == null)
				newspaper.setPublicationDate(new Date());
			Assert.notNull(newspaper.getPublicationDate(), "Must have a publication date");

			final Collection<Article> articles = this.articleService.findAllByNewspaperId(newspaper.getId());
			Assert.notEmpty(articles, "Can not publicate a newspaper whitout articles");

			for (final Article article : articles)
				Assert.isTrue(article.isFinalMode(), "All articles is not final mode");
		}

		savedNewspaper = this.newspaperRepository.saveAndFlush(newspaper);

		if (newspaper.getIsPublicated() == true) {
			final Collection<Article> articles = this.articleService.findAllByNewspaperId(newspaper.getId());

			for (final Article article : articles)
				this.articleService.changePublicationDate(article, savedNewspaper.getPublicationDate());
		}

		if (newspaper.getId() == 0) {
			final User user = (User) actor;
			user.getNewspapers().add(savedNewspaper);
			this.userService.save(user);
		}

		return savedNewspaper;
	}

	public Newspaper update(final Newspaper newspaper) {
		final Newspaper savedNewspaper;
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof Administrator);
		savedNewspaper = this.newspaperRepository.save(newspaper);

		return savedNewspaper;
	}

	public Newspaper findOne(final int newspaperId) {
		Assert.notNull(newspaperId);
		final Newspaper newspaper = this.newspaperRepository.findOne(newspaperId);
		Assert.notNull(newspaper);
		return newspaper;
	}

	public Newspaper findOneToEdit(final int newspaperId) {
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);

		Assert.notNull(newspaperId);
		final Newspaper newspaper = this.newspaperRepository.findOne(newspaperId);
		Assert.isTrue(user.getNewspapers().contains(newspaper));
		Assert.notNull(newspaper);
		return newspaper;
	}

	// Other business methods -------------------------------------------------
	public void checkPrincipal(final Newspaper newspaper) {
		final Actor principal = this.actorService.findByPrincipal();
		User userPrincipal = null;
		if (principal instanceof User) {
			userPrincipal = (User) principal;
			Assert.isTrue(userPrincipal.getNewspapers().contains(newspaper));
		} else
			Assert.isTrue(principal.getClass().equals(Administrator.class));
	}
	public Collection<Newspaper> findNewspaperByKeyWord(final String word) {
		final Collection<Newspaper> newspapers;
		if (word.isEmpty())
			newspapers = this.findAllWithArticle();
		else
			newspapers = this.newspaperRepository.findNewspaperByKeyword(word);

		return newspapers;
	}

	public Collection<Newspaper> findByUser(final Integer userId) {
		final Collection<Newspaper> newspapers = this.newspaperRepository.findNewspaperByPublisher(userId);
		return newspapers;
	}

	public Collection<Newspaper> findAllNotPublished() {
		final Collection<Newspaper> newspapers = this.newspaperRepository.findAllNotPublished();
		return newspapers;
	}

	public Collection<Newspaper> findAllNewspaperPublicated() {
		final Collection<Newspaper> newspapers = this.newspaperRepository.findAllNewspaperPublicated();
		return newspapers;
	}

	public Collection<Newspaper> findAllWithArticle() {
		final Collection<Newspaper> newspapers = this.newspaperRepository.findAllNewspaperPublicated();
		// quitar los periodicos que no tienen todos los articulos como final
		// requisito 6.2
		Assert.notNull(newspapers);
		return newspapers;
	}

	public void delete(final Newspaper newspaper) {
		Assert.notNull(newspaper);
		this.checkPrincipal(newspaper);
		final Collection<Article> articles = newspaper.getArticles();
		if (articles != null)
			this.articleService.delete(articles);
		this.articleService.flush();

		final Collection<Subscription> suscriptions = this.suscriptionService.findSuscriptionToNewspaper(newspaper);
		if (suscriptions != null)
			this.suscriptionService.deleteInBatch(suscriptions);

		final User user = this.userService.findOneByNewspaper(newspaper.getId());
		user.getNewspapers().remove(newspaper);
		this.userService.save(user);
		this.userService.flush();

		this.newspaperRepository.delete(newspaper);
		this.newspaperRepository.flush();

	}

	public void deleteByAdministrator(final Newspaper newspaper) {
		Assert.notNull(newspaper);
		final Administrator admin = this.administratorService.findByPrincipal();
		Assert.notNull(admin);
		final User user = this.userService.findOneByNewspaper(newspaper.getId());
		final Collection<Article> articles = newspaper.getArticles();
		if (articles != null)
			this.articleService.delete(articles);
		this.articleService.flush();

		final Collection<Subscription> suscriptions = this.suscriptionService.findSuscriptionToNewspaper(newspaper);
		if (suscriptions != null)
			this.suscriptionService.deleteInBatch(suscriptions);
		this.suscriptionService.flush();

		final Collection<Advertisement> advertisements = this.advertisementService.findAllByNewspaperId(newspaper.getId());
		if (advertisements != null)
			this.advertisementService.deleteInBatch(advertisements);

		this.volumeService.removeNewspaperFromVolumes(newspaper.getId());

		user.getNewspapers().remove(newspaper);

		this.userService.save(user);
		this.userService.flush();

		this.newspaperRepository.delete(newspaper);
		this.newspaperRepository.flush();

	}
	public Newspaper findByArticle(final Article article) {
		Assert.notNull(article);
		final Newspaper result = this.newspaperRepository.findNewspaperByArticle(article);
		Assert.notNull(result);
		return result;
	}

	public void flush() {
		this.newspaperRepository.flush();

	}

	public Collection<Newspaper> findAll() {
		return this.newspaperRepository.findAll();
	}

	public Collection<Object> sumAvgAndCountfArticlesPerNewspaper() {
		return this.newspaperRepository.sumAvgAndCountfArticlesPerNewspaper();
	}

	public Collection<Newspaper> findNewspapersOverAvgOfArticles(final double avg) {

		return this.newspaperRepository.findNewspapersOverAvgOfArticles(avg);
	}

	//	@Autowired
	//	private Validator	validator;

	public Newspaper reconstruct(final NewspaperEditForm newspaperForm, final BindingResult binding) {
		Assert.notNull(newspaperForm);

		final Newspaper newspaper = this.newspaperRepository.findOne(newspaperForm.getId());
		Assert.notNull(newspaper);

		newspaper.setTitle(newspaperForm.getTitle());
		newspaper.setDescription(newspaperForm.getDescription());
		newspaper.setPublicationDate(newspaperForm.getPublicationDate());
		newspaper.setPicture(newspaperForm.getPicture());
		newspaper.setIsPrivate(newspaperForm.getIsPrivate());
		newspaper.setIsPublicated(newspaperForm.getIsPublicated());

		//this.validator.validate(newspaper, binding);
		return newspaper;
	}

	public Collection<Newspaper> findNewspapersUnderAvgOfArticles(final Double rasero) {

		return this.newspaperRepository.findNewspapersUnderAvgOfArticles(rasero);
	}

	public NewspaperEditForm constructEditForm(final Newspaper newspaper) {
		Assert.notNull(newspaper);
		final NewspaperEditForm res = new NewspaperEditForm();

		res.setTitle(newspaper.getTitle());
		res.setDescription(newspaper.getDescription());
		res.setPublicationDate(newspaper.getPublicationDate());
		res.setPicture(newspaper.getPicture());
		res.setPrivate(newspaper.getIsPrivate());
		res.setId(newspaper.getId());
		res.setIsPublicated(newspaper.getIsPublicated());

		return res;
	}

	public Collection<Newspaper> findAllPrivate() {
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof Customer);

		final Collection<Newspaper> newspaperPrivates = this.newspaperRepository.findAllPrivate();
		Assert.notNull(newspaperPrivates);

		return newspaperPrivates;
	}

	public Collection<Object> countsForRatioOfPublicNewspapers() {
		return this.newspaperRepository.countsForRatioOfPublicNewspapers();
	}

	public Double avgOfArticleInPrivateNewspapers() {
		return this.newspaperRepository.avgOfArticleInPrivateNewspapers();
	}

	public Double avgOfArticleInPublicNewspapers() {
		return this.newspaperRepository.avgOfArticleInPublicNewspapers();
	}

	public Double avgRatioOfPrivateNewspapersPerPublisher() {
		return this.newspaperRepository.avgRatioOfPrivateNewspapersPerPublisher();
	}
	public Collection<Newspaper> findSubscribedNewspapers(final int id) {
		return this.newspaperRepository.findSubscribedNewspapers(id);
	}
	public Collection<Newspaper> findNewspapersWithAdvertisements() {
		final Agent agent = this.agentService.findByPrincipal();
		Assert.notNull(agent);
		final Collection<Newspaper> result = new LinkedList<Newspaper>();
		final Collection<Newspaper> newspapers = this.newspaperRepository.findNewspapersWithAdvertisement();
		for (final Newspaper n : newspapers)
			if (!result.contains(n))
				result.add(n);
		return result;
	}

	// N2.0 C3 The ratio of newspapers that have at least one advertisement versus the newspapers that haven't any
	public Double ratioAtLeastOneAdvertisement() {
		return this.newspaperRepository.ratioAtLeastOneAdvertisement();
	}

	public Double avgOfNewspaperPerVolume() {
		final Double result = this.newspaperRepository.avgOfNewspaperPerVolume();
		return result != null ? result : 0.;
	}

	public Collection<Newspaper> findNewspapersWithoutAdvertisements() {
		final Set<Newspaper> result = new HashSet<Newspaper>(this.findAll());
		result.removeAll(this.findNewspapersWithAdvertisements());
		return result;
	}

	public Newspaper findOneToDisplay(final int newspaperId) {
		final Newspaper newspaper = this.findOne(newspaperId);
		Newspaper result = null;

		if (newspaper.getPublicationDate() == null) {
			final User user = this.userService.findByPrincipal();
			Assert.notNull(user);
			Assert.isTrue(user.getNewspapers().contains(newspaper));
			result = newspaper;
		} else if (newspaper.getIsPrivate()) {
			final Actor actor = this.actorService.findByPrincipal();
			Assert.notNull(actor, "The content is only for users");
			if (actor instanceof User) {
				final User user = this.userService.findByPrincipal();
				Assert.notNull(user, "The content is only for customers");
				result = newspaper;
			} else if (actor instanceof Customer) {
				final Customer customer = this.customerService.findByPrincipal();
				Assert.notNull(customer);
				result = newspaper;
			}
		} else
			result = newspaper;

		return result;
	}

}
