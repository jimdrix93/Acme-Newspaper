
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.VSubscriptionRepository;
import domain.Administrator;
import domain.CreditCard;
import domain.Customer;
import domain.Newspaper;
import domain.Subscription;
import domain.VSubscription;

@Service
@Transactional
public class VSubscriptionService {

	// Managed repositories ------------------------------------------------
	@Autowired
	private VSubscriptionRepository	vsubscriptionRepository;

	// Services
	@Autowired
	private AdministratorService	administratorService;
	@Autowired
	private CreditCardService		creditCardService;
	@Autowired
	private CustomerService			customerService;
	@Autowired
	private VolumeService			volumeService;
	@Autowired
	private SubscriptionService		subscriptionService;


	// Constructor ----------------------------------------------------------
	public VSubscriptionService() {
		super();
	}

	// Methods CRUD ---------------------------------------------------------

	public VSubscription create() {
		final Customer customer = this.customerService.findByPrincipal();
		Assert.notNull(customer);

		final VSubscription vsubscription = new VSubscription();
		vsubscription.setCustomer(customer);

		final Collection<CreditCard> creditCards = this.creditCardService.findByCustomerId(customer.getId());

		if (!creditCards.isEmpty())
			vsubscription.setCreditCard(creditCards.iterator().next());
		else
			vsubscription.setCreditCard(new CreditCard());

		return vsubscription;

	}

	public VSubscription findOne(final int id) {
		VSubscription result;

		result = this.vsubscriptionRepository.findOne(id);
		Assert.notNull(result);

		return result;
	}

	public Collection<VSubscription> findAll() {

		Collection<VSubscription> result;

		result = this.vsubscriptionRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public VSubscription save(final VSubscription vsubscription) {
		Assert.notNull(vsubscription);

		final Customer customer = this.customerService.findByPrincipal();
		Assert.notNull(customer);

		Assert.isTrue(vsubscription.getCustomer().equals(customer));

		//Nos aseguramos que no se ha suscrito ya
		final VSubscription subscriptionBD = this.vsubscriptionRepository.findByCustomerAndVolume(customer.getId(), vsubscription.getVolume().getId());
		Assert.isNull(subscriptionBD);

		VSubscription saved;
		CreditCard savedCredit;

		savedCredit = this.creditCardService.save(vsubscription.getCreditCard());
		vsubscription.setCreditCard(savedCredit);
		saved = this.vsubscriptionRepository.save(vsubscription);

		final Collection<Newspaper> newspapersSubscribed = this.volumeService.findNewspapersByVolume(saved.getVolume().getId());
		for (final Newspaper newspaper : newspapersSubscribed) {
			final Subscription subscription = this.subscriptionService.create();
			subscription.setNewspaper(newspaper);
			subscription.setCreditCard(saved.getCreditCard());
			this.subscriptionService.saveForVolume(subscription);
		}

		return saved;
	}

	public void delete(final VSubscription vsuscription) {
		Assert.notNull(vsuscription);

		this.vsubscriptionRepository.delete(vsuscription);
	}

	public void deleteInBatch(final Collection<VSubscription> vsuscriptions) {
		Assert.notNull(vsuscriptions);
		final Administrator admin = this.administratorService.findByPrincipal();
		Assert.notNull(admin);
		this.vsubscriptionRepository.deleteInBatch(vsuscriptions);
	}

	public void flush() {
		this.vsubscriptionRepository.flush();

	}

	public Collection<VSubscription> findVSubscriptionToVolume(final int volumeId) {

		return this.vsubscriptionRepository.findVSuscriptionToVolume(volumeId);
	}

	public Collection<VSubscription> findAllByCustomer() {
		final Customer customer = this.customerService.findByPrincipal();
		Assert.notNull(customer);

		final Collection<VSubscription> subscriptions = this.vsubscriptionRepository.findAllByCustomer(customer.getId());
		Assert.notNull(subscriptions);

		return subscriptions;
	}

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

}
