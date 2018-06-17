package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Actor;
import domain.Administrator;
import domain.Folder;
import domain.MyMessage;
import repositories.MyMessageRepository;

@Service
@Transactional
public class MyMessageService {

	// Validator
	@Autowired
	private Validator validator;

	// Managed repositories ------------------------------------------------
	@Autowired
	private MyMessageRepository myMessageRepository;

	// Supporting services
	@Autowired
	private ActorService actorService;
	@Autowired
	private AdministratorService administratorService;
	@Autowired
	private FolderService folderService;
	



	// CRUD Methods

	// Create

	public MyMessage create() {
		MyMessage res;
		Date moment;
		res = new MyMessage();
		Actor actor = actorService.findByPrincipal();
		moment = new Date(System.currentTimeMillis() - 1);
		res.setSender(actor);
		res.setMoment(moment);
		return res;
	}

	// Save

	public MyMessage save(MyMessage message) {
		// Compruebo que no sea nulo el mensaje que me pasan
		Assert.notNull(message);
		Assert.notNull(message.getRecipient());
		// Inicializo el momento en el que se envía
		Date moment;
		// Inicializo el Folder del destinatario
		Folder recipientFolder;
		// Inicializo el mensaje guardado
		MyMessage saved = null;
		// Si el mensaje que me pasan ya había estado guardado en la base de
		// datos (se quiere cambiar de Folder)
		// Si el mensaje se está guardando en la base de datos por
		// primera vez:
		// instancio el momento en que se envía como el momento actual
		moment = new Date(System.currentTimeMillis() - 1);
		message.setMoment(moment);
		// guardo el mensaje en la base de datos
		saved = myMessageRepository.save(message);

		// Hago una copia del mensaje original, guardo la copia en la base
		// de datos y
		// lo añado a la colección de mensajes del sender
		MyMessage copiedMessage = message;
		moment = new Date(System.currentTimeMillis() - 1);
		message.setMoment(moment);
		MyMessage copiedAndSavedMessage = myMessageRepository.save(copiedMessage);

		// Comprubeo si el mensaje contiene texto marcado como spam
		// si contiene spam
		if (administratorService.checkIsSpam(saved.getSubject(), saved.getBody())) {
			// instancio el Folder del destinatario como el spambox
			recipientFolder = folderService.getSpamBoxFolderFromActorId(saved.getRecipient().getId());
		} else {// si no contiene spam
			// instancio el Folder del destinatario como inbox
			recipientFolder = folderService.getInBoxFolderFromActorId(saved.getRecipient().getId());
		}
		// cojo los mensajes del Folder del destinatario
		Collection<MyMessage> recipientFolderMessages = recipientFolder.getMymessages();
		// Añado el mensaje guardado
		recipientFolderMessages.add(saved);
		// Actualizo el conjunto de mensajes
		recipientFolder.setMymessages(recipientFolderMessages);
		// Cojo el sender
		Actor sender = actorService.findByPrincipal();
		// Cojo el outbox del sender
		Folder senderOutbox = folderService.getOutBoxFolderFromActorId(sender.getId());
		// Cojo los mensajes del outbox del sender
		Collection<MyMessage> senderOutboxMessages = senderOutbox.getMymessages();

		// Añado el mensaje guardado a los mensajes del outbox del sender
		senderOutboxMessages.add(copiedAndSavedMessage);
		// Actualizo los mensajes del outbox del sender
		senderOutbox.setMymessages(senderOutboxMessages);
		// folderService.save(senderOutbox);

		return copiedAndSavedMessage;
	}

	// Save to move
	public void saveToMove(MyMessage message, Folder folder) {

		Assert.notNull(message);
		Assert.notNull(folder);

		Folder currentFolder = folderService.getFolderFromMyMessageId(message.getId());
		Collection<MyMessage> currentFolderMessages = currentFolder.getMymessages();
		currentFolderMessages.remove(message);
		currentFolder.setMymessages(currentFolderMessages);
		folderService.simpleSave(currentFolder);

		// this.messageRepository.delete(message.getId());

		// Message savedCopy = this.messageRepository.save(copy);

		// Message saved = this.messageRepository.save(message);
		Collection<MyMessage> folderMessages = folder.getMymessages();
		folderMessages.add(message);
		folder.setMymessages(folderMessages);
		folderService.simpleSave(folder);

		// this.messageRepository.save(message);

	}

