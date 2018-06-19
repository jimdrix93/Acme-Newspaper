
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Customer;
import forms.ActorRegisterForm;
import services.ActorService;
import services.CustomerService;

@Controller
@RequestMapping("/customer")
public class CustomerController extends AbstractController {

	// Supporting services -----------------------------------------------------
	@Autowired
	CustomerService	customerService;
	@Autowired
	ActorService	actorService;


	// Constructors -----------------------------------------------------------

	public CustomerController() {
		super();
	}

	// User Register Form ------------------------------------------------------------------

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		ModelAndView result;
		ActorRegisterForm actorRegisterForm;

		actorRegisterForm = new ActorRegisterForm();

		result = new ModelAndView("customer/register");
		result.addObject("actorRegisterForm", actorRegisterForm);
		result.addObject("uri", "customer/register.do");
		return result;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ActorRegisterForm actorRegisterForm, final BindingResult binding) {
		ModelAndView result;
		Customer customer;

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorRegisterForm);
		else
			try {
				customer = this.customerService.reconstruct(actorRegisterForm);
				this.customerService.save(customer);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable oops) {
				result = new ModelAndView("customer/register");
				result.addObject("actorRegisterForm", actorRegisterForm);
				result.addObject("uri", "customer/register.do");
				result.addObject("message", "customer.commit.error");
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

		result = new ModelAndView("customer/register");
		result.addObject("customer", actorRegisterForm);
		result.addObject("uri", "customer/register.do");
		result.addObject("message", message);

		return result;
	}
}
