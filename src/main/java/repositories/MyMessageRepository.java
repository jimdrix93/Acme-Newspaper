package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.MyMessage;

@Repository
public interface MyMessageRepository extends JpaRepository<MyMessage, Integer> {

	@Query("select m from Actor a join a.folders f join f.mymessages m where a.id=?1")
	Collection<MyMessage> messagesFromActorId(int actorId);

}


