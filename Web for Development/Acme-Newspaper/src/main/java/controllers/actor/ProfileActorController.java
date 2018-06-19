
package controllers.actor;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import controllers.AbstractController;
import domain.Actor;
import domain.User;
import forms.ActorRegisterForm;

@Controller
@RequestMapping("/profile/actor")
public class ProfileActorController extends AbstractController {

	// Supporting services -----------------------------------------------------
	@Autowired
	private ActorService	actorService;


	// Constructors -----------------------------------------------------------

	public ProfileActorController() {
		super();
	}

	// Edit ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		ActorRegisterForm actorRegisterForm;

		final Actor actor = this.actorService.findByPrincipal();
		actorRegisterForm = new ActorRegisterForm(actor);

		result = new ModelAndView("profile/actor/edit");
		result.addObject("actorModel", actorRegisterForm);
		result.addObject("requestUri", "profile/actor/edit.do");
		return result;
	}

	// Create user ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("actorModel") @Valid final ActorRegisterForm actorRegisterForm, final BindingResult binding) {
		ModelAndView result;
		Actor actor;

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorRegisterForm);
		else
			try {
				actor = this.actorService.reconstruct(actorRegisterForm, binding);
				this.actorService.save(actor);
				result = new ModelAndView("redirect:/profile/actor/display.do");
			} catch (final Throwable oops) {
				result = new ModelAndView("profile/actor/edit");
				result.addObject("actorModel", actorRegisterForm);
				if (oops.getMessage().startsWith("profile"))
					result.addObject("message", oops.getMessage());
				else
					result.addObject("message", "user.commit.error");
			}
		return result;
	}

	// Display user -----------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		final ModelAndView result;

		final Actor actor;

		actor = this.actorService.findByPrincipal();

		result = new ModelAndView("profile/actor/display");
		result.addObject("profile", actor);
		result.addObject("owner", true);

		return result;
	}

	// Auxiliary methods
	// ---------------------------------------------------------------
	protected ModelAndView createEditModelAndView(final User user) {
		final ModelAndView result;
		result = this.createEditModelAndView(user, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final User user, final String message) {
		final ModelAndView result;

		result = new ModelAndView("profile/actor/edit");
		result.addObject("user", user);
		result.addObject("message", message);

		return result;
	}

	// Auxiliary methods Form
	// ---------------------------------------------------------------
	protected ModelAndView createEditModelAndView(final ActorRegisterForm userRegisterForm) {
		final ModelAndView result;
		result = this.createEditModelAndView(userRegisterForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final ActorRegisterForm userRegisterForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("profile/actor/edit");
		result.addObject("actorModel", userRegisterForm);
		result.addObject("requestUri", "profile/actor/edit.do");
		result.addObject("message", message);

		return result;
	}
}
