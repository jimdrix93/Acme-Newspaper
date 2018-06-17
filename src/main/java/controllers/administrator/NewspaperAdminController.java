
package controllers.administrator;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.Actor;
import domain.Administrator;
import domain.Newspaper;
import domain.User;
import exceptions.HackingException;
import services.ActorService;
import services.NewspaperService;
import services.UserService;

@Controller
@RequestMapping("/newspaper/administrator")
public class NewspaperAdminController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private NewspaperService	newspaperService;
	@Autowired
	private UserService			userService;
	@Autowired
	private ActorService		actorService;


	// Constructor -----------------------------------------------------------
	public NewspaperAdminController() {
		super();
	}

	// Create ---------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Newspaper newspaper = this.newspaperService.create();

		result = this.createEditModelAndView(newspaper);
		return result;
	}

	// Save ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Newspaper newspaper, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = new ModelAndView("newspaper/edit");
			result.addObject("newspaper", newspaper);
		} else
			try {
				this.newspaperService.save(newspaper);
				result = new ModelAndView("redirect:/newspaper/user/list.do");
			} catch (final Throwable ooops) {
				result = this.createEditModelAndView(newspaper, "newspaper.commit.error");

			}
		return result;
	}

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
		final Newspaper newspaper = this.newspaperService.findOne(id);
		Assert.notNull(newspaper);
		try {
			this.newspaperService.deleteByAdministrator(newspaper);
			result = new ModelAndView("redirect:/tabooWord/administrator/listNewspapers.do");
		} catch (final Throwable ooops) {
			result = new ModelAndView("newspaper/display");
			result.addObject("newspaper", newspaper);
			result.addObject("message", "newspaper.commit.error");
		}
		return result;
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		final User user = this.userService.findByPrincipal();

		final Collection<Newspaper> newspapers = this.newspaperService.findByUser(user.getId());

		result = new ModelAndView("newspaper/list");
		result.addObject("newspapers", newspapers);
		result.addObject("user", user);
		result.addObject("showSubscribe", true);

		return result;
	}

	// Auxiliary methods ----------------------------------------------------
	protected ModelAndView createEditModelAndView(final Newspaper newspaper) {
		final ModelAndView result;
		result = this.createEditModelAndView(newspaper, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Newspaper newspaper, final String message) {
		final ModelAndView result = new ModelAndView("newspaper/edit");
		result.addObject("newspaper", newspaper);
		result.addObject("message", message);
		return result;
	}

}
