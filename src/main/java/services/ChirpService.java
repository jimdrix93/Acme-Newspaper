
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ChirpRepository;
import domain.Administrator;
import domain.Chirp;
import domain.User;

@Service
@Transactional
public class ChirpService {

	// Managed repository ----------------------------------------------------
	@Autowired
	private ChirpRepository			chirpRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private UserService				userService;
	@Autowired
	private AdministratorService	administratorService;
	@Autowired
	private TabooWordService		tabooWordService;


	// Constructors -----------------------------------------------------------

	public ChirpService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Chirp create() {
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);

		final Date moment = new Date(System.currentTimeMillis() - 1);

		final Chirp res = new Chirp();
		res.setMoment(moment);

		return res;
	}

	public Chirp findOne(final Integer id) {
		final Chirp chirp = this.chirpRepository.findOne(id);
		Assert.notNull(chirp);
		return chirp;
	}

	public Chirp save(final Chirp chirp) {
		final Chirp saved;
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);

		saved = this.chirpRepository.save(chirp);
		if (!user.getChirps().contains(saved)) {
			user.getChirps().add(saved);
			this.userService.save(user);
		}

		return saved;
	}

	public void flush() {
		this.chirpRepository.flush();

	}

	public void deleteByAdmin(final Chirp chirp) {
		Assert.isTrue(chirp.getId() != 0);

		final Administrator administrator = this.administratorService.findByPrincipal();
		Assert.notNull(administrator);
		//		final Collection<Chirp> tabooChirps = this.tabooWordService.findAllChirpsWithTabooWord();
		//		Assert.isTrue(tabooChirps.contains(chirp));
		final User user = this.userService.findByChirp(chirp.getId());
		user.getChirps().remove(chirp);

		this.chirpRepository.delete(chirp);
	}
	// Other business methods -------------------------------------------------

	public void checkPrincipal(final Chirp chirp) {
		final User principal = this.userService.findByPrincipal();

		Assert.isTrue(principal.getChirps().contains(chirp));
	}

	public Collection<Chirp> findAllByPrincipal() {
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);

		final Collection<Chirp> result = user.getChirps();
		return result;
	}

	public Collection<Chirp> findAll() {
		final Collection<Chirp> result = this.chirpRepository.findAll();
		return result;
	}

	public Collection<Chirp> findAllByFollowed() {
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);
		final Collection<User> followeds = this.userService.findFollowersByUser(user);
		final Collection<Chirp> chirps = new ArrayList<Chirp>();
		for (final User u : followeds)
			chirps.addAll(u.getChirps());
		return chirps;
	}

	public Collection<Object> avgAndStdevOfChirpsPerUser() {
		return this.chirpRepository.avgAndStdevOfChirpsPerUser();
	}

}
