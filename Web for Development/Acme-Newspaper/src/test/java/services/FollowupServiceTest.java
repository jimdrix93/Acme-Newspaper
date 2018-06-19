
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import services.FollowupService;
import utilities.AbstractTest;
import domain.Followup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class FollowupServiceTest extends AbstractTest {

	@Autowired
	private FollowupService	followupService;

	
	/*
	 * Requerimientos:
	 * 14. The writer of an article may write follow-ups on it. Follow-ups can be written only after an
	 * article is saved in final mode and the corresponding newspaper is published. For every fol-
	 * low-up, the system must store the following data: title, publication moment, summary, text,
	 * and optional pictures.
	 * 
	 */

	/*
	 * Un usuario autenticado como User:
	 * El escritor de un artículo puede escribir follow-ups en él.Los Follow-ups
	 * pueden ser escritos solo después de que un artículo este en modo final y el periodico
	 * publicado
	 */
	@Test
	public void createFollowup() {

		//System.out.println("-----Create Followup test. Positive 0, Negative 1 to 4.");

		final Object testingData[][] = {
			//Positives test cases
			{// P1: User create a followup
				"P1", "user2", "article4", "titleFollow", "summaryFollow", "textFollow", "http://photo.com", null
			},

			//Negative test cases
			{// N1: User tries create a followup in an article that is not his
				"N1", "user1", "article4", "titleFollow", "summaryFollow", "textFollow", "http://photo.com", IllegalArgumentException.class
			}, {// N2: User create a followup without title
				"N2", "user2", "article4", null, "summaryFollow", "textFollow", "http://photo.com", ConstraintViolationException.class
			}, {// N3: User create a followup without title
				"N3", "user2", "article4", "titleFollow", null, "textFollow", "http://photo.com", ConstraintViolationException.class
			}, {// N4: User create a followup without title
				"N4", "user2", "article4", "titleFollow", "summaryFollow", null, "http://photo.com", ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateFollowupTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(String) testingData[i][2], //articleId
				(String) testingData[i][3], //title follow
				(String) testingData[i][4], //summary follow
				(String) testingData[i][5], //text follow
				(String) testingData[i][6], //photo follow
				(Class<?>) testingData[i][7]); //Exception class
	}
	protected void templateCreateFollowupTest(final Integer i, final String nameTest, final String username, final String articleId, final String titleFollow, final String summaryFollow, final String textFollow, final String photoFollow,
		final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			final Followup followup = this.followupService.create(super.getEntityId(articleId));
			followup.setTitle(titleFollow);
			followup.setText(textFollow);
			followup.setSummary(summaryFollow);
			followup.getPictures().add(photoFollow);

			this.followupService.save(followup, super.getEntityId(articleId));
			this.followupService.flush();

			//System.out.println(i + " Create Followup: " + nameTest + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(i + " Create Followup: " + nameTest + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}

}
