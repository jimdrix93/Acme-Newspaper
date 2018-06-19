
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Agent;
import forms.ActorRegisterForm;
import services.ActorService;
import services.AgentService;

@Controller
@RequestMapping("/agent")
public class AgentController extends AbstractController {

	// Supporting services -----------------------------------------------------
	@Autowired
	AgentService	agentService;
	@Autowired
	ActorService	actorService;


	// Constructors -----------------------------------------------------------

	public AgentController() {
		super();
	}

	// User Register Form ------------------------------------------------------------------

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		ModelAndView result;
		ActorRegisterForm actorRegisterForm;

		actorRegisterForm = new ActorRegisterForm();

		result = new ModelAndView("agent/register");
		result.addObject("actorRegisterForm", actorRegisterForm);
		result.addObject("uri", "agent/register.do");
		return result;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ActorRegisterForm actorRegisterForm, final BindingResult binding) {
		ModelAndView result;
		Agent agent;

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorRegisterForm);
		else
			try {
				agent = this.agentService.reconstruct(actorRegisterForm);
				this.agentService.save(agent);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable oops) {
				result = new ModelAndView("agent/register");
				result.addObject("actorRegisterForm", actorRegisterForm);
				result.addObject("uri", "agent/register.do");
				result.addObject("message", "agent.commit.error");
			}
		return result;
	}

	// Auxiliary methods Form ---------------------------------------------------------------
	protected ModelAndView createEditModelAndView(final ActorRegisterForm actorRegisterForm) {
		final ModelAndView result;
		result = this.createEditModelAndView(actorRegisterForm, null);
		return result;
	}
	protected ModelAndView createEditModelAndView(final ActorRegisterForm actorRegisterForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("agent/register");
		result.addObject("agent", actorRegisterForm);
		result.addObject("uri", "agent/register.do");
		result.addObject("message", message);

		return result;
	}
}
