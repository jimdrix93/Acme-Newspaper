
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.FollowupRepository;
import domain.Article;
import domain.Followup;
import domain.User;

@Service
@Transactional
public class FollowupService {

	@Autowired
	private FollowupRepository	followupRepository;
	@Autowired
	private UserService			userService;
	@Autowired
	private ArticleService		articleService;


	// Constructors -----------------------------------------------------------

	public FollowupService() {
		super();
	}

	//CRUD methods
	public Followup create(final int articleId) {
		final User user = this.userService.findByPrincipal();
		final Article article = this.articleService.findOne(articleId);
		Assert.notNull(user);
		Assert.isTrue(user.getArticles().contains(article));
		Assert.isTrue(article.getPublicationMoment()!=null);
		final Followup followup = new Followup();
		final Collection<String> photos = new ArrayList<String>();
		final Date moment = new Date();
		followup.setPictures(photos);
		followup.setPublicationMoment(moment);

		return followup;
	}

	public Followup save(final Followup followup, final int articleId) {
		Followup saved;

		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);
		final Article article = this.articleService.findOne(articleId);
		Assert.notNull(article);
		Assert.isTrue(user.getArticles().contains(article));

		final Date moment = new Date(System.currentTimeMillis() - 1);
		followup.setPublicationMoment(moment);

		final String regex = "^((http|https)?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		final Collection<String> pictures = followup.getPictures();
		for (final String picture : pictures)
			Assert.isTrue(picture.matches(regex), "followup.picture.not.url");

		saved = this.followupRepository.save(followup);
		Assert.notNull(saved);

		if (followup.getId() == 0)
			article.getFollowups().add(saved);

		return saved;
	}

	public void flush() {
		this.followupRepository.flush();

	}

	//Other methods
	public Double avgFolloupsPerArticle() {
		return this.followupRepository.avgFolloupsPerArticle();
	}

	public Collection<Followup> findAllByArticle(final int articleId) {
		final Article article = this.articleService.findOne(articleId);
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);
		Assert.isTrue(user.getArticles().contains(article));

		final Collection<Followup> followups = this.followupRepository.findAllByArticle(article.getId());

		return followups;
	}

	public Followup findOneToDisplay(final int articleId, final int followupId) {
		final Collection<Followup> followups = this.findAllByArticle(articleId);
		final Followup followup = this.followupRepository.findOne(followupId);
		Assert.isTrue(followups.contains(followup));

		return followup;

	}

}
