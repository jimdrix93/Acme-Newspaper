
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Newspaper;
import domain.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

	@Query("select s from Subscription s where (s.newspaper like ?1)")
	Collection<Subscription> findSuscriptionToNewspaper(Newspaper newspaper);

	@Query("select s from Subscription s where s.customer.id = ?1")
	Collection<Subscription> findAllByCustomer(int id);

	@Query("select s from Subscription s where s.customer.id = ?1 and s.newspaper.id = ?2")
	Subscription findByCustomerAndNewspaper(int customerId, int newspaperId);

	// Dashboard A4 The ratio of subscribers per private newspaper versus the total
	// number of customers.
	@Query("select count(s), (select count(cu) from Customer cu) from Subscription s where s.newspaper.isPrivate = false")
	public Collection<Object> countsForRatioOfPrivateSubscribers();

	// N2.0 B The ratio of subscriptions to volumes versus subscriptions to newspapers
	@Query("select count(*)/(select count(*) from Subscription) from VSubscription")
	public Double ratioSubscriptionsVSubcriptions();
}
