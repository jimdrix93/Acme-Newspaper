package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import domain.Folder;
import domain.MyMessage;
import domain.User;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class MyMessageServiceTest extends AbstractTest {

	// SUT-----------------------------------------------------
	@Autowired
	private MyMessageService myMessageService;

	// Services
	@Autowired
	private UserService userService;
	@Autowired
	private FolderService folderService;
	

	/*
	 * 13. An actor who is authenticated must be able to: 1. Exchange messages with
	 * other actors
	 */
	/* Caso de uso: Usuario autenticado enviando un mensaje a otro usuario */

	@Test
	public void authenticatedSendingMessage() {

		Object testingData[][] = {
				// Positive:
				// Sending a message
				{ "user1", "user2", "HIGH", "Subject1", "Body1", null },
				// Negative:
				// Sending a message with empty subject
				{ "user1", "user2", "LOW", "", "Body1", ConstraintViolationException.class },
				// Negative:
				// Sending a message without authentication
				{ "", "user2", "LOW", "Subject1", "Body1", IllegalArgumentException.class }

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAuthenticatedSendingMessage((String) testingData[i][0],
					(int) super.getEntityId((String) testingData[i][1]), (String) testingData[i][2],
					(String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	protected void templateAuthenticatedSendingMessage(String username, int recipientId, String priority,
			String subject, String body, Class<?> expected) {

		Class<?> caught;
		caught = null;

		try {

			authenticate(username);
			MyMessage message = myMessageService.create();
			User recipient = userService.findOne(recipientId);
			message.setPriority(priority);
			message.setSubject(subject);
			message.setBody(body);
			message.setRecipient(recipient);
			myMessageService.save(message);
			unauthenticate();

		} catch (Throwable oops) {

			caught = oops.getClass();

		}

		checkExceptions(expected, caught);

	}

	/*
	 * 13. An actor who is authenticated must be able to: 1..............which
	 * includes deleting and.......
	 */
	/* Caso de uso: Usuario autenticado borrando un mensaje */

	@Test
	public void authenticatedDeletingMessage() {

		Object testingData[][] = {
				// Positive:
				// Deleting a message
				{ "user1", "", null },
				// Negative:
				// Deleting a message of other user
				{ "user1", "customer1", IllegalArgumentException.class },
				// Negative:
				// Deleting a message without authentication
				{ "", "", IllegalArgumentException.class }

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAuthenticatedDeletingMessage((String) testingData[i][0], (String) testingData[i][1],
					(Class<?>) testingData[i][2]);

	}

	protected void templateAuthenticatedDeletingMessage(String username1, String username2, Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			MyMessage saved;

			if (!username2.isEmpty()) {

				// Create
				authenticate(username2);
				MyMessage message = myMessageService.create();
				User recipient = userService.findOne(super.getEntityId("user2"));
				message.setPriority("HIGH");
				message.setSubject("Subject1");
				message.setBody("Body1");
				message.setRecipient(recipient);
				saved = myMessageService.save(message);
				unauthenticate();

			} else {

				// Create
				authenticate(username1);
				MyMessage message = myMessageService.create();
				User recipient = userService.findOne(super.getEntityId("user2"));
				message.setPriority("HIGH");
				message.setSubject("Subject1");
				message.setBody("Body1");
				message.setRecipient(recipient);
				saved = myMessageService.save(message);
				unauthenticate();

			}

			// Delete
			authenticate(username1);
			myMessageService.delete(saved);
			unauthenticate();

		} catch (Throwable oops) {

			caught = oops.getClass();

		}

		checkExceptions(expected, caught);

	}

	/*
	 * 13. An actor who is authenticated must be able to: 1. ....... and moving them
	 * from one folder to another folder.
	 */
	/* Caso de uso: actor autenticado moviendo un mensaje de una carpeta a otra */

	@Test
	public void authenticatedMovingMessage() {

		Object testingData[][] = {
				// Positive:
				// Moving a message
				{ "user1", "notificationbox1", null },
				// Negative:
				// Moving a message to other user´s folder
				{ "user1", "notificationbox5", IllegalArgumentException.class },
				// Negative:
				// Moving a message without authentication
				{ "", "notificationbox1", IllegalArgumentException.class }

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAuthenticatedMovingMessage((String) testingData[i][0], (int) super.getEntityId((String) testingData[i][1]),
					(Class<?>) testingData[i][2]);

	}

	private void templateAuthenticatedMovingMessage(String username1, int notificationboxId, Class<?> expected) {

		Class<?> caught;
		caught = null;

		try {

			MyMessage saved;
			Folder notificationbox;

			// Create
			authenticate(username1);
			MyMessage message = myMessageService.create();
			User recipient = userService.findOne(super.getEntityId("user2"));
			message.setPriority("HIGH");
			message.setSubject("Subject1");
			message.setBody("Body1");
			message.setRecipient(recipient);
			saved = myMessageService.save(message);
			//Move
			notificationbox = folderService.findOne(notificationboxId);
			myMessageService.saveToMove(saved, notificationbox);

			

		} catch (Throwable oops) {

			caught = oops.getClass();

		}

		checkExceptions(expected, caught);

	}

}
