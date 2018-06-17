
package services;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Folder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class FolderServiceTest extends AbstractTest {

	// SUT--------------------------------------------------
	@Autowired
	private FolderService	folderService;
	// Services
	@Autowired
	private ActorService	actorService;


	/*
	 * 13. An actor who is authenticated must be able to: 2. Manage his or her
	 * message folders, except for the system folders.
	 */

	/* Caso de uso: actor autenticado creando una nueva carpeta */

	@Test
	public void authenticatedCreatingFolder() {

		final Object testingData[][] = {
			// Positive:
			// Creating a folder
			{
				"user1", "Name1", "notificationbox1", null
			},
			// Negative:
			// Creating a folder with blank name
			{
				"user1", "", "notificationbox1", ConstraintViolationException.class
			},
			// Negative:
			// Creating a folder without authentication
			{
				"", "Name1", "notificationbox1", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAuthenticatedCreatingFolder((String) testingData[i][0], (String) testingData[i][1], super.getEntityId((String) testingData[i][2]), (Class<?>) testingData[i][3]);

	}

	private void templateAuthenticatedCreatingFolder(final String username, final String folderName, final int parentfolderId, final Class<?> expected) {

		Class<?> caught;
		caught = null;

		try {
			this.authenticate(username);
			final Folder folder = this.folderService.create();
			folder.setName(folderName);
			final Folder parentFolder = this.folderService.findOne(parentfolderId);
			folder.setParentFolder(parentFolder);
			this.folderService.save(folder);
			this.folderService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);

	}

	/*
	 * 13. An actor who is authenticated must be able to: 2. Manage his or her
	 * message folders, except for the system folders.
	 */

	/*
	 * Caso de uso: actor autenticado editando una carpeta (cambiando el nombre de
	 * la carpeta)
	 */

	@Test
	public void authenticatedEditingFolder() {

		final Object testingData[][] = {
			// Positive:
			// Editing a folder
			{
				"user1", "Name2", null
			},
			// Negative:
			// Editing a folder with blank name
			{
				"user1", "", ConstraintViolationException.class
			},
			// Negative:
			// Editing folder without authentication
			{
				"", "Name2", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAuthenticatedEditingFolder((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	private void templateAuthenticatedEditingFolder(final String username, final String folderName, final Class<?> expected) {

		Class<?> caught;
		caught = null;

		try {
			this.authenticate(username);

			final Folder folder = this.folderService.create();
			folder.setName("Name1");
			folder.setParentFolder(null);
			final Folder saved = this.folderService.save(folder);
			this.folderService.flush();
			final Folder toEdit = this.folderService.findOneToEdit(saved.getId());

			toEdit.setName(folderName);
			this.folderService.save(toEdit);
			this.folderService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);

	}

	/*
	 * 13. An actor who is authenticated must be able to: 2. Manage his or her
	 * message folders, except for the system folders.
	 */

	/*
	 * Caso de uso: actor autenticado moviendo una carpeta a otra carpeta
	 */

	@Test
	public void authenticatedMovingFolder() {

		final Object testingData[][] = {
			// Positive:
			// Moving a folder
			{
				"user1", "notificationbox1", null
			},
			// Negative:
			// Moving a folder to other user´s folder
			{
				"user1", "notificationbox5", IllegalArgumentException.class
			},
			// Negative:
			// Moving a folder without authentication
			{
				"", "notificationbox1", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAuthenticatedMovingFolder((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);

	}

	private void templateAuthenticatedMovingFolder(final String username, final int notificationboxId, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			Folder notificationbox;

			// Create
			this.authenticate(username);
			final Folder folder = this.folderService.create();
			folder.setName("Name1");
			folder.setParentFolder(null);
			final Folder saved = this.folderService.save(folder);
			this.folderService.flush();
			// Move
			notificationbox = this.folderService.findOne(notificationboxId);
			this.folderService.saveToMove(saved, notificationbox);

		} catch (final Throwable oops) {

			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);

	}

	/*
	 * 13. An actor who is authenticated must be able to: 2. Manage his or her
	 * message folders, except for the system folders.
	 */
	/* Caso de uso: Usuario autenticado borrando una carpeta */

	@Test
	public void authenticatedDeletingFolder() {

		final Object testingData[][] = {
			// Positive:
			// Deleting a folder
			{
				"user1", "", null
			},
			// Negative:
			// Deleting a folder of other user
			{
				"user1", "customer1", IllegalArgumentException.class
			},
			// Negative:
			// Deleting a folder without authentication
			{
				"", "", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAuthenticatedDeletingFolder((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	protected void templateAuthenticatedDeletingFolder(final String username1, final String username2, final Class<?> expected) {
		Class<?> caught;
		caught = null;

		try {

			Folder saved;

			if (!username2.isEmpty()) {

				// Create
				this.authenticate(username2);
				final Folder folder = this.folderService.create();
				folder.setName("Name1");
				folder.setParentFolder(null);
				saved = this.folderService.save(folder);
				this.folderService.flush();
				this.unauthenticate();

			} else {

				// Create
				this.authenticate(username1);
				final Folder folder = this.folderService.create();
				folder.setName("Name1");
				folder.setParentFolder(null);
				saved = this.folderService.save(folder);
				this.folderService.flush();
				this.unauthenticate();

			}

			// Delete
			this.authenticate(username1);
			this.folderService.delete(saved);
			this.unauthenticate();

		} catch (final Throwable oops) {

			caught = oops.getClass();

		}

		this.checkExceptions(expected, caught);

	}

}
