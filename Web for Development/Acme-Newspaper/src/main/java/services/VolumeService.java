
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.VolumeRepository;
import domain.Actor;
import domain.Newspaper;
import domain.User;
import domain.VSubscription;
import domain.Volume;
import forms.VolumeForm;

@Service
@Transactional
public class VolumeService {

	// Managed repository ----------------------------------------------------
	@Autowired
	private VolumeRepository		volumeRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private UserService				userService;

	//	@Autowired
	//	private VSubscriptionService vSubscriptionService;

	@Autowired
	private ArticleService			articleService;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private AdministratorService	administratorService;
	@Autowired
	private NewspaperService		newspaperService;
	@Autowired
	private VSubscriptionService	vsubscriptionService;


	// Constructors -----------------------------------------------------------

	public VolumeService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Volume create() {
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);
		final Volume res = new Volume();
		res.setUser(user);
		return res;
	}

	public Volume save(final Volume volume) {
		Volume savedVolume;
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof User);
		Assert.isTrue(((User) actor).equals(volume.getUser()));

		if (volume.getId() != 0) {
			savedVolume = this.volumeRepository.findOne(volume.getId());
			Assert.isTrue(((User) actor).equals(savedVolume.getUser()));
		}
		Assert.isTrue(!volume.getNewspapers().isEmpty(), "volume.newspapers.empty");
		savedVolume = this.volumeRepository.saveAndFlush(volume);

		return savedVolume;
	}

	public Volume findOne(final int volumeId) {
		Assert.notNull(volumeId);
		final Volume volume = this.volumeRepository.findOne(volumeId);
		Assert.notNull(volume);
		return volume;
	}

	// Other business methods -------------------------------------------------
	public void checkPrincipal(final Volume volume) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(principal instanceof User);
		Assert.isTrue(this.findVolumeByUser(principal.getId()).contains(volume));
	}

	public Collection<Volume> findVolumeByUser(final Integer userId) {
		final Collection<Volume> volumes = this.volumeRepository.findVolumeByUser(userId);
		return volumes;
	}

	public void flush() {
		this.volumeRepository.flush();

	}

	public Collection<Volume> findAll() {
		return this.volumeRepository.findAll();
	}

	public Collection<Volume> findSubscribedVolumes(final int customerId) {
		return this.volumeRepository.findSubscribedVolumes(customerId);
	}

	public Collection<Volume> findNotSubscribedVolumes(final int customerId) {
		return this.volumeRepository.findNotSubscribedVolumes(customerId);
	}

	public Volume reconstruct(final VolumeForm volumeForm, final BindingResult binding) {
		Assert.notNull(volumeForm);

		Volume volume = this.volumeRepository.findOne(volumeForm.getId());
		if (volume == null)
			volume = this.create();

		volume.setTitle(volumeForm.getTitle());
		volume.setDescription(volumeForm.getDescription());
		volume.setYear(volumeForm.getYear());
		final List<Newspaper> newspapers = new ArrayList<>();
		for (final Entry<Newspaper, Boolean> entry : volumeForm.getNewspapers().entrySet())
			if (entry.getValue() != null && entry.getValue())
				newspapers.add(entry.getKey());
		volume.setNewspapers(newspapers);
		//this.validator.validate(newspaper, binding);
		return volume;
	}

	public Collection<Volume> findAllByUser(final int userId) {
		return this.volumeRepository.findVolumeByUser(userId);
	}

	public Collection<Newspaper> findNewspapersByVolume(final int volumeId) {

		return this.volumeRepository.findNewspapersByVolume(volumeId);
	}

	public void removeNewspaperFromVolumes(final int id) {
		final Newspaper newspaper = this.newspaperService.findOne(id);
		final Collection<Volume> volumes = this.volumeRepository.findVolumeByNewspaperId(id);
		for (final Volume v : volumes) {
			v.getNewspapers().remove(newspaper);
			if (v.getNewspapers().isEmpty())
				this.delete(v);
		}
	}

	private void delete(final Volume v) {

		final Collection<VSubscription> vsubscriptions = this.vsubscriptionService.findVSubscriptionToVolume(v.getId());
		if (vsubscriptions != null)
			this.vsubscriptionService.deleteInBatch(vsubscriptions);

		this.volumeRepository.delete(v);

	}

}
