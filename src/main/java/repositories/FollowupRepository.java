
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Followup;

@Repository
public interface FollowupRepository extends JpaRepository<Followup, Integer> {

	// The average number of follow-ups per article
	@Query("select avg(a.followups.size) from Article a")
	Double avgFolloupsPerArticle();

	@Query("select a.followups from Article a where a.id=?1")
	Collection<Followup> findAllByArticle(int articleId);

	// TODO
	// The average number of follow-ups per article up to one week after the
	// corresponding
	// newspaper’s been published

	// TODO
	// The average number of follow-ups per article up to two weeks after the
	// corresponding
	// newspaper’s been published

}
