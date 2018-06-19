
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chirp;

@Repository
public interface ChirpRepository extends JpaRepository<Chirp, Integer> {
	
	//The average and the standard deviation of the number of chirps per user
	@Query("select avg(u.chirps.size), sqrt(sum(u.chirps.size*u.chirps.size)/count(u.chirps.size)-(avg(u.chirps.size)*avg(u.chirps.size))) from User u")
	Collection<Object> avgAndStdevOfChirpsPerUser();
	
}
