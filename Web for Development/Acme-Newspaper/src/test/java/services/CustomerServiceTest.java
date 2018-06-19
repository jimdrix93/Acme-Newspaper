
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import services.CustomerService;
import utilities.AbstractTest;
import domain.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class CustomerServiceTest extends AbstractTest {

	@Autowired
	private CustomerService	customerService;

	/*
	 * Requerimientos:
	 * 19. There's a new kind of actors: customers.
	 */

	/*
	 * Caso de uso:
	 * Usuario anónimo registrandose en el sistema como Customer
	 */
	@Test
	public void createCustomerTest() {
		final Object testingData[][] = {
			{// P1: Anonymous user can create an customer
				"P1", "Name", "Surname", "652956526", "test@gmail.com", "Address Test", "Username1", "Password1", null
			}, {//P2: Anonymous user can create an customer
				"P2", "Name2", "Surname2", "652956526", "test@gmail.com", "Address Test", "Username2", "Password2", null
			},

			{// N1: Anonymous user can create an customer without name
				"N1", null, "Surname Test", "652956526", "email@Test.com", "Address Test", "Username3", "Password3", ConstraintViolationException.class
			}, {// N2: Anonymous user can create an customer without surname
				"N2", "Name2", null, "652956526", "email@Test.com", "Address Test", "Username4", "Password4", DataIntegrityViolationException.class
			}, {// N3: Anonymous user can create an customer without phone
				"N3", "Name2", "Surname Test", null, "email@Test.com", "Address Test", "Username5", "Password5", DataIntegrityViolationException.class
			}, {// N4: Anonymous user can create an customer without email
				"N4", "Name2", "Surname Test", "652956526", "email", "Address Test", "Username6", "Password6", DataIntegrityViolationException.class
			}, {// N5: Anonymous user can create an customer without username
				"N5", "Name2", "Surname Test", "652956526", "email@Test.com", "Address Test", null, "Password7", DataIntegrityViolationException.class
			}, {// N6: Anonymous user can create an customer without username
				"N6", "Name2", "Surname Test", "652956526", "email@Test.com", "Address Test", "Username7", null, DataIntegrityViolationException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateCustomerTest(i, (String) testingData[i][0], //Nº Positive/Negative
				(String) testingData[i][1], //Name
				(String) testingData[i][2], //Surname
				(String) testingData[i][3], //phone
				(String) testingData[i][4], //email
				(String) testingData[i][5], //address
				(String) testingData[i][6], //username
				(String) testingData[i][7], //password
				(Class<?>) testingData[i][8]); //Exception class
	}

	protected void templateCreateCustomerTest(final Integer i, final String nameTest, final String name, final String surname, final String phone, final String email, final String address, final String username, final String password,
		final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {

			final Customer customer = this.customerService.create();

			customer.setAddress(address);
			customer.setEmail(email);
			customer.setName(name);
			customer.setSurname(surname);
			customer.getUserAccount().setUsername(username);
			customer.getUserAccount().setPassword(password);

			this.customerService.save(customer);
			this.customerService.flush();

			//System.out.println(i + " Create Customer: " + nameTest + " ok");

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(i + " Create Customer: " + nameTest + " " + oops.getClass().toString());

		}
		super.checkExceptions(expected, caught);
	}

}
