
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u where ?1 member of u.followeds")
	Collection<User> findFollowersByUser(User user);

	@Query("select u from User u where ?1 member of u.articles")
	User findOneByArticle(int articleId);

	@Query("select u from User u where ?1 member of u.chirps")
	User findByChirp(int chirpId);

	@Query("select u from User u where ?1 member of u.newspapers")
	User findOneByNewspaper(int newspaperId);

	// The average and the standard deviation of newspapers created per user.
	// La desviacion estandar: stdev(x) = sqrt(sum(x*x)/count(x) - avg(x)*avg(x))
	@Query("select sum(u.newspapers.size*u.newspapers.size), avg(u.newspapers.size), count(u.newspapers.size) from User u")
	Collection<Object> sumAvgAndCountfNewspPerUser();

	@Query("select sum(u.articles.size*u.articles.size), avg(u.articles.size), count(u.articles.size) from User u")
	Collection<Object> sumAvgAndCountfArticlesPerUser();

	@Query("select count(u),(select count(us) from User us) from User u where u.newspapers.size > 0")
	Collection<Object> countsForRatioOfNewspaperWriters();

	@Query("select count(u),(select count(us) from User us) from User u where u.articles.size > 0")
	Collection<Object> countsForRatioOfArticleWriters();

	// The ratio of users who have posted above 75% the average number of chirps
	// per user
	@Query("select sum(case when u.chirps.size > 0.75*(select avg(u1.chirps.size) from User u1) then 1.0 else 0.0 end)/count(u) from User u")
	Double ratioOfUsersAbove75ChirpsPerUser();

}
