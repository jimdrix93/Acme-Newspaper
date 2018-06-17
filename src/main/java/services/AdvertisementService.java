
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AdvertisementRepository;
import domain.Administrator;
import domain.Advertisement;
import domain.Agent;
import domain.CreditCard;

@Service
@Transactional
public class AdvertisementService {

	// Managed repositories ------------------------------------------------
	@Autowired
	private AdvertisementRepository	advertisementRepository;

	//Services
	@Autowired
	private AdministratorService	administratorService;
	@Autowired
	private AgentService			agentService;
	@Autowired
	private CreditCardService		creditCardService;


	// Constructor ----------------------------------------------------------
	public AdvertisementService() {
		super();
	}

	// Methods CRUD ---------------------------------------------------------

	public Advertisement create() {
		final Agent agent = this.agentService.findByPrincipal();
		Assert.notNull(agent);

		final Advertisement advertisement = new Advertisement();
		advertisement.setAgent(agent);
		return advertisement;
	}

	public Collection<Advertisement> findAll() {
		final Agent agent = this.agentService.findByPrincipal();
		Assert.notNull(agent);
		final Collection<Advertisement> advertisements = this.advertisementRepository.findAll();
		return advertisements;
	}
	
	public Collection<Advertisement> findAllByAgent() {
		final Agent agent = this.agentService.findByPrincipal();
		Assert.notNull(agent);
		final Collection<Advertisement> advertisements = this.advertisementRepository.findAllByAgent(agent.getId());
		return advertisements;
	}

	public Advertisement save(final Advertisement advertisement) {
		Advertisement saved;
		final Agent agent = this.agentService.findByPrincipal();
		Assert.notNull(agent);
		Assert.notNull(advertisement);

		CreditCard savedCredit;
		savedCredit = this.creditCardService.save(advertisement.getCreditCard());
		advertisement.setCreditCard(savedCredit);

		saved = this.advertisementRepository.save(advertisement);
		return saved;

	}

	public void delete(final Integer advertisementId) {
		Advertisement advertisement;
		advertisement = this.advertisementRepository.findOne(advertisementId);
		Assert.notNull(advertisement);
		this.advertisementRepository.delete(advertisement);

	}

	public Advertisement findOne(final Integer advertisementId) {
		Advertisement advertisement;
		advertisement = this.advertisementRepository.findOne(advertisementId);
		Assert.notNull(advertisement);
		return advertisement;
	}
	// Other business methods -------------------------------------------------

	// N2.0 C3 The ratio of advertisements that have taboo words.
	public Double ratioAdvertisementWithTabooWords() {
		//		Assert.isTrue(this.administratorService.findByPrincipal().getUserAccount().getAuthorities().contains("ADMIN"));
		return this.advertisementRepository.ratioAdvertisementsWithTabooWords();
	}

	public Collection<Advertisement> findAllByNewspaperId(final Integer newspaperId) {
		final Collection<Advertisement> advertisements = this.advertisementRepository.findByNewspaperId(newspaperId);
		Assert.notNull(advertisements);
		return advertisements;
	}

	public Advertisement findByNewspaperId(final Integer newspaperId) {
		final LinkedList<Advertisement> advertisements = new LinkedList<Advertisement>(this.advertisementRepository.findByNewspaperId(newspaperId));
		final Random rand = new Random();
		Advertisement result = null;
		if (advertisements != null && !advertisements.isEmpty())
			result = advertisements.get(rand.nextInt(advertisements.size()));

		return result;
	}

	//check credit card date
	public boolean checkCreditCardDate(final CreditCard creditCard) {
		Assert.notNull(creditCard);

		boolean result = false;

		final Date d = new Date();
		final Calendar currentDate = new GregorianCalendar();
		currentDate.setTime(d);
		final int month = currentDate.get(Calendar.MONTH) + 1;
		int year = currentDate.get(Calendar.YEAR);

		final String stringYear = Integer.toString(year);
		final String substringYear = stringYear.substring(2, 4);
		year = Integer.parseInt(substringYear);

		if (creditCard.getExpirationYear() > year || (creditCard.getExpirationYear() == year && creditCard.getExpirationMonth() > month))
			result = true;

		return result;

	}

	public void flush() {
		this.advertisementRepository.flush();

	}

	public void deleteInBatch(final Collection<Advertisement> advertisements) {
		Assert.notNull(advertisements);
		final Administrator admin = this.administratorService.findByPrincipal();
		this.advertisementRepository.deleteInBatch(advertisements);

	}

}
