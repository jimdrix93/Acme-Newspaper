
package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.Actor;
import domain.Administrator;
import exceptions.HackingException;
import services.ActorService;
import services.AdvertisementService;

@Controller
@RequestMapping("/advertisement/administrator")
public class AdvertisementAdminController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private AdvertisementService	advertisementService;
	@Autowired
	private ActorService			actorService;


	// Delete ---------------------------------------------------------------
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam(required = false) final Integer id) throws HackingException {
		ModelAndView result;
		final Actor actor = this.actorService.findByPrincipal();
		try {
			Assert.notNull(actor);
			Assert.isTrue(actor instanceof Administrator);
		} catch (final Throwable oops) {
			throw new HackingException(oops);
		}
		try {
			Assert.notNull(id);
		} catch (final Throwable ooops) {
			result = new ModelAndView("mis/oops");
			result.addObject("message", "assert.error.cause.nullId");
		}
		this.advertisementService.delete(id);
		result = new ModelAndView("redirect:/tabooWord/administrator/listAdvertisement.do");
		return result;
	}

}