	// Delete
	public void delete(MyMessage message) {
		// Compruebo que el mensaje que me pasan no sea nulo
		Assert.notNull(message);
		// Saco el actor que está logueado
		Actor actor = actorService.findByPrincipal();
		// Compruebo que el mensaje que me pasan sea del actor que está logueado
		// String type = actorService.getType(actor.getUserAccount());
		//
		// if (type.equals("USER")) {
		// actor = (User) actor;
		// } else if (type.equals("AUDITOR")) {
		// actor = (Auditor) actor;
		// } else if (type.equals("RANGER")) {
		// actor = (Ranger) actor;
		// } else if (type.equals("MANAGER")) {
		// actor = (Manager) actor;
		// } else if (type.equals("SPONSOR")) {
		// actor = (Sponsor) actor;
		// }

		checkPrincipal(message, actor);
		// cojo el trashbox del actor logueado
		Folder trashbox = folderService.getTrashBoxFolderFromActorId(actor.getId());
		// Compruebo que el trashbox del actor logueado no sea nulo
		Assert.notNull(trashbox);
		// si el mensaje que me pasan está en el trashbox del actor logueado:
		if (trashbox.getMymessages().contains(message)) {
			// saco la collection de mensajes del trashbox del actor logueado
			Collection<MyMessage> trashboxMessages = trashbox.getMymessages();
			// borro el mensaje que me pasan de la collection de mensajes del
			// trashbox
			 trashboxMessages.remove(message);
			// actualizo la collection de mensajes del trashbox
			trashbox.setMymessages(trashboxMessages);
			// borro el mensaje del sistema
			myMessageRepository.delete(message);

		} else {// si el mensaje que se quiere borrar no está en el trashbox:

			// Borro el mensaje del folder en el que estaba
			Folder messageFolder = folderService.getFolderFromMyMessageId(message.getId());
			Assert.notNull(messageFolder);
			Collection<MyMessage> messages = messageFolder.getMymessages();
			messages.remove(message);
			messageFolder.setMymessages(messages);

			// Meto en el trashbox el mensaje
			Collection<MyMessage> trashboxMessages = trashbox.getMymessages();
			trashboxMessages.add(message);
			trashbox.setMymessages(trashboxMessages);

		}
	}

	// Delete collection
	public void delete(Iterable<MyMessage> messages) {
		Assert.notNull(messages);
		myMessageRepository.delete(messages);
	}

	// FindOne
	public MyMessage findOne(int messageId) {
		 MyMessage message = myMessageRepository.findOne(messageId);
		 Assert.notNull(message);
		 Actor principal;
		 principal = actorService.findByPrincipal();
		checkPrincipal(message,principal);
		return message;

	}
	
	 

	// Other methods

	public void checkPrincipal(MyMessage message, Actor actor) {
		Collection<MyMessage> messages = this.myMessageRepository.messagesFromActorId(actor.getId());
		Assert.isTrue(messages.contains(message));
	}

	public void broadcastMessage(MyMessage message) {
		Assert.notNull(message);
		Actor principal = actorService.findByPrincipal();
		Assert.isTrue(principal instanceof Administrator);
	

		for (Actor recipient : actorService.findAll()) {
			if (!recipient.equals(principal)) {
				Folder notificationbox = folderService.getNotificationBoxFolderFromActorId(recipient.getId());
				MyMessage copy = new MyMessage(message);
				copy.setSubject("[BROADCAST] " + copy.getSubject());
				copy.setBroadcast(true);
				MyMessage saved = this.myMessageRepository.save(copy);
				notificationbox.getMymessages().add(saved);

			} else {

				Folder outbox = folderService.getOutBoxFolderFromActorId(principal.getId());
				MyMessage copy = new MyMessage(message);
				copy.setSubject("[BROADCAST] " + copy.getSubject());
				copy.setBroadcast(true);
				MyMessage saved = this.myMessageRepository.save(copy);
				outbox.getMymessages().add(saved);
			}

		}

	}

	public MyMessage reconstruct(MyMessage m, BindingResult binding) {

		Administrator sender = (Administrator) m.getSender();
		Administrator recipient = sender;
		m.setRecipient(recipient);
		validator.validate(m, binding);

		return m;
	}

}
