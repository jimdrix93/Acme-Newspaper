
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.FolderRepository;
import domain.Actor;
import domain.Folder;
import domain.MyMessage;

@Service
@Transactional
public class FolderService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private FolderRepository	folderRepository;

	// Supporting services
	@Autowired
	private ActorService		actorService;

	@Autowired
	private MyMessageService	myMessageService;


	// CRUD Methods

	// Create
	public Folder create() {

		final Folder folder = new Folder();
		folder.setSystemFolder(false);
		folder.setMymessages(new ArrayList<MyMessage>());
		return folder;
	}

	// Save
	public Folder save(final Folder folder) {
		Assert.notNull(folder);
		if (folder.getId() != 0)
			this.checkPrincipal(folder);
		Actor actor;
		actor = this.actorService.findByPrincipal();
		final Folder folderSaved = this.folderRepository.save(folder);
		this.flush();

		if (folder.getId() == 0) {
			final Collection<Folder> actorFolders = actor.getFolders();
			actorFolders.add(folderSaved);
			actor.setFolders(actorFolders);
			this.actorService.save(actor);
		}

		return folderSaved;
		// actorService.save(actor);

	}

	//Simple Save
	public Folder simpleSave(final Folder f) {
		Assert.notNull(f);

		final Folder folderSaved = this.folderRepository.save(f);

		return folderSaved;

	}
	//Save to move
	public void saveToMove(final Folder folder, final Folder targetFolder) {

		Assert.notNull(targetFolder);
		Assert.notNull(folder);
		folder.setParentFolder(targetFolder);
		//this.folderRepository.save(folder);

	}

	//FindOne
	public Folder findOne(final int folderId) {
		Assert.isTrue(folderId != 0);
		Folder folder;
		folder = this.folderRepository.findOne(folderId);
		Assert.notNull(folder);
		this.checkPrincipal(folder);
		return folder;
	}

	//FindOne to edit
	public Folder findOneToEdit(final int folderId) {
		Assert.isTrue(folderId != 0);
		Folder folder;
		folder = this.folderRepository.findOne(folderId);
		Assert.notNull(folder);
		Assert.isTrue(!folder.getSystemFolder());
		this.checkPrincipal(folder);
		return folder;
	}

	//Delete
	public void delete(final Folder folder) {
		Assert.notNull(folder);
		Assert.isTrue(!folder.getSystemFolder());
		this.checkPrincipal(folder);
		final Collection<MyMessage> messages = folder.getMymessages();
		final Actor a = this.actorService.findByPrincipal();
		a.getFolders().remove(folder);
		this.actorService.save(a);

		final Collection<Folder> childFolders = this.folderRepository.getChildFolders(folder.getId());

		if (childFolders.size() > 0)
			for (final Folder child : childFolders)
				this.delete(child);

		this.folderRepository.delete(folder);
		this.myMessageService.delete(messages);

	}

	// Other methods
	public Folder getOutBoxFolderFromActorId(final int id) {
		return this.folderRepository.getOutBoxFolderFromActorId(id);
	}

	public Folder getInBoxFolderFromActorId(final int id) {
		return this.folderRepository.getInBoxFolderFromActorId(id);
	}

	public Folder getSpamBoxFolderFromActorId(final int id) {
		return this.folderRepository.getSpamBoxFolderFromActorId(id);
	}

	public Folder getTrashBoxFolderFromActorId(final int id) {
		return this.folderRepository.getTrashBoxFolderFromActorId(id);
	}

	public Folder getNotificationBoxFolderFromActorId(final int id) {
		return this.folderRepository.getNotificationBoxFolderFromActorId(id);
	}

	public Collection<Folder> getFirstLevelFoldersFromActorId(final int actorId) {
		return this.folderRepository.getFirstLevelFoldersFromActorId(actorId);
	}

	public Folder getFolderFromMyMessageId(final int mymessageId) {
		return this.folderRepository.getFolderFromMyMessageId(mymessageId);
	}

	public Collection<Folder> getChildFolders(final int folderId) {
		return this.folderRepository.getChildFolders(folderId);
	}

	public void checkPrincipal(final Folder folder) {
		Actor actor;
		actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor.getFolders().contains(folder));
	}

	public void createSystemFolders(final Actor actor) {

		Folder inbox;
		Folder outbox;
		Folder trashbox;
		Folder notificationbox;
		Folder spambox;
		final Collection<Folder> folders = new ArrayList<Folder>();

		inbox = new Folder();
		inbox.setSystemFolder(true);
		inbox.setName("inbox");
		inbox.setMymessages(new ArrayList<MyMessage>());
		// folders.add(inbox);
		// folderRepository.save(inbox);

		outbox = new Folder();
		outbox.setSystemFolder(true);
		outbox.setName("outbox");
		outbox.setMymessages(new ArrayList<MyMessage>());
		// folders.add(outbox);
		// folderRepository.save(outbox);

		trashbox = new Folder();
		trashbox.setSystemFolder(true);
		trashbox.setName("trashbox");
		trashbox.setMymessages(new ArrayList<MyMessage>());
		// folders.add(trashbox);
		// folderRepository.save(trashbox);

		notificationbox = new Folder();
		notificationbox.setSystemFolder(true);
		notificationbox.setName("notificationbox");
		notificationbox.setMymessages(new ArrayList<MyMessage>());
		// folders.add(notificationbox);
		// folderRepository.save(notificationbox);

		spambox = new Folder();
		spambox.setSystemFolder(true);
		spambox.setName("spambox");
		spambox.setMymessages(new ArrayList<MyMessage>());
		// folders.add(spambox);
		// folderRepository.save(spambox);

		// folderRepository.save(folders);
		final Folder savedinbox = this.folderRepository.save(inbox);
		final Folder savedoutbox = this.folderRepository.save(outbox);
		final Folder savedtrashbox = this.folderRepository.save(trashbox);
		final Folder savednotificationbox = this.folderRepository.save(notificationbox);
		final Folder savedspambox = this.folderRepository.save(spambox);

		folders.add(savedinbox);
		folders.add(savedoutbox);
		folders.add(savedtrashbox);
		folders.add(savednotificationbox);
		folders.add(savedspambox);

		actor.setFolders(folders);

		// folderRepository.save(folders);

		// TODO probar si es necesario hacer esto:
		// managerService.save((Manager)actor);

	}

	public void flush() {
		this.folderRepository.flush();

	}

}
