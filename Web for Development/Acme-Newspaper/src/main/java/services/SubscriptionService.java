
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SubscriptionRepository;
import domain.Administrator;
import domain.CreditCard;
import domain.Customer;
import domain.Newspaper;
import domain.Subscription;

@Service
@Transactional
public class SubscriptionService {

	// Managed repositories ------------------------------------------------
	@Autowired
	private SubscriptionRepository	subscriptionRepository;

	// Services
	@Autowired
	private AdministratorService	administratorService;
	@Autowired
	private CreditCardService		creditCardService;
	@Autowired
	private NewspaperService		newspaperService;
	@Autowired
	private CustomerService			customerService;


	// Constructor ----------------------------------------------------------
	public SubscriptionService() {
		super();
	}

	// Methods CRUD ---------------------------------------------------------

	public Subscription create() {
		final Customer customer = this.customerService.findByPrincipal();
		Assert.notNull(customer);

		final Collection<CreditCard> creditCards = this.creditCardService.findByCustomerId(customer.getId());
		final Subscription subscription = new Subscription();
		subscription.setCustomer(customer);

		if (!creditCards.isEmpty())
			subscription.setCreditCard(creditCards.iterator().next());
		else
			subscription.setCreditCard(new CreditCard());

		return subscription;

	}

	public Subscription findOne(final int id) {
		Subscription result;

		result = this.subscriptionRepository.findOne(id);
		Assert.notNull(result);

		return result;
	}

	public Collection<Subscription> findAll() {

		Collection<Subscription> result;

		result = this.subscriptionRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Subscription save(final Subscription subscription) {
		Assert.notNull(subscription);
		final Customer customer = this.customerService.findByPrincipal();
		Assert.notNull(customer);
		Assert.isTrue(subscription.getCustomer().equals(customer));

		final Subscription subscriptionBD = this.subscriptionRepository.findByCustomerAndNewspaper(customer.getId(), subscription.getNewspaper().getId());
		Assert.isNull(subscriptionBD);

		Subscription saved;
		CreditCard savedCredit;

		savedCredit = this.creditCardService.save(subscription.getCreditCard());
		subscription.setCreditCard(savedCredit);
		saved = this.subscriptionRepository.save(subscription);

		return saved;
	}

	public Subscription saveForVolume(final Subscription subscription) {

		Assert.notNull(subscription);
		final Customer customer = this.customerService.findByPrincipal();
		Assert.notNull(customer);
		Assert.isTrue(subscription.getCustomer().equals(customer));

		final Subscription subscriptionBD = this.subscriptionRepository.findByCustomerAndNewspaper(customer.getId(), subscription.getNewspaper().getId());

		//Si ya esta subscrito, no lo subscribimos otra vez
		if (subscriptionBD != null)
			return null;
		else
			return this.save(subscription);
	}

	public void delete(final Subscription suscription) {
		Assert.notNull(suscription);

		this.subscriptionRepository.delete(suscription);
	}

	public void deleteInBatch(final Collection<Subscription> suscriptions) {
		Assert.notNull(suscriptions);
		final Administrator admin = this.administratorService.findByPrincipal();
		this.subscriptionRepository.deleteInBatch(suscriptions);
	}

	public void flush() {
		this.subscriptionRepository.flush();

	}

	public Collection<Subscription> findSuscriptionToNewspaper(final Newspaper newspaper) {
		// TODO Auto-generated method stub
		return this.subscriptionRepository.findSuscriptionToNewspaper(newspaper);
	}

	public Collection<Subscription> findAllByCustomer() {
		final Customer customer = this.customerService.findByPrincipal();
		Assert.notNull(customer);

		final Collection<Subscription> subscriptions = this.subscriptionRepository.findAllByCustomer(customer.getId());
		Assert.notNull(subscriptions);

		return subscriptions;
	}

	public Collection<Object> countsForRatioOfPrivateSubscribers() {
		return this.subscriptionRepository.countsForRatioOfPrivateSubscribers();

	}

	// N2.0 B The ratio of subscriptions to volumes versus subscriptions to newspapers
	public Double ratioSubscriptionsVSubcriptions() {
		final Double result = this.subscriptionRepository.ratioSubscriptionsVSubcriptions();
		return result != null ? result : 0.;
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

	public Subscription findByCustomerAndNewspaper(final Customer customer, final Newspaper newspaper) {
		final Subscription subscription = this.subscriptionRepository.findByCustomerAndNewspaper(customer.getId(), newspaper.getId());
		return subscription;
	}
}
