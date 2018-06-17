
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

import services.ChirpService;
import services.TabooWordService;
import utilities.AbstractTest;
import domain.Chirp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class ChirpServiceTest extends AbstractTest {

	@Autowired
	private ChirpService		chirpService;
	@Autowired
	private TabooWordService	tabooWordService;


	/*
	 * Requerimientos:
	 * 15. A user may post a chirp. For every chirp, the system must store the moment, a title, and a
	 * description. The list or chirps are considered a part of the profile of a user.
	 * 16. An actor who is authenticated as a user must be able to:
	 * 		1. Post a chirp. Chirps may not be changed or deleted once they are posted.
	 * 		5. Display a stream with the chirps posted by all of the users that he or she follows
	 * 17. An actor who is authenticated as an administrator must be able to:
	 * 		5. Remove a chirp that he or she thinks is inappropriate
	 * 
	 */
	
	/*
	 * Un usuario autenticado como User postea un Chirp.
	 */
	@Test
	public void createChirp() {

		//System.out.println("-----Create chirp test. Positive 0, Negative 1 to 3.");

		final Object testingData[][] = {
			//Positives test cases
			{// P1: User create a chirp
				"P1", "user1", "chirpTitle", "chirpDescription", null
			},

			//Negative test cases
			{// N1: Anonymous tries to create a chirp
				"N1", "", "chirpTitle", "chirpDescription", IllegalArgumentException.class
			}, {//N2: User create a chirp without title
				"N2", "user1", "", "chirpDescription", ConstraintViolationException.class
			}, {//N3: User create a chirp without description
				"N3", "user1", "chirpTitle", "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateChirpTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(String) testingData[i][2], //chirp title
				(String) testingData[i][3], //chirp description
				(Class<?>) testingData[i][4]); //Exception class
	}

	protected void templateCreateChirpTest(final Integer i, final String nameTest, final String username, final String chirpTitle, final String chirpDescription, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			final Chirp chirp = this.chirpService.create();
			chirp.setTitle(chirpTitle);
			chirp.setDescription(chirpDescription);
			this.chirpService.save(chirp);
			this.chirpService.flush();

			//System.out.println(i + " Create Chirp: " + nameTest + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(i + " Create Chirp: " + nameTest + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Un usuario logado como User tiene que mostrar una lista con los chirps
	 * posteados de todos los usuarios a los que sigue.
	 */
	@Test
	public void listChirps() {

		//System.out.println("-----List chirp test. Positive 0, Negative 1.");

		final Object testingData[][] = {
			//Positives test cases
			{// P1: User list chirps
				"P1", "user1", null
			},

			//Negative test cases
			{// N1: Anonymous tries to list chirps
				"N1", "", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListChirpsTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(Class<?>) testingData[i][2]); //Exception class
	}

	protected void templateListChirpsTest(final Integer i, final String nameTest, final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			final Collection<Chirp> chirps = this.chirpService.findAllByFollowed();
			Assert.notNull(chirps);
			this.chirpService.flush();

			//System.out.println(i + " List Chirps: " + nameTest + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(i + " List Chirps: " + nameTest + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Un usuario logado como Admin tiene que listar los chirps que contienen taboo words.
	 */
	@Test
	public void listTabooChirps() {

		//System.out.println("-----List taboo chirps test. Positive 0, Negative 1.");

		final Object testingData[][] = {
			//Positives test cases
			{// P1: List taboo chirps
				"P1", "admin", null
			},

			//Negative test cases
			{// N1: Anonymous tries list taboo chirps
				"N1", "", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListTabooChirpsTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(Class<?>) testingData[i][2]); //Exception class
	}

	protected void templateListTabooChirpsTest(final Integer i, final String nameTest, final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			final Collection<Chirp> chirps = this.tabooWordService.findAllChirpsWithTabooWord();
			Assert.notNull(chirps);
			this.chirpService.flush();

			//System.out.println(i + " List Taboo Chirps: " + nameTest + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(i + " List Taboo Chirps: " + nameTest + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Un usuario logado como Admin tiene que borrar un chirp que considere inapropiado.
	 */
	@Test
	public void deleteTabooChirps() {

		//System.out.println("-----List taboo chirps test. Positive 0, Negative 1.");

		final Object testingData[][] = {
			//Positives test cases
			{// P1: delete a taboo chirp
				"P1", "admin", null
			},

			//Negative test cases
			{// N1: Anonymous tries delete taboo chirps
				"N1", "", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteTabooChirpTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(Class<?>) testingData[i][2]); //Exception class
	}

	protected void templateDeleteTabooChirpTest(final Integer i, final String nameTest, final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			final Collection<Chirp> tabooChirps = this.tabooWordService.findAllChirpsWithTabooWord();
			Assert.notNull(tabooChirps);
			Chirp chirpDelete = new Chirp();
			for (final Chirp chirp : tabooChirps) {
				chirpDelete = chirp;
				this.chirpService.deleteByAdmin(chirp);
				break;
			}
			final Collection<Chirp> chirps = this.chirpService.findAll();
			Assert.isTrue(!chirps.contains(chirpDelete));
			this.chirpService.flush();

			//System.out.println(i + " Delete Taboo Chirps: " + nameTest + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(i + " Delete Taboo Chirps: " + nameTest + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}
}
