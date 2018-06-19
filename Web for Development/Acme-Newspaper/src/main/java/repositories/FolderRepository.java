package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Integer> {

	@Query("select f from Actor a join a.folders f where a.id = ?1 and f.name='outbox'")
	Folder getOutBoxFolderFromActorId(int id);

	@Query("select f from Actor a join a.folders f where a.id = ?1 and f.name='inbox'")
	Folder getInBoxFolderFromActorId(int id);

	@Query("select f from Actor a join a.folders f where a.id = ?1 and f.name='spambox'")
	Folder getSpamBoxFolderFromActorId(int id);

	@Query("select f from Actor a join a.folders f where a.id = ?1 and f.name='trashbox'")
	Folder getTrashBoxFolderFromActorId(int id);
	
	@Query("select f from Actor a join a.folders f where a.id = ?1 and f.name='notificationbox'")
	Folder getNotificationBoxFolderFromActorId(int id);
	
	@Query("select f from Actor a join a.folders f where a=(select a from Actor a where a.id=?1) and f.parentFolder=null")
	Collection<Folder> getFirstLevelFoldersFromActorId(int actorId);
	
	
//	@Query("select f from Folder f where f.actor.id=?1 and f.parentFolder=null")
//	Collection<Folder> getFirstLevelFoldersFromActorId(int actorId);

	@Query("select f from Folder f join f.mymessages m where m.id=?1")
	Folder getFolderFromMyMessageId(int mymessageId);
	
	@Query("select f from Folder f where f.parentFolder.id=?1")
	Collection<Folder> getChildFolders(int folderId);
	
	

}
