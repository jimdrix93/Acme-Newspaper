
package services;

import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import services.ArticleService;
import services.TabooWordService;
import services.UserService;
import utilities.AbstractTest;
import domain.Article;
import domain.TabooWord;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class ArticleServiceTest extends AbstractTest {

	@Autowired
	private ArticleService	articleService;
	@Autowired
	TabooWordService		tabooWordService;
	@Autowired
	private UserService		userService;


	/*
	 * Requerimientos:
	 * 3. For each article, the system must store a title, the moment when it is published, a summary,
	 * a piece of text (the body), and some optional pictures. An article is published when the cor-
	 * responding newspaper is published.
	 * 4. An actor who is not authenticated must be able to:
	 * 4. Search for a published article using a single key word that must appear somewhere
	 * in its title, summary, or body
	 * 6. An actor who is authenticated as a user must be able to:
	 * 3. Write an article and attach it to any newspaper that has not been published, yet.
	 * Note that articles may be saved in draft mode, which allows to modify them later, or
	 * final model, which freezes them forever.
	 * 7. An actor who is authenticated as an administrator must be able to:
	 * 1. Remove an article that he or she thinks is inappropriate.
	 */

	/*
	 * AGF
	 * Caso de uso:
	 * Usuario logeandose como user, listando sus articulos y editando uno.
	 */
	@Test
	public void userListingArticlesAndEditingOneTest() {

		//System.out.println("-----userListingArticlesAndEditingOneTest. Positive 0 to 4, Negative 5 to 9");

		final Object testingData[][] = {
			//Positive cases
			{
				"user1", "article1", "Riadas en Sevilla", "Consectetur adipiscing elit, sed eiusmod tempor incidunt", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. ", null
			},
			{
				"user1", "article6", "Estudiantes de informática se rebelan", "Toman las principales instituciones y decretan el estado de emergencia", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed eiusmod tempor incidunt", null
			},
			{
				"user1", "article1", "Científicos descubren que la ingenería del software es un timo", "Es independiente su uso para que un proyecto tenga éxito o no.",
				"Científicos demuestran matemáticamente que solo sirve para mover pdf y papelitos de arriba a abajo, Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed eiusmod tempor in", null
			},
			{
				"user1", "article1", "Nuevo Master en la US: Cifuentelogía", "Los alumnos aprenderan la mejor manera de triunfar con títulos sin tener ni idea", "Consectetur adipiscing elit, sed eiusmod tempor incidunt", null
			},
			{
				"user1", "article6", "Descubren que la mayoría de proyectos software los dirige gente que no aprobaría el graduado escolar", "Cientificos de la Universidad de Wiskonsin, mediante estudios en ratones, han descubierto que ....",
				"Consectetur adipiscing elit, sed eiusmod tempor incidunt", null
			},

			// Negative cases
			//Without title
			{
				"user1", "article1", "", "Resumen", "Detalle", ConstraintViolationException.class
			},
			//Without summary
			{
				"user1", "article2", "Título", "", "Detalle", ConstraintViolationException.class
			},
			//Without body
			{
				"user2", "article3", "Título", "Resumen", "", ConstraintViolationException.class
			},
			//Without user
			{
				"", "article4", "Título", "Resumen", "Detalle", IllegalArgumentException.class
			},
			//Without correct pictures
			{
				"user1", "article4", "Título", "Resumen", "Detalle", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateUserListingArticlesAndEditingOneTest(i, (String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	protected void templateUserListingArticlesAndEditingOneTest(final Integer i, final String username, final String article, final String title, final String summary, final String body, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			final User user = this.userService.findByPrincipal();
			final Collection<Article> articles = this.articleService.findAllByUser(user.getId());
			Article saved = null;
			for (final Article art : articles) {
				if (art.getId() == this.getEntityId(article))

					art.setTitle(title);
				art.setSummary(summary);
				art.setBody(body);
				saved = this.articleService.save(art);
				this.articleService.flush();

				break;
			}

			Assert.isTrue(user.getArticles().contains(saved));
			Assert.isTrue(saved.getBody().equals(body));

			super.unauthenticate();

			//System.out.println(i.toString() + " ok ");
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(i.toString() + oops.getMessage());
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * AGF
	 * Caso de uso:
	 * Usuario no autenticado buscando un articulo por palabra clave
	 */
	@Test
	public void anonymousUserSearchingForArticles() {

		//System.out.println("------  anonymousUserSearchingForArticles. Positive 0, Negative 1 to 2");

		final Object testingData[][] = {
			{//Positive case
				"article4", "Article4", null
			}, { // Negative case: Sin artículos en el resultado
				"article1", "artiiasdaisas", IllegalArgumentException.class
			}, { // Negative case: Con palabra de búsqueda vacía
				"article1", "", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAnonymousUserSearchingForArticles(i, //Nº Positive/Negative
				(String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templateAnonymousUserSearchingForArticles(final Integer i, final String articleName, final String searchParam, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.unauthenticate();

			final Integer articleId = this.getEntityId(articleName);
			final Article article = this.articleService.findOne(articleId);

			final Collection<Article> articles = this.articleService.findArticleByKeyWord(searchParam);

			Assert.isTrue(articles.contains(article));

			//System.out.println(i.toString() + " ok ");
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(i.toString() + oops.getMessage());
		}
		super.checkExceptions(expected, caught);
	}

	/**
	 * Caso de uso:
	 * Administrador borra un artículo que encuentre inapropiado
	 */
	@Test
	public void adminDeleteInapropiateArticleTest() {

		//System.out.println("-----adminDeleteInapropiateArticleTest. Positive 0, Negative 1.");
		final Object testingData[][] = {
			{//Positive case
				"P1", "admin", "article1", null
			}, { // Negative case: Delete an article non-existent
				"N1", "admin", "article1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAdminDeleteInapropiateArticleTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //username
				super.getEntityId((String) testingData[i][2]), //Article1
				(Class<?>) testingData[i][3]); //Exception class
	}

	protected void templateAdminDeleteInapropiateArticleTest(final Integer i, final String nameTest, final String username, final Integer articleId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			final Article article = this.articleService.findOne(articleId);
			this.articleService.delete(article);
			this.articleService.flush();

			Assert.isTrue(!this.articleService.findAll().contains(article), "Articulo no se ha borrado.");

			//System.out.println(String.format("%s AdminDeleteInapropiateArticle: %s ok.", i, nameTest));
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(String.format("%s AdminDeleteInapropiateArticle: %s -> %s", i, nameTest, oops.getClass().toString()));
		}
		super.checkExceptions(expected, caught);
	}

	/**
	 * Caso de uso:
	 * Administrador lista articles que contienen taboo words
	 */
	@Test
	public void adminListArticleWithTabooWordsTest() {

		//System.out.println("-----Create article test. Positive 0, Negative 1.");
		final Object testingData[][] = {
			{//Positive case
				"P1", "admin", null
			}, { // Negative case: Delete an article non-existent
				"N1", "user1", ClassCastException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAdminListArticleWithTabooWordsTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //username
				(Class<?>) testingData[i][2]); //Exception class
	}

	protected void templateAdminListArticleWithTabooWordsTest(final Integer i, final String nameTest, final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			final Collection<Article> articles = this.tabooWordService.findAllArticlesWithTabooWord();
			final Collection<TabooWord> tabooWords = this.tabooWordService.findAll();
			boolean isTaboo = false;

			for (final Article article : articles)
				for (final TabooWord tabooWord : tabooWords)
					if (article.getTitle().contains(tabooWord.getText()) || article.getSummary().contains(tabooWord.getText()) || article.getBody().contains(tabooWord.getText()))
						isTaboo = true;

			Assert.isTrue(isTaboo);

			//System.out.println(String.format("%s AdminDeleteInapropiateArticle: %s ok.", i, nameTest));
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(String.format("%s AdminDeleteInapropiateArticle: %s -> %s", i, nameTest, oops.getClass().toString()));
		}
		super.checkExceptions(expected, caught);
	}
}
