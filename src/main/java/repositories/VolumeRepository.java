
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Newspaper;
import domain.Volume;

@Repository
public interface VolumeRepository extends JpaRepository<Volume, Integer> {

	@Query("select v from Volume v where v.user.id = ?1")
	public Collection<Volume> findVolumeByUser(int userId);

	@Query("select s.volume from VSubscription s where s.customer.id=?1")
	public Collection<Volume> findSubscribedVolumes(int customerId);

	@Query("select v from Volume v where v.id not in (select s.volume.id from VSubscription s where s.customer.id=?1)")
	public Collection<Volume> findNotSubscribedVolumes(int customerId);

	@Query("select v.newspapers from Volume v where v.id=?1")
	public Collection<Newspaper> findNewspapersByVolume(int volumeId);

	@Query("select v from Volume v where ?1 member of v.newspapers")
	public Collection<Volume> findVolumeByNewspaperId(int id);

}
