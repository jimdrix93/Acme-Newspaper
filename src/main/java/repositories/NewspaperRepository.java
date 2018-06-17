
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Article;
import domain.Newspaper;

@Repository
public interface NewspaperRepository extends JpaRepository<Newspaper, Integer> {

	@Query("select user.newspapers from User user where user.id = ?1")
	public Collection<Newspaper> findNewspaperByPublisher(int userId);

	@Query("select n from domain.Newspaper n where (n.title like %?1% or n.description like %?1%) and n.isPublicated = true")
	Collection<Newspaper> findNewspaperByKeyword(String word);

	@Query("select n from Newspaper n where n.isPublicated = true")
	public Collection<Newspaper> findAllNewspaperPublicated();

	@Query("select n from Newspaper n where ?1 member of n.articles")
	public Newspaper findNewspaperByArticle(Article article);

	@Query("select n from Newspaper n where n.isPublicated = false")
	public Collection<Newspaper> findAllNotPublished();

	// The average and the standard deviation of newspapers created per user.
	// La desviacion estandar: stdev(x) = sqrt(sum(x*x)/count(x) - avg(x)*avg(x))
	@Query("select sum(n.articles.size*n.articles.size), avg(n.articles.size), count(n.articles.size) from Newspaper n")
	public Collection<Object> sumAvgAndCountfArticlesPerNewspaper();

	// Dashboard C4
	@Query("select n from Newspaper n where n.articles.size*1.0>=?1")
	public Collection<Newspaper> findNewspapersOverAvgOfArticles(double avg);

	// Dashboard C5
	@Query("select n from Newspaper n where ?1>=n.articles.size*1.0")
	public Collection<Newspaper> findNewspapersUnderAvgOfArticles(double rasero);

	// Dashboard A1
	@Query("select count(n), (select count(ne) from Newspaper ne) from Newspaper n where n.isPrivate = false")
	Collection<Object> countsForRatioOfPublicNewspapers();

	// Dashboard A2
	@Query("select avg(n.articles.size) from Newspaper n where n.isPrivate = true")
	public Double avgOfArticleInPrivateNewspapers();

	@Query("select n from Newspaper n where n.isPrivate = true and n.isPublicated = true")
	public Collection<Newspaper> findAllPrivate();

	// Dashboard A3
	@Query("select avg(n.articles.size) from Newspaper n where n.isPrivate = false")
	public Double avgOfArticleInPublicNewspapers();
	// Dashboard A5
	@Query(value = "" + "select avg(cuenta.ratio) from\r\n" + "	(select (select count(une.newspapers_id)  \r\n" + "    from user us \r\n" + "    left join user_newspaper une on us.id = une.User_id \r\n"
		+ "    where us.id = u.id and une.newspapers_id in \r\n" + "	(select id from newspaper where isPrivate = true) \r\n" + ")/count(un.newspapers_id) as ratio\r\n" + "from user u left join user_newspaper un on u.id = un.User_id group by u.id\r\n"
		+ ") as cuenta", nativeQuery = true)
	Double avgRatioOfPrivateNewspapersPerPublisher();

	@Query("select s.newspaper from Subscription s where s.customer.id=?1")
	public Collection<Newspaper> findSubscribedNewspapers(int id);

	// N2.0 C3 The ratio of newspapers that have at least one advertisement versus the newspapers that haven't any
	@Query("select count(distinct n)/(select count(n1) from Newspaper n1) from Article a join a.newspaper n")
	public Double ratioAtLeastOneAdvertisement();

	// N2.0 B The average number of newspapers per volume.
	@Query("select avg(v.newspapers.size) from Volume v")
	public Double avgOfNewspaperPerVolume();

	@Query("select a.newspaper from Advertisement a")
	public Collection<Newspaper> findNewspapersWithAdvertisement();

}
