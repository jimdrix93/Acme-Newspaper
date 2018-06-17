
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

import utilities.AbstractTest;
import domain.Agent;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class AgentServiceTest extends AbstractTest {

	@Autowired
	AgentService	agentService;


	/*
	 * Requerimientos:
	 * 3. An actor who is not authenticated must be able to:
	 * 3.1. Register to the system as an agent.
	 */

	/*
	 * Caso de uso:
	 * No auth-> Registro agent(CU26)
	 */
	@Test
	public void createAgentTest() {

		final Object testingData[][] = {
			{// Positive
				"Pedro", "Dominguez Lopez", "652956526", "test@gmail.com", "Address Test", "Username1", "Password1", null
			}, {//Positive
				"Amaia", "Fernandez Rodriguezáéíóú", "652956526", "test@gmail.com", "Address Test", "Username2", "Password2", null
			}, {// Negative: without name
				"Name", "Surname Test", "652956526", "email", "Address Test", "Username3", "Password3", ConstraintViolationException.class
			}, {// Negative: with name null
				"", "Surname Test", "652956526", "emailTest@email.com", "Address Test", "Username4", "Password4", DataIntegrityViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAgentTest(i, (String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}

	protected void templateCreateAgentTest(final Integer i, final String name, final String surname, final String phone, final String email, final String address, final String username, final String password, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			final Agent agent = this.agentService.create();

			agent.setName(name);
			agent.setAddress(address);
			agent.setEmail(email);
			agent.setPhone(phone);
			agent.setSurname(surname);
			agent.getUserAccount().setPassword(password);
			agent.getUserAccount().setUsername(username);

			this.agentService.save(agent);
			this.agentService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

}
