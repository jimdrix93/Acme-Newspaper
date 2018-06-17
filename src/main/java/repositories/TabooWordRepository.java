
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.TabooWord;

@Repository
public interface TabooWordRepository extends JpaRepository<TabooWord, Integer> {

	@Query("select t from TabooWord t where ?1 like concat('%',t.text,'%') or ?2 like concat('%',t.text,'%')")
	Collection<TabooWord> getTabooWordFromMyMessageSubjectAndBody(String subject, String body);

	//	@Query("select t from Advertisement t where t.title like %?1%")
	//	Collection<Advertisement> findAllAdvertisementWithTabooWord(String text);

	@Query("select t.text from TabooWord t")
	Collection<String> findAllTabooWord();

	@Query(value = "" +
	" select * " +
	" from Newspaper n" +
	" where " +
	"      n.title regexp (select group_concat(t.`text` SEPARATOR '|') from TabooWord t) = 1 "+
	"   OR n.description regexp (select group_concat(t.`text` SEPARATOR '|') from TabooWord t) = 1 " +
	"   OR n.picture regexp (select group_concat(t.`text` SEPARATOR '|') from TabooWord t) = 1 "  , nativeQuery = true)
	Collection<Object[]> findAllNewspaperWithTabooWord();
	
	@Query(value = "" +
	" select * " +
	" from Advertisement n" +
	" where " +
	"      n.title regexp (select group_concat(t.`text` SEPARATOR '|') from TabooWord t) = 1 "+
	"   OR n.bannerURL regexp (select group_concat(t.`text` SEPARATOR '|') from TabooWord t) = 1 " +
	"   OR n.targetURL regexp (select group_concat(t.`text` SEPARATOR '|') from TabooWord t) = 1 " , nativeQuery = true)
	Collection<Object[]> findAllAdvertisementWithTabooWord();
	
	@Query(value = "" +
	" select * " +
	" from Article n" +
	" where " +
	"      n.title regexp (select group_concat(t.`text` SEPARATOR '|') from TabooWord t) = 1 "+
	"   OR n.summary regexp (select group_concat(t.`text` SEPARATOR '|') from TabooWord t) = 1 " +
	"   OR n.body regexp (select group_concat(t.`text` SEPARATOR '|') from TabooWord t) = 1 " , nativeQuery = true)
	Collection<Object[]> findAllArticleWithTabooWord();
	
	@Query(value = "" +
	" select * " +
	" from Chirp n" +
	" where " +
	"      n.title regexp (select group_concat(t.`text` SEPARATOR '|') from TabooWord t) = 1 "+
	"   OR n.description regexp (select group_concat(t.`text` SEPARATOR '|') from TabooWord t) = 1 " , nativeQuery = true)
	Collection<Object[]> findAllChirpWithTabooWord();
}
