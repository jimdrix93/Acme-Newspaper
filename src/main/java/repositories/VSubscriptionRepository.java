
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Newspaper;
import domain.Subscription;
import domain.VSubscription;

@Repository
public interface VSubscriptionRepository extends JpaRepository<VSubscription, Integer> {

	@Query("select s from VSubscription s where s.volume.id like ?1")
	Collection<VSubscription> findVSuscriptionToVolume(int volumeId);

	@Query("select s from VSubscription s where s.customer.id = ?1")
	Collection<VSubscription> findAllByCustomer(int id);

	@Query("select s from VSubscription s where s.customer.id = ?1 and s.volume.id = ?2")
	VSubscription findByCustomerAndVolume(int customerId, int volumeId);

}
