
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import services.CreditCardService;
import services.NewspaperService;
import services.SubscriptionService;
import utilities.AbstractTest;
import domain.CreditCard;
import domain.Newspaper;
import domain.Subscription;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class SubscriptionServiceTest extends AbstractTest {

	@Autowired
	private SubscriptionService	subscriptionService;
	@Autowired
	private NewspaperService	newspaperService;
	@Autowired
	private CreditCardService	creditCardService;

	
	/*
	 * Requerimientos:
	 * 22. An actor who is authenticated as a customer can:
	 * 		1. Subscribe to a private newspaper by providing a valid credit card.
	 */

	/*
	 * Un usuario autenticado como Customer puede suscribirse a un periodico privado.
	 */
	@Test
	public void suscribeToNewspaper() {

		//System.out.println("-----Edit tabooWord test. Positive 0-1, Negative 2-5.");

		final Object testingData[][] = {
			//Positives test cases
			{// P1: Customer suscribe to private newspaper
				"P1", "customer1", "newspaper3", "customer1Name", "VISA", "4485262346709266", 8, 21, 852, null
			}, {// P2: Customer suscribe to private newspaper
				"P2", "customer2", "newspaper3", "customer2Name", "VISA", "4716961053525705", 10, 20, 369, null
			},

			//Negative test cases
			{// N1: User tries to suscribe to private newspaper
				"N1", "user1", "newspaper3", "customer2Name", "VISA", "4532523936882000", 1, 21, 654, IllegalArgumentException.class
			}, {// N2: Anonymous tries to suscribe to private newspaper
				"N2", "", "newspaper3", "customer2Name", "VISA", "4485958410879501", 2, 21, 951, IllegalArgumentException.class
			}, {// N3: Customer tries to suscribe to private newspaper with invalid credit card number
				"N3", "customer2", "newspaper3", "customer2Name", "VISA", "123456789", 11, 21, 753, IllegalArgumentException.class
			}, {// N4: Customer tries to suscribe to private newspaper without brandName
				"N4", "customer2", "newspaper3", "customer2Name", null, "123456789", 5, 21, 123, IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditTabooWordTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Username login
				(String) testingData[i][2], //newspaper id
				(String) testingData[i][3], //holder name
				(String) testingData[i][4], //brand name
				(String) testingData[i][5], //CreditCard number
				(Integer) testingData[i][6], //monthExpiration
				(Integer) testingData[i][7], //yearExpiration
				(Integer) testingData[i][8], //CVVCode
				(Class<?>) testingData[i][9]); //Exception class
	}
	protected void templateEditTabooWordTest(final Integer i, final String nameTest, final String username, final String newspaperId, final String holderName, final String brandName, final String number, final Integer expirationMonth,
		final Integer expirationYear, final Integer cvv, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			final Collection<Newspaper> newspapersPrivates = this.newspaperService.findAllPrivate();
			final Newspaper newspaper = this.newspaperService.findOne(super.getEntityId(newspaperId));
			Assert.isTrue(newspapersPrivates.contains(newspaper));

			final CreditCard creditCard = this.creditCardService.create();
			creditCard.setBrandName(brandName);
			creditCard.setHolderName(holderName);
			creditCard.setNumber(number);
			creditCard.setExpirationMonth(expirationMonth);
			creditCard.setExpirationYear(expirationYear);
			creditCard.setCvv(cvv);

			final Subscription subscription = this.subscriptionService.create();
			subscription.setNewspaper(newspaper);
			subscription.setCreditCard(creditCard);

			this.subscriptionService.save(subscription);
			this.subscriptionService.flush();

			//System.out.println(i + " Create Subscription: " + nameTest + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(i + " Create Subscription: " + nameTest + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}
}
