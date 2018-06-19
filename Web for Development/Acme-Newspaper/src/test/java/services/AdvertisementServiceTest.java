
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

import utilities.AbstractTest;
import domain.Advertisement;
import domain.Agent;
import domain.CreditCard;
import domain.Newspaper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class AdvertisementServiceTest extends AbstractTest {

	@Autowired
	private AdvertisementService	advertisementService;
	@Autowired
	private NewspaperService		newspaperService;
	@Autowired
	private TabooWordService		tabooWordService;
	@Autowired
	private AgentService			agentService;


	/*
	 * Requerimientos:
	 * 4. An actor who is authenticated as an agent must be able to:
	 * 4.2. Register an advertisement and place it in a newspaper.
	 * 
	 * 5. An actor who is authenticated as an administrator must be able to:
	 * 5.1. List the advertisements that contain taboo words in its title.
	 */

	/*
	 * Caso de uso:
	 * Agent-> Registrar advertisement y ponerlo en un newspaper(CU37)
	 */

	@Test
	public void agentCreateAdvertisementWithNewspaper() {

		//	System.out.println("-----createUserTest. Positive 0 to 1, Negative 0 to 2");

		final Object testingData[][] = {
			{	// Positive1
				"agent1", "newspaper1", "Xbox", "http://img1.meristation.as.com/files/imagenes/general/xbox-one-logo-banner.jpg", "http://www.xbox.es", "Agent1", "VISA", "4929476473937904", 10, 20, 123, null
			}, {// Positive2
				"agent2", "newspaper3", "Coca Cola", "https://naldzgraphics.net/wp-content/uploads/2011/10/coca-cola.ads.jpg", "http://www.cocacola.es", "Agent2", "VISA", "4265091582021003", 11, 21, 159, null
			}, {// Negative1: Advertisement Name is Null
				"agent2", "newspaper3", "", "https://naldzgraphics.net/wp-content/uploads/2011/10/coca-cola.ads.jpg", "http://www.cocacola.es", "Agent2", "VISA", "4265091582021003", 11, 21, 159, ConstraintViolationException.class
			}, {// Negative2: Advertisement BannerUrl is Null
				"agent2", "newspaper3", "Coca Cola", "", "http://www.cocacola.es", "Agent2", "VISA", "4265091582021003", 11, 21, 159, ConstraintViolationException.class
			}, {// Negative3: Advertisement TargetUrl is Null
				"agent2", "newspaper3", "Coca Cola", "https://naldzgraphics.net/wp-content/uploads/2011/10/coca-cola.ads.jpg", "", "Agent2", "VISA", "4265091582021003", 11, 21, 159, ConstraintViolationException.class
			}, {// Negative4: Advertisement TargetUrl is not Url
				"agent2", "newspaper3", "Coca Cola", "https://naldzgraphics.net/wp-content/uploads/2011/10/coca-cola.ads.jpg", "coca cola", "Agent2", "VISA", "4265091582021003", 11, 21, 159, ConstraintViolationException.class
			}, {// Negative5: Advertisement BannerUrl is not Url
				"agent2", "newspaper3", "Coca Cola", "banner", "http://www.cocacola.es", "Agent2", "VISA", "4265091582021003", 11, 21, 159, ConstraintViolationException.class
			}, {// Negative6: CreditCard HolderName is Null
				"agent1", "newspaper1", "Xbox", "http://img1.meristation.as.com/files/imagenes/general/xbox-one-logo-banner.jpg", "http://www.xbox.es", "", "VISA", "4929476473937904", 10, 20, 123, ConstraintViolationException.class
			}, {// Negative7: CreditCard BrandName is Null
				"agent1", "newspaper1", "Xbox", "http://img1.meristation.as.com/files/imagenes/general/xbox-one-logo-banner.jpg", "http://www.xbox.es", "Agent1", "", "4929476473937904", 10, 20, 123, ConstraintViolationException.class
			}, {// Negative8: CreditCard Number is Null
				"agent1", "newspaper1", "Xbox", "http://img1.meristation.as.com/files/imagenes/general/xbox-one-logo-banner.jpg", "http://www.xbox.es", "Agent1", "VISA", "", 10, 20, 123, ConstraintViolationException.class
			}, {// Negative9: CreditCard Number is not Valid
				"agent1", "newspaper1", "Xbox", "http://img1.meristation.as.com/files/imagenes/general/xbox-one-logo-banner.jpg", "http://www.xbox.es", "Agent1", "VISA", "123456", 10, 20, 123, ConstraintViolationException.class
			}, {// Negative10: CreditCard expirationYear is not Valid
				"agent1", "newspaper1", "Xbox", "http://img1.meristation.as.com/files/imagenes/general/xbox-one-logo-banner.jpg", "http://www.xbox.es", "Agent1", "VISA", "4929476473937904", 10, 17, 123, ConstraintViolationException.class
			}, {// Negative10: CreditCard cvv is not Valid
				"agent1", "newspaper1", "Xbox", "http://img1.meristation.as.com/files/imagenes/general/xbox-one-logo-banner.jpg", "http://www.xbox.es", "Agent1", "VISA", "4929476473937904", 10, 18, null, ConstraintViolationException.class
			}, {// Negative11: Actor is not an Agent
				"user1", "newspaper1", "Xbox", "http://img1.meristation.as.com/files/imagenes/general/xbox-one-logo-banner.jpg", "http://www.xbox.es", "Agent1", "VISA", "4929476473937904", 10, 18, 159, IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAgentCreateAdvertisementWithNewspaper(i, (String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5],
				(String) testingData[i][6], (String) testingData[i][7], (Integer) testingData[i][8], (Integer) testingData[i][9], (Integer) testingData[i][10], (Class<?>) testingData[i][11]);
	}

	protected void templateAgentCreateAdvertisementWithNewspaper(final Integer i, final String userName, final String newspaperName, final String title, final String bannerURL, final String targetURL, final String holderName, final String brandName,
		final String number, final Integer expirationMonth, final Integer expirationYear, final Integer cvv, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			super.authenticate(userName);
			final Agent agent = this.agentService.findByPrincipal();

			final Advertisement advertisement = this.advertisementService.create();
			final CreditCard creditCard = new CreditCard();
			creditCard.setBrandName(brandName);
			creditCard.setHolderName(holderName);
			creditCard.setNumber(number);
			creditCard.setExpirationMonth(expirationMonth);
			creditCard.setExpirationYear(expirationYear);
			creditCard.setCvv(cvv);

			advertisement.setTitle(title);
			advertisement.setBannerURL(bannerURL);
			advertisement.setTargetURL(targetURL);
			advertisement.setCreditCard(creditCard);

			final Integer newspaperId = this.getEntityId(newspaperName);
			final Newspaper newspaper = this.newspaperService.findOne(newspaperId);

			advertisement.setNewspaper(newspaper);

			final Advertisement saved = this.advertisementService.save(advertisement);

			this.advertisementService.flush();
			final int testId = saved.getId();
			Assert.isTrue(this.advertisementService.findOne(testId) != null);
			Assert.isTrue(saved.getAgent().equals(agent));

			//	System.out.println(i.toString() + " ok ");
			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//	System.out.println(i.toString() + oops.getMessage());
		}
		super.checkExceptions(expected, caught);
	}
	/*
	 * Caso de uso:
	 * 5.1. Admin-> Listar advertisements que tengan taboo words en su title(CU40)
	 * 5.2. Admin-> Borrar advertisement que considere inapropiado(CU41)
	 */

	@Test
	public void administratorListAdvertisementWithTabooWordsAndDeleteOne() {

		final Object testingData[][] = {
			{//Positive case
				"Admin", null
			}, { // Negative case: logueado como user
				"user1", ClassCastException.class
			}, { // Negative case: logueado como customer
				"customer1", ClassCastException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAdministratorListAdvertisementWithTabooWordsAndDeleteOne(i, (String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	protected void templateAdministratorListAdvertisementWithTabooWordsAndDeleteOne(final Integer i, final String name, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {
			super.authenticate(name);
			final Collection<Advertisement> lista = this.tabooWordService.findAllAdvertisementWithTabooWord();
			Assert.notNull(lista);
			for (final Advertisement a : lista) {
				this.advertisementService.delete(a.getId());
				break;
			}

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//	System.out.println(i.toString() + oops.getMessage());
		}
		super.checkExceptions(expected, caught);
	}

}
