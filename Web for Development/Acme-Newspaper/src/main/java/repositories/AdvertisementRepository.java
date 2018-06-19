
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Advertisement;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {

	// N2.0 C3 The ratio of advertisements that have taboo words.
	@Query(value = "select count(*)/(select count(*) from Advertisement) \r\n" + "	from (select *, ata.title regexp \r\n" + "	(select group_concat(t.`text` SEPARATOR '$|') from TabooWord t) as taboo\r\n" + "	from Advertisement ata) as cuenta\r\n"
		+ "	where cuenta.taboo = 1;", nativeQuery = true)
	public Double ratioAdvertisementsWithTabooWords();

	@Query("select a from Advertisement a where a.newspaper.id = ?1")
	public Collection<Advertisement> findByNewspaperId(int newspaperId);
	
	@Query("select a from Advertisement a where a.agent.id = ?1")
	public Collection<Advertisement> findAllByAgent(int agentId);

}
