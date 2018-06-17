
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import services.TabooWordService;
import utilities.AbstractTest;
import domain.TabooWord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class TabooWordServiceTest extends AbstractTest {

	@Autowired
	private TabooWordService	tabooWordService;

	/*
	 * Requerimientos:
	 * 17. An actor who is authenticated as an administrator must be able to:
	 * 		1. Manage a list of taboo words.
	 * 		2. List the articles that contain taboo words.
	 * 		3. List the newspapers that contain taboo words.
	 * 		4. List the chirps that contain taboo words.
	
	 */
	
	
	/*
	 * Un usuario autenticado como Admin crea una palabra tabú.
	 */
	@Test
	public void createTabooWord() {

		//System.out.println("-----Create tabooWord test. Positive 0, Negative 1 to 3.");

		final Object testingData[][] = {
			//Positives test cases
			{// P1: Admin create a taboo word
				"P1", "admin", "tabooText", null
			},

			//Negative test cases
			{// N1: Anonymous tries to create a taboo word
				"N1", "", "tabooText", IllegalArgumentException.class
			}, {//N2: User tries to create a taboo word
				"N2", "user1", "tabooText", ClassCastException.class
			}, {//N3: Admin create a taboo word without text
				"N3", "admin", "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateTabooWordTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(String) testingData[i][2], //tabooWord text
				(Class<?>) testingData[i][3]); //Exception class
	}
	protected void templateCreateTabooWordTest(final Integer i, final String nameTest, final String username, final String tabooText, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			final TabooWord tabooWord = this.tabooWordService.create();
			tabooWord.setText(tabooText);
			this.tabooWordService.save(tabooWord);
			this.tabooWordService.flush();

			//System.out.println(i + " Create TabooWord: " + nameTest + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(i + " Create TabooWord: " + nameTest + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Un usuario autenticado como Admin edita una palabra tabú.
	 */
	@Test
	public void editTabooWord() {

		//System.out.println("-----Edit tabooWord test. Positive 0-1, Negative 2-5.");

		final Object testingData[][] = {
			//Positives test cases
			{// P1: Admin edit a taboo word
				"P1", "admin", "tabooWord1", "tabooText", null
			}, {// P2: Admin edit a taboo word
				"P2", "admin", "tabooWord2", "tabooText2", null
			},

			//Negative test cases
			{// N1: Anonymous tries to edit a taboo word
				"N1", "", "tabooWord1", "tabooText", IllegalArgumentException.class
			}, {//N2: User tries to edit a taboo word
				"N2", "user1", "tabooWord1", "tabooText", ClassCastException.class
			}, {//N3: Admin edit a taboo word without text
				"N3", "admin", "tabooWord1", "", ConstraintViolationException.class
			}, {//N4: Admin tries to edit a inexistent taboo word 
				"N4", "admin", "tabooWord7", "tabooText", NumberFormatException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditTabooWordTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(String) testingData[i][2], //tabooWord id
				(String) testingData[i][3], //tabooWord text
				(Class<?>) testingData[i][4]); //Exception class
	}
	protected void templateEditTabooWordTest(final Integer i, final String nameTest, final String username, final String tabooId, final String tabooText, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			final TabooWord tabooWord = this.tabooWordService.findOne(super.getEntityId(tabooId));
			tabooWord.setText(tabooText);
			this.tabooWordService.save(tabooWord);
			this.tabooWordService.flush();

			//System.out.println(i + " Edit TabooWord: " + nameTest + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(i + " Edit TabooWord: " + nameTest + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Un usuario autenticado como Admin edita una palabra tabú.
	 */
	@Test
	public void deleteTabooWord() {

		//System.out.println("-----Delete tabooWord test. Positive 0-1, Negative 2-4.");

		final Object testingData[][] = {
			//Positives test cases
			{// P1: Admin delete a taboo word
				"P1", "admin", "tabooWord1", null
			}, {// P2: Admin delete a taboo word
				"P2", "admin", "tabooWord2", null
			},

			//Negative test cases
			{// N1: Anonymous tries to edit a taboo word
				"N1", "", "tabooWord1", IllegalArgumentException.class
			}, {//N2: User tries to edit a taboo word
				"N2", "user1", "tabooWord1", ClassCastException.class
			}, {//N3: Admin tries to delete a inexistent taboo word 
				"N3", "admin", "tabooWord7", NumberFormatException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteTabooWordTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(String) testingData[i][2], //tabooWord id
				(Class<?>) testingData[i][3]); //Exception class
	}
	protected void templateDeleteTabooWordTest(final Integer i, final String nameTest, final String username, final String tabooId, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			final TabooWord tabooWord = this.tabooWordService.findOne(super.getEntityId(tabooId));
			this.tabooWordService.delete(tabooWord);
			this.tabooWordService.flush();

			//System.out.println(i + " Delete TabooWord: " + nameTest + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(i + " Delete TabooWord: " + nameTest + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}
}
