
package services;

import java.util.Collection;
import java.util.LinkedList;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Customer;
import domain.Newspaper;
import domain.VSubscription;
import domain.Volume;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class VolumeServiceTest extends AbstractTest {

	@Autowired
	private VolumeService			volumeService;

	@Autowired
	private VSubscriptionService	vSubscriptionService;

	@Autowired
	private CustomerService			customerService;

	@Autowired
	private UserService				userService;

	@Autowired
	private NewspaperService		newspaperService;


	/*
	 * Requerimientos:
	 * 8. An actor who is not authenticated must be able to:
	 * 1. List the volumes in the system and browse their newspapers
	 * as long as they are authorised (for instance, a private newspaper
	 * cannot be fully displayed to unauthenticated actors).
	 * 
	 * 9. An actor who is authenticated as a customer must be able to:
	 * 1. Subscribe to a volume by providing a credit card. Note that subscribing to
	 * a volume implies subscribing automatically to all
	 * of the newspapers of which it is composed, including newspapers that might be published after
	 * 
	 * 10. An actor who is authenticated as a user must be able to:
	 * 1. Create a volume with as many published newspapers as he or she wishes. Note that the newspapers in a volume
	 * can be added or removed at any time. The same newspaper may be used to create different volumes.
	 */

	/*
	 * Caso de uso:
	 * No auth-> Listar volumes en el sisterma y mostrar sus newspapers
	 * siempre que estén autorizados(privados no)(CU27)
	 * 
	 * Customer-> Subscribirse a un volume proporcionando una credit card válida...(CU36)
	 */

	@Test
	public void listVolumenAndSubscribeTest() {
		final Object testingData[][] = {
			{//Positive case
				"customer1", "volume2", null
			}, { // Negative case: suscribirse sin ser customer
				"user1", "volume2", IllegalArgumentException.class
			}, { // Negative case: suscribirse sin estar logueado
				"", "volume2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templatelistVolumenAndSubscribeTest(i, (String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void templatelistVolumenAndSubscribeTest(final Integer i, final String userName, final String volumenTitle, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {
			super.authenticate(userName);
			final Customer customer = this.customerService.findByPrincipal();
			final Integer volumenId = this.getEntityId(volumenTitle);
			this.volumeService.findAll();
			final VSubscription susc = this.vSubscriptionService.create();
			susc.setCustomer(customer);
			final Volume volumen = this.volumeService.findOne(volumenId);
			susc.setVolume(volumen);

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(i.toString() + oops.getMessage());
		}
		super.checkExceptions(expected, caught);
	}

	/*
	 * Caso de uso:
	 * User-> Crear volume con tantos newspapers publicados como desee...(CU35)
	 */

	@Test
	public void createVolumenWithNewspaperTest() {
		final Object testingData[][] = {
			{//Positive case
				"user1", "newspaper1", "Titulo del volumen", "Descripcion del volumen", "2018", null
			}, { // Negative case: crear volumen sin titulo
				"user1", "newspaper1", "", "Descripcion del volumen", "2018", ConstraintViolationException.class
			}, { // Negative case: crear volemen con titulo null
				"user1", "newspaper1", null, "Descripcion del volumen", "2018", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateVolumenWithNewspaperTest(i, (String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	protected void templateCreateVolumenWithNewspaperTest(final Integer i, final String userName, final String newspaper, final String title, final String description, final String year, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(userName);
			final Integer newspaperId = this.getEntityId(newspaper);
			final Volume volumen = this.volumeService.create();
			final Collection<Newspaper> newspapers = new LinkedList<>();
			newspapers.add(this.newspaperService.findOne(newspaperId));
			volumen.setDescription(description);
			volumen.setNewspapers(newspapers);
			volumen.setTitle(title);
			volumen.setYear(year);
			this.volumeService.save(volumen);

		} catch (final Throwable oops) {
			caught = oops.getClass();
			// System.out.println(i.toString() + oops.getMessage());
		}
		super.checkExceptions(expected, caught);

	}
}
