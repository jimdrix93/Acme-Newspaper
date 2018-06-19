
package controllers.customer;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CreditCardService;
import services.NewspaperService;
import services.SubscriptionService;
import services.VSubscriptionService;
import controllers.AbstractController;
import domain.CreditCard;
import domain.Newspaper;
import domain.Subscription;

@Controller
@RequestMapping("subscription/customer")
public class SubscriptionCustomerController extends AbstractController {

	// Services ----------------------------------------------------
	@Autowired
	private SubscriptionService		subscriptionService;
	@Autowired
	private VSubscriptionService	vsubscriptionService;
	@Autowired
	private CreditCardService		creditCardService;
	@Autowired
	private NewspaperService		newspaperService;


	// Creation ----------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam(required = false) final Integer newspaperId) {
		final ModelAndView result;
		final Subscription subscription = this.subscriptionService.create();
		result = this.createEditModelAndView(subscription);
		if (newspaperId != null) {
			final Newspaper newsp = this.newspaperService.findOne(newspaperId);
			if (newsp != null)
				subscription.setNewspaper(newsp);

		}
		return result;
	}

	// Save --------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Subscription subscription, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(subscription);
		else {

			final CreditCard creditCard = subscription.getCreditCard();

			if (!this.subscriptionService.checkCreditCardDate(creditCard))
				result = this.createEditModelAndView(subscription, "subscription.creditCard.error");
			else
				try {
					this.subscriptionService.save(subscription);
					result = new ModelAndView("redirect:/subscription/customer/list.do");
				} catch (final Throwable oops) {
					oops.printStackTrace();
					result = this.createEditModelAndView(subscription, "subscription.commit.error");
				}

		}

		return result;

	}
	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Subscription> subscriptions = this.subscriptionService.findAllByCustomer();

		result = new ModelAndView("subscription/customer/list");
		result.addObject("requestURI", "subscription/customer/list.do");
		result.addObject("subscriptions", subscriptions);

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Subscription subscription) {
		ModelAndView result;

		result = this.createEditModelAndView(subscription, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Subscription subscription, final String message) {
		final ModelAndView result;

		final Collection<Newspaper> newspapersPrivates = this.newspaperService.findAllPrivate();
		final Collection<Subscription> subscriptions = this.subscriptionService.findAllByCustomer();
		final Collection<Newspaper> newspapers = new ArrayList<Newspaper>(newspapersPrivates);

		for (final Subscription s : subscriptions)
			if (newspapersPrivates.contains(s.getNewspaper()))
				newspapers.remove(s.getNewspaper());

		result = new ModelAndView("subscription/customer/create");
		result.addObject("subscription", subscription);
		result.addObject("newspapers", newspapers);
		result.addObject("requestURI", "subscription/customer/create.do");
		result.addObject("message", message);

		return result;
	}

}
