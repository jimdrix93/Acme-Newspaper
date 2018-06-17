
package controllers.customer;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.CreditCard;
import domain.Customer;
import domain.VSubscription;
import domain.Volume;
import services.CustomerService;
import services.VSubscriptionService;
import services.VolumeService;

@Controller
@RequestMapping("vsubscription/customer")
public class VSubscriptionCustomerController extends AbstractController {

	// Services ----------------------------------------------------
	@Autowired
	private VSubscriptionService	vsubscriptionService;
	
	@Autowired
	private VolumeService	volumeService;
	@Autowired
	private CustomerService customerService;


	// Creation ----------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam(required = false) Integer volumeId) {
		final ModelAndView result;
		final VSubscription vsubscription = this.vsubscriptionService.create();
		
		result = this.createEditModelAndView(vsubscription);
		
		if(volumeId!=null) {
			Volume volume = volumeService.findOne(volumeId);
			if(volume!=null) {
				vsubscription.setVolume(volume);				
			}	
		}
		return result;
	}

	// Save --------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("vsubscription") @Valid final VSubscription vsubscription, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(vsubscription);
		
		} else {
			
			CreditCard creditCard = vsubscription.getCreditCard();
			
			if(!this.vsubscriptionService.checkCreditCardDate(creditCard)) {
				
				
				result = this.createEditModelAndView(vsubscription,"subscription.creditCard.error");
				
				}else {
			
			
			try {
				this.vsubscriptionService.save(vsubscription);
				result = new ModelAndView("redirect:/vsubscription/customer/list.do");
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.createEditModelAndView(vsubscription, "vsubscription.commit.error");
			}
				}
		}

		return result;

	}
	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<VSubscription> vsubscriptions = this.vsubscriptionService.findAllByCustomer();

		result = new ModelAndView("vsubscription/customer/list");
		result.addObject("vsubscriptions", vsubscriptions);

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final VSubscription vsubscription) {
		ModelAndView result;

		result = this.createEditModelAndView(vsubscription, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final VSubscription vsubscription, final String message) {
		final ModelAndView result;

		Customer customer = this.customerService.findByPrincipal();
		final Collection<Volume> volumes = this.volumeService.findNotSubscribedVolumes(customer.getId());

		result = new ModelAndView("vsubscription/customer/create");
		result.addObject("vsubscription", vsubscription);
		result.addObject("volumes", volumes);
		result.addObject("message",message);
		
		return result;
	}

}
