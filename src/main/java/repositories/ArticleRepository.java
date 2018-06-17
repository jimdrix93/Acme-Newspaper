
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Article;
import domain.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

	@Query("select n.articles from Newspaper n where n.id = ?1")
	Collection<Article> findAllByNewspaper(int newspaperId);

	@Query("select a from Article a where a.newspaper.id = ?1")
	Collection<Article> findAllByNewspaperId(int newspaperId);

	@Query("select u.articles from User u where u.id=?1")
	Collection<Article> findAllByUser(int userId);

	@Query("select a from domain.Article a where (a.title like %?1% or a.summary like %?1% or a.body like %?1%) and a.newspaper.isPublicated = true")
	Collection<Article> findArticleByKeyword(String word);

	@Query("select a from User u join u.articles a where u=?1 and a in (select a1 from Newspaper n join n.articles a1 where n.isPublicated = true)")
	Collection<Article> findArticlesPublishedInNewspapersByUser(User user);

	@Query(value = "" + "select avg(cuenta.resultado) " + "from ( " + "	select article.id, " + "		sum(case when followup.publicationMoment <= date_add(article.publicationMoment, INTERVAL 1 WEEK) "
		+ "			and followup.publicationMoment >= article.publicationMoment then 1 else 0 end) as resultado " + "	from article " + "		left join article_followup on article.id = article_followup.article_id "
		+ "		left join followup on article_followup.followups_id = followup.id " + "	group by article.id  " + ") as cuenta", nativeQuery = true)
	Double findAvgFollowupPerArticle1Week();

	@Query(value = "" + "select avg(cuenta.resultado) " + "from ( " + "	select article.id, " + "		sum(case when followup.publicationMoment <= date_add(article.publicationMoment, INTERVAL 2 WEEK) "
		+ "			and followup.publicationMoment >= article.publicationMoment then 1 else 0 end) as resultado " + "	from article " + "		left join article_followup on article.id = article_followup.article_id "
		+ "		left join followup on article_followup.followups_id = followup.id " + "	group by article.id  " + ") as cuenta", nativeQuery = true)
	Double findAvgFollowupPerArticle2Week();

	@Query("select a from Article a where a.newspaper.isPublicated = true")
	Collection<Article> findAllPublished();
}
