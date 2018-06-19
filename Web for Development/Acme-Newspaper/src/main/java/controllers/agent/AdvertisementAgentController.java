
package controllers.agent;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.Advertisement;
import domain.Agent;
import domain.CreditCard;
import domain.Newspaper;
import services.AdvertisementService;
import services.AgentService;
import services.NewspaperService;

@Controller
@RequestMapping("/advertisement/agent")
public class AdvertisementAgentController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private AdvertisementService advertisementService;
	@Autowired
	private AgentService agentService;
	@Autowired
	private NewspaperService newspaperService;

	// Constructor -----------------------------------------------------------
	public AdvertisementAgentController() {
		super();
	}

	// Create ---------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Advertisement advertisement = this.advertisementService.create();

		result = this.createEditModelAndView(advertisement);
		return result;
	}

	// Save ---------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Advertisement advertisement, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(advertisement);
		} else {
			
			CreditCard creditCard = advertisement.getCreditCard();
			
			if(!this.advertisementService.checkCreditCardDate(creditCard)) {
			
	
			result = this.createEditModelAndView(advertisement,"advertisement.creditCard.error");
			
			}else {
			
			
			
			try {
				this.advertisementService.save(advertisement);
				result = new ModelAndView("redirect:/advertisement/agent/list.do");
			} catch (final Throwable ooops) {
				ooops.printStackTrace();
				result = this.createEditModelAndView(advertisement, "advertisement.commit.error");
			}
			
			}
		}
		return result;
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		final Agent agent = this.agentService.findByPrincipal();

		final Collection<Advertisement> advertisements = this.advertisementService.findAllByAgent();

		result = new ModelAndView("advertisement/agent/list");
		result.addObject("requestUri", "advertisement/agent/list.do");
		result.addObject("advertisements", advertisements);
		result.addObject("agent", agent);

		return result;
	}

	// Auxiliary methods ----------------------------------------------------
	protected ModelAndView createEditModelAndView(final Advertisement advertisement) {
		final ModelAndView result;
		result = this.createEditModelAndView(advertisement, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Advertisement advertisement, final String message) {
		final ModelAndView result;

		final Collection<Newspaper> newspapers = this.newspaperService.findAll();

		result = new ModelAndView("advertisement/agent/create");
		result.addObject("newspapers", newspapers);
		result.addObject("advertisement", advertisement);
		result.addObject("message", message);
		result.addObject("requestUri", "advertisement/agent/create.do");
		return result;
	}
}
