
package controllers.customer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.NewspaperService;
import services.VolumeService;
import controllers.AbstractController;
import domain.Actor;
import domain.Customer;
import domain.Newspaper;
import domain.Volume;

@Controller
@RequestMapping("/newspaper/customer")
public class NewspaperCustomerController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private NewspaperService	newspaperService;
	@Autowired
	private VolumeService		volumeService;

	@Autowired
	ActorService				actorService;


	// Constructor -----------------------------------------------------------
	public NewspaperCustomerController() {
		super();
	}

	// Display -----------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int newspaperId) {
		final ModelAndView result;
		final Newspaper newspaper;
		newspaper = this.newspaperService.findOneToDisplay(newspaperId);

		if (newspaper != null) {
			result = new ModelAndView("newspaper/display");
			result.addObject("newspaper", newspaper);
			if (newspaper.getIsPrivate()) {
				final Actor actor = this.actorService.findByPrincipal();
				if (actor instanceof Customer) {
					final Collection<Newspaper> subscribedNewspapers = this.newspaperService.findSubscribedNewspapers(actor.getId());
					for (final Volume volume : this.volumeService.findSubscribedVolumes(actor.getId()))
						subscribedNewspapers.addAll(volume.getNewspapers());
					if (subscribedNewspapers.contains(newspaper))
						result.addObject("subscribed", true);
					else
						result.addObject("subscribed", false);
				} else
					result.addObject("subscribed", false);
			}
		} else {
			result = new ModelAndView("misc/ooops");
			result.addObject("message", "subscribers.only.alert");
		}

		return result;
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;

		final Collection<Newspaper> newspapers = this.newspaperService.findAllPrivate();

		result = new ModelAndView("newspaper/customer/list");
		result.addObject("requestUri", "newspaper/customer/list.do");
		result.addObject("newspapers", newspapers);
		result.addObject("showSubscribe", true);

		return result;
	}

	// Auxiliary methods ----------------------------------------------------
	protected ModelAndView createEditModelAndView(final Collection<Newspaper> newspapers) {
		final ModelAndView result;
		result = this.createEditModelAndView(newspapers, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Collection<Newspaper> newspapers, final String message) {
		final ModelAndView result = new ModelAndView("newspaper/customer/list");
		result.addObject("requestUri", "newspaper/customer/list.do");
		result.addObject("newspapers", newspapers);
		result.addObject("message", message);
		return result;
	}

}
