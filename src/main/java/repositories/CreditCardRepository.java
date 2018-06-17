
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.CreditCard;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {

	@Query("select c from CreditCard c where c.number = ?1 and c.cvv = ?2")
	CreditCard findSameCreditcard(String number, Integer cvv);

	@Query("select s.creditCard from Subscription s where s.customer.id = ?1")
	Collection<CreditCard> findByCustomerId(int id);

}
