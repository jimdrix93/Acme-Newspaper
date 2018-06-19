
package services;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ArticleRepository;
import domain.Actor;
import domain.Administrator;
import domain.Article;
import domain.Customer;
import domain.Followup;
import domain.Newspaper;
import domain.Subscription;
import domain.User;

@Service
@Transactional
public class ArticleService {

	// Managed repositories ------------------------------------------------
	@Autowired
	private ArticleRepository		articleRepository;

	//Services
	@Autowired
	private AdministratorService	administratorService;
	@Autowired
	private UserService				userService;
	@Autowired
	private NewspaperService		newspaperService;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private CustomerService			customerService;
	@Autowired
	private SubscriptionService		subscriptionService;


	// Constructor ----------------------------------------------------------
	public ArticleService() {
		super();
	}

	// Methods CRUD ---------------------------------------------------------

	public Article findOne(final int articleId) {
		Article result;

		result = this.articleRepository.findOne(articleId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Article> findAll() {

		Collection<Article> result;

		result = this.articleRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Collection<Article> findAllByNewspaper(final int newspaperId) {
		Assert.notNull(newspaperId);
		final Collection<Article> result = this.articleRepository.findAllByNewspaper(newspaperId);

		return result;
	}

	public Collection<Article> findAllByNewspaperId(final int newspaperId) {
		Assert.notNull(newspaperId);
		final Collection<Article> result = this.articleRepository.findAllByNewspaperId(newspaperId);

		return result;
	}

	public Collection<Article> findAllByUser(final int userId) {
		Assert.notNull(userId);

		final Collection<Article> result = this.articleRepository.findAllByUser(userId);

		return result;
	}

	public Article create() {
		final Article article = new Article();
		article.setPublicationMoment(new Date(System.currentTimeMillis() - 1));
		final Collection<String> pictures = new LinkedList<String>();
		final Collection<Followup> followups = new LinkedList<Followup>();
		article.setPictures(pictures);
		article.setFollowups(followups);

		return article;

	}

	public Article changePublicationDate(final Article article, final Date publicationDate) {
		article.setPublicationMoment(publicationDate);
		final Article saved = this.articleRepository.saveAndFlush(article);

		return saved;

	}

	public Article save(final Article article) {
		Assert.notNull(article);
		final User user = this.userService.findByPrincipal();

		if (article.getId() != 0) {
			Assert.isTrue(user.getArticles().contains(article));

			final Article oldArticle = this.articleRepository.findOne(article.getId());
			Assert.isTrue(!oldArticle.isFinalMode());
		}
		Article saved;

		final String regex = "^((http|https)?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		final Collection<String> pictures = article.getPictures();
		for (final String picture : pictures)
			Assert.isTrue(picture.matches(regex), "article.picture.not.url");

		article.setPublicationMoment(article.getNewspaper().getPublicationDate());

		saved = this.articleRepository.save(article);

		if (article.getId() == 0) {
			user.getArticles().add(saved);
			this.userService.save(user);
			final Newspaper newspaper = this.newspaperService.findOne(article.getNewspaper().getId());
			newspaper.getArticles().add(saved);
			this.newspaperService.save(newspaper);
		}

		return saved;
	}

	public void delete(final Article article) {
		Assert.notNull(article);
		final User user = this.userService.findOneByArticle(article.getId());
		Assert.isTrue(user.equals(this.actorService.findByPrincipal()) || this.actorService.findByPrincipal().getClass().equals(Administrator.class), "Usuario no admin o no propietario.");

		Newspaper newspaper = this.newspaperService.findByArticle(article);
		final Collection<Article> userArticles = user.getArticles();
		userArticles.remove(article);
		this.userService.save(user);
		this.userService.flush();
		newspaper.getArticles().remove(article);
		newspaper = this.newspaperService.update(newspaper);
		this.newspaperService.flush();
		article.getPictures().clear();
		this.articleRepository.delete(article);
		this.flush();

	}
	public void deleteInBatch(final Collection<Article> articles) {
		Assert.notNull(articles);
		final Administrator admin = this.administratorService.findByPrincipal();
		this.articleRepository.deleteInBatch(articles);
	}

	public void flush() {
		this.articleRepository.flush();

	}

	public Collection<Article> findArticleByKeyWord(final String word) {
		final Collection<Article> articles;
		if (word.isEmpty())
			articles = this.findAllPublished();
		else
			articles = this.articleRepository.findArticleByKeyword(word);

		return articles;
	}

	public void delete(final Collection<Article> articles) {

		Assert.notNull(articles);
		final Administrator admin = this.administratorService.findByPrincipal();
		while (!articles.isEmpty())
			this.delete(articles.iterator().next());
	}

	public Collection<Article> findArticlesPublishedInNewspapersByUser(final User user) {

		Assert.notNull(user);
		return this.articleRepository.findArticlesPublishedInNewspapersByUser(user);

	}

	public Double findAvgFollowupPerArticle1Week() {

		return this.articleRepository.findAvgFollowupPerArticle1Week();

	}

	public Double findAvgFollowupPerArticle2Week() {

		return this.articleRepository.findAvgFollowupPerArticle2Week();

	}

	public Article findOneToEdit(final int articleId) {
		Article result;
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);

		result = this.articleRepository.findOne(articleId);
		Assert.notNull(result);

		Assert.isTrue(user.getArticles().contains(result));

		Assert.isTrue(!result.isFinalMode());

		return result;
	}

	public Collection<Article> findAllPublished() {
		final Collection<Article> articles = this.articleRepository.findAllPublished();
		return articles;
	}

	public Article findOneToDisplay(final int articleId) {
		final Article article = this.findOne(articleId);
		Article result = null;
		if (article.getPublicationMoment() == null) {
			final User user = this.userService.findByPrincipal();
			Assert.notNull(user);
			Assert.isTrue(user.getArticles().contains(article));
			result = article;
		} else {
			final Newspaper newspaper = this.newspaperService.findByArticle(article);
			if (newspaper.getIsPrivate()) {
				final Actor actor = this.actorService.findByPrincipal();
				Assert.notNull(actor, "The content is only for users");
				if (actor instanceof User) {
					final User user = this.userService.findByPrincipal();
					Assert.notNull(user);
					Assert.isTrue(user.getArticles().contains(article), "The content is only for customers");
					result = article;
				} else if (actor instanceof Customer) {
					final Customer customer = this.customerService.findByPrincipal();
					Assert.notNull(customer);
					final Subscription subscription = this.subscriptionService.findByCustomerAndNewspaper(customer, newspaper);
					Assert.notNull(subscription, "The content is only for subscribers");
					result = article;
				}
			} else
				result = article;

		}
		return result;
	}
}
