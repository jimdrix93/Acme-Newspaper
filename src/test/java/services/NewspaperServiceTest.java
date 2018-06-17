
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Article;
import domain.Newspaper;
import domain.TabooWord;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class NewspaperServiceTest extends AbstractTest {

	@Autowired
	private ArticleService		articleService;
	@Autowired
	private NewspaperService	newspaperService;
	@Autowired
	private UserService			userService;
	@Autowired
	private TabooWordService	tabooWordService;


	/*
	 * Requerimientos:
	 * 2. A user may create a newspaper, for which the system must store a title, a publication date
	 * (year, month, and day), a description, an optional picture, and the list of articles of which it is
	 * composed.
	 * 4. An actor who is not authenticated must be able to:
	 * 2. List the newspapers that are published and browse their articles.
	 * 5. Search for a published newspaper using a single keyword that must appear some-
	 * where in its title or its description.
	 * 6. An actor who is authenticated as a user must be able to:
	 * 1. Create a newspaper. A user who has created a newspaper is commonly referred to
	 * as a publisher.
	 * 2. Publish a newspaper that he or she's created. Note that no newspaper can be pub-
	 * lished until each of the articles of which it is composed is saved in final mode.
	 * 7. An actor who is authenticated as an administrator must be able to:
	 * 2. Remove a newspaper that he or she thinks is inappropriate. Removing a newspaper
	 * implies removing all of the articles of which it is composed.
	 * 20. Newspapers can be public or private. Public newspapers can be browsed by any user; pri-
	 * vate newspapers can be only browsed by customers who have subscribed to them.
	 * 23. An actor who is authenticated as a user must be able to:
	 * 1. Decide on whether a newspaper that he or she's created is public or private.
	 * 
	 * Acme Newspaper v2
	 * 4. An actor who is authenticated as an agent must be able to:
	 * 4.3 List the newspapers in which they have placed an advertisement.
	 * 4.4 List the newspapers in which they have not placed any advertisements.
	 */

	/*
	 * AGF
	 * Caso de uso:
	 * Usuario anónimo listando los newspapers publicados y mostrando sus artículos
	 */
	@Test
	public void anonymousUserListingNewspapersTest() {

		//System.out.println("-----anonymousUserListingNewspapersTest. Positive 0, Negative 1 to 2");

		final Object testingData[][] = {
			// Positive
			{
				"newspaper3", "article4", null
			},
			// Negative: article not from newspaper
			{
				"newspaper1", "article4", IllegalArgumentException.class
			},
			// Negative: newspaper not exists
			{
				"newspaper6", "article4", NumberFormatException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAnonymousUserListingNewspapersTest(i, (String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateAnonymousUserListingNewspapersTest(final Integer i, final String newspaperName, final String articleName, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.unauthenticate();

			final Collection<Newspaper> newspapers = this.newspaperService.findAllWithArticle();
			final Integer newspaperId = this.getEntityId(newspaperName);
			final Newspaper newspaper = this.newspaperService.findOne(newspaperId);

			Assert.isTrue(newspapers.contains(newspaper));

			final Collection<Article> articles = newspaper.getArticles();

			final Integer articleId = this.getEntityId(articleName);
			final Article article = this.articleService.findOne(articleId);
			Assert.isTrue(articles.contains(article));

			//System.out.println(i.toString() + " ok ");
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(i.toString() + oops.getMessage());
		}
		super.checkExceptions(expected, caught);
	}

	/**
	 * AGF
	 * Caso de uso:
	 * Usuario logado escribiendo un articulo y añadiendolo a un periodico no publicado aún. Posteriormente editandolo
	 */
	@Test
	public void userWritingArticleAndAttachingToNewspaper() {

		//System.out.println("-----userWritingArticleAndAttachingToNewspaper. Positive 0, Negative 1 to 2");

		final Object testingData[][] = {
			//Positive case
			{
				"user1", "newspaper1", "Lluvia torrencial", "Sevillanos mojados", "Madre de dios que frio", "nuevo body", null
			},
			// Negative case: Newspaper is publicated
			{
				"user1", "newspaper3", "Lluvia torrencial", "Sevillanos mojados", "Madre de dios que frio", "nuevo body", IllegalArgumentException.class
			},
			// Negative case: article without title
			{
				"user1", "newspaper3", "title", "Sevillanos mojados", "Madre de dios que frio", "nuevo body", IllegalArgumentException.class
			}
		};

		//System.out.println("-----userWritingArticleAndAttachingToNewspaper");

		for (int i = 0; i < testingData.length; i++)
			this.templateUserWritingArticleAndAttachingToNewspaper(i, (String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5],
				(Class<?>) testingData[i][6]);
	}

	protected void templateUserWritingArticleAndAttachingToNewspaper(final Integer i, final String userName, final String newspaperName, final String title, final String summary, final String body, final String nuevoBody, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(userName);
			final User user = this.userService.findByPrincipal();

			final Article article = this.articleService.create();

			article.setTitle(title);
			article.setSummary(summary);
			article.setBody(body);

			final Integer newspaperId = this.getEntityId(newspaperName);
			Newspaper newspaper = this.newspaperService.findOne(newspaperId);

			article.setNewspaper(newspaper);
			final Article saved = this.articleService.save(article);
			Assert.isTrue(user.getArticles().contains(saved));

			newspaper = this.newspaperService.findOne(newspaperId);
			Assert.isTrue(newspaper.getArticles().contains(saved));
			saved.setBody(nuevoBody);

			final Article saved2 = this.articleService.save(saved);
			this.articleService.flush();
			Assert.isTrue(saved2.getBody().equals(nuevoBody));

			//System.out.println(i.toString() + " ok ");
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(i.toString() + oops.getMessage());
		}
		super.checkExceptions(expected, caught);
	}

	/**
	 * Caso de uso:
	 * Usuario anonimo busca un newspaper publicado por palabra clave.
	 */
	@Test
	public void anonymousSearchNewspaperByKeywordTest() {

		//System.out.println("-----Create article test. Positive 0, Negative 1.");
		final Object testingData[][] = {
			{//Positive case
				"P1", "3", null
			}, { // Negative case
				"N1", null, NullPointerException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAnonymousSearchNewspaperByKeywordTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Word to search
				(Class<?>) testingData[i][2]); //Exception class
	}

	protected void templateAnonymousSearchNewspaperByKeywordTest(final Integer i, final String nameTest, final String word, final Class<?> expected) {
		Class<?> caught;
		final Collection<Newspaper> newspapersResult = new LinkedList<Newspaper>();
		caught = null;
		try {
			final Collection<Newspaper> newspapers = this.newspaperService.findAll();
			final Collection<Newspaper> newspapersByKeyword = this.newspaperService.findNewspaperByKeyWord(word);

			for (final Newspaper newspaper : newspapers)
				if (newspaper.getIsPublicated() && (newspaper.getTitle().contains(word) || newspaper.getDescription().contains(word)))
					newspapersResult.add(newspaper);

			Assert.isTrue(newspapersResult.equals(newspapersByKeyword), "Newspaper no se ha borrado.");

			//System.out.println(String.format("%s anonymousSearchNewspaperByKeyword: %s ok.", i, nameTest));
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(String.format("%s anonymousSearchNewspaperByKeyword: %s -> %s", i, nameTest, oops.getClass().toString()));
		}
		super.checkExceptions(expected, caught);
	}
	/**
	 * Caso de uso:
	 * Usuario crea un newspaper
	 * 
	 * @throws ParseException
	 */
	@Test
	public void userCreateNewspaperTest() throws ParseException {

		//System.out.println("-----Create article test. Positive 0, Negative 1.");
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		final Object testingData[][] = {
			{//Positive case
				"P1", "user1", "news1", "news1-Description", false, false, "http://uri.com", sdf.parse("01/09/2018"), null
			}, { // Negative case: Anonymous can not create a newspaper
				"N1", "", "news1", "news1-Description", false, false, "http://uri.com", sdf.parse("01/09/2018"), IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateUserCreateNewspaperTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //username
				(String) testingData[i][2],//title
				(String) testingData[i][3],// description
				(Boolean) testingData[i][4],//isPrivate
				(Boolean) testingData[i][5],//isPublicated
				(String) testingData[i][6],// Picture
				(Date) testingData[i][7],// publicationDate
				(Class<?>) testingData[i][8]); //Exception class
	}

	protected void templateUserCreateNewspaperTest(final Integer i, final String nameTest, final String username, final String title, final String description, final Boolean isPrivate, final Boolean isPublicated, final String picture,
		final Date publicationDate, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);

			final Newspaper newspaper = new Newspaper();
			newspaper.setTitle(title);
			newspaper.setDescription(description);
			newspaper.setIsPrivate(isPrivate);
			newspaper.setIsPublicated(isPublicated);
			newspaper.setPicture(picture);
			newspaper.setPublicationDate(publicationDate);

			final Newspaper newspaperSaved = this.newspaperService.save(newspaper);

			Assert.isTrue(this.newspaperService.findAll().contains(newspaperSaved));
			//System.out.println(String.format("%s UserCreateNewspaperTest: %s ok.", i, nameTest));
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(String.format("%s UserCreateNewspaperTest: %s -> %s", i, nameTest, oops.getClass().toString()));
		}
		super.checkExceptions(expected, caught);
	}

	/**
	 * Caso de uso:
	 * Usuario publica un newspaper que haya creado.
	 * No se debe poder publicar hasta que todos los artículos que lo forman estén en finalMode
	 * 
	 */
	@Test
	public void userPublicateNewspaperTest() throws ParseException {

		//System.out.println("-----Create article test. Positive 0, Negative 1.");

		final Object testingData[][] = {
			{//Positive case
				"P1", "user2", "newspaper3", null
			}, { // Negative case: Anonymous can not create a newspaper
				"P1", "user1", "newspaper1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateUserPublicateNewspaperTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //username
				super.getEntityId((String) testingData[i][2]),// newspaperId
				(Class<?>) testingData[i][3]); //Exception class
	}

	protected void templateUserPublicateNewspaperTest(final Integer i, final String nameTest, final String username, final Integer newspaperId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);

			final Newspaper newspaper = this.newspaperService.findOne(newspaperId);
			newspaper.setIsPublicated(true);
			newspaper.setPublicationDate(new Date());
			final Newspaper newspaperSaved = this.newspaperService.save(newspaper);
			this.newspaperService.flush();

			Assert.isTrue(this.newspaperService.findAll().contains(newspaperSaved));

			//System.out.println(String.format("%s userPublicateNewspaperTest: %s ok.", i, nameTest));
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(String.format("%s userPublicateNewspaperTest: %s -> %s", i, nameTest, oops.getClass().toString()));
		}
		super.checkExceptions(expected, caught);
	}

	/**
	 * Caso de uso:
	 * Administrador borrar un newspaper que encuentre inapropiado.
	 * Esto implica borrar todos los artículos de los que está compuesto.
	 * 
	 */
	@Test
	public void adminDeleteNewspaperInapropiateTest() throws ParseException {

		//System.out.println("-----Create article test. Positive 0, Negative 1.");

		final Object testingData[][] = {
			{//Positive case
				"P1", "admin", "newspaper1", null
			}, { // Negative case: Anonymous can not create a newspaper
				"P1", "", "newspaper2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAdminDeleteNewspaperInapropiateTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //username
				super.getEntityId((String) testingData[i][2]),// newspaperId
				(Class<?>) testingData[i][3]); //Exception class
	}

	protected void templateAdminDeleteNewspaperInapropiateTest(final Integer i, final String nameTest, final String username, final Integer newspaperId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);

			final Newspaper newspaper = this.newspaperService.findOne(newspaperId);

			this.newspaperService.delete(newspaper);
			this.newspaperService.flush();

			Assert.isTrue(!this.newspaperService.findAll().contains(newspaper));

			//System.out.println(String.format("%s adminDeleteNewspaperInapropiate: %s ok.", i, nameTest));
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(String.format("%s adminDeleteNewspaperInapropiate: %s -> %s", i, nameTest, oops.getClass().toString()));
		}
		super.checkExceptions(expected, caught);
	}

	/**
	 * Caso de uso:
	 * Administrador lista newspaper que contienen taboo words
	 */
	@Test
	public void adminListNewspaperWithTabooWordsTest() {

		//System.out.println("-----Create article test. Positive 0, Negative 1.");
		final Object testingData[][] = {
			{//Positive case
				"P1", "admin", null
			}, { // Negative case: Delete an article non-existent
				"N1", "user1", ClassCastException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAdminListNewspaperWithTabooWordsTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //username
				(Class<?>) testingData[i][2]); //Exception class
	}

	protected void templateAdminListNewspaperWithTabooWordsTest(final Integer i, final String nameTest, final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			final Collection<Newspaper> newspapers = this.tabooWordService.findAllNewspapersWithTabooWord();
			final Collection<TabooWord> tabooWords = this.tabooWordService.findAll();

			boolean isTaboo = false;
			for (final Newspaper newspaper : newspapers)
				for (final TabooWord tabooWord : tabooWords)
					if (newspaper.getTitle().contains(tabooWord.getText()) || newspaper.getDescription().contains(tabooWord.getText()))
						isTaboo = true;

			Assert.isTrue(isTaboo);
			//System.out.println(String.format("%s AdminDeleteInapropiateNewspaper: %s ok.", i, nameTest));
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(String.format("%s AdminDeleteInapropiateNewspaper: %s -> %s", i, nameTest, oops.getClass().toString()));
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Caso de uso:
	 * Agent-> Listar newspapers en los que ha puesto un advertisement(CU38)
	 */

	@Test
	public void agentListNewspaperWithTheirAdvertisement() {

		final Object testingData[][] = {
			{//Positive case
				"agent1", null
			}, { // Negative case: logueado como user
				"user1", IllegalArgumentException.class
			}, { // Negative case: logueado como customer
				"customer1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAgentListNewspaperWithTheirAdvertisement(i, (String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	protected void templateAgentListNewspaperWithTheirAdvertisement(final Integer i, final String name, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(name);
			final Collection<Newspaper> lista = this.newspaperService.findNewspapersWithAdvertisements();
			Assert.notNull(lista);
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//		System.out.println(i.toString() + oops.getMessage());
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Caso de uso:
	 * 4.4. Agent-> Listar newspapers en los que no ha puesto un advertisement(CU39)
	 */

	@Test
	public void agentListNewspaperWithOutTheirAdvertisement() {

		final Object testingData[][] = {
			{//Positive case
				"agent1", null
			}, { // Negative case: logueado como user
				"user1", IllegalArgumentException.class
			}, { // Negative case: logueado como customer
				"customer1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAgentListNewspaperWithTheirAdvertisement(i, (String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	protected void templateAgentListNewspaperWithOutTheirAdvertisement(final Integer i, final String name, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(name);
			final Collection<Newspaper> lista = this.newspaperService.findNewspapersWithoutAdvertisements();
			Assert.notNull(lista);
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//		System.out.println(i.toString() + oops.getMessage());
		}
		super.checkExceptions(expected, caught);
	}

}
