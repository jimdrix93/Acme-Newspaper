
package controllers.user;

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
import domain.Newspaper;
import domain.User;
import exceptions.HackingException;
import forms.NewspaperEditForm;
import services.NewspaperService;
import services.UserService;

@Controller
@RequestMapping("/newspaper/user")
public class NewspaperUserController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private NewspaperService	newspaperService;
	@Autowired
	private UserService			userService;


	// Constructor -----------------------------------------------------------
	public NewspaperUserController() {
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
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Newspaper newspaper, final BindingResult binding) throws HackingException {
		ModelAndView result;
		if (binding.hasErrors()) {
			result = new ModelAndView("newspaper/user/create");
			result.addObject("newspaper", newspaper);
		} else
			try {
				final Actor actor = this.userService.findByPrincipal();
				try {
					Assert.notNull(actor);
					Assert.isTrue(actor instanceof User);
					if (newspaper.getId() != 0)
						this.newspaperService.checkPrincipal(newspaper);
				} catch (final Throwable oops) {
					throw new HackingException(oops);
				}

				this.newspaperService.save(newspaper);
				result = new ModelAndView("redirect:/newspaper/user/list.do");
			} catch (final Throwable ooops) {
				if (ooops.getMessage().equals("All articles is not final mode"))
					result = this.createEditModelAndView(newspaper, "newspaper.commit.error.articles");
				else if (ooops.getMessage().equals("Can not publicate a newspaper whitout articles"))
					result = this.createEditModelAndView(newspaper, "newspaper.commit.error.without.articles");
				else if (ooops.getMessage().equals("Must have a publication date"))
					result = this.createEditModelAndView(newspaper, "newspaper.commit.error.date");
				else
					result = this.createEditModelAndView(newspaper, "newspaper.commit.error");
			}
		return result;
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		final User user = this.userService.findByPrincipal();

		final Collection<Newspaper> newspapers = this.newspaperService.findByUser(user.getId());

		result = new ModelAndView("newspaper/user/list");
		result.addObject("requestUri", "newspaper/user/list.do");
		result.addObject("newspapers", newspapers);
		result.addObject("user", user);
		result.addObject("showEdit", true);

		return result;
	}

	// Auxiliary methods ----------------------------------------------------
	protected ModelAndView createEditModelAndView(final Newspaper newspaper) {
		final ModelAndView result;
		result = this.createEditModelAndView(newspaper, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Newspaper newspaper, final String message) {
		final ModelAndView result = new ModelAndView("newspaper/user/create");
		result.addObject("newspaper", newspaper);
		result.addObject("message", message);
		return result;
	}

	// Forms ----------------------------------------------------------------

	//edit newspaper form
	// Edit ---------------------------------------------------------------
	@RequestMapping(value = "/editForm", method = RequestMethod.GET)
	public ModelAndView editForm(@RequestParam final int newspaperId) {
		ModelAndView result;
		Newspaper newspaper;
		newspaper = this.newspaperService.findOneToEdit(newspaperId);
		//		final NewspaperEditForm newspaperEditForm = this.newspaperService.constructEditForm(newspaper);

		result = new ModelAndView("newspaper/user/editForm");
		result.addObject("newspaper", newspaper);

		return result;
	}

	// Save ---------------------------------------------------------------
	@RequestMapping(value = "/editForm", method = RequestMethod.POST, params = "save")
	public ModelAndView save2(@Valid final Newspaper newspaper, final BindingResult binding) throws HackingException {
		ModelAndView result;

		//		final Newspaper newspaper = this.newspaperService.reconstruct(newspaperEditForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(newspaper);
		else
			try {
				final Actor actor = this.userService.findByPrincipal();

				try {
					Assert.notNull(actor);
					Assert.isTrue(actor instanceof User);
					if (newspaper.getId() != 0)
						this.newspaperService.checkPrincipal(newspaper);
				} catch (final Throwable oops) {
					throw new HackingException(oops);
				}
				this.newspaperService.save(newspaper);
				result = new ModelAndView("redirect:/newspaper/user/list.do");
			} catch (final Throwable ooops) {
				//TODO capturar el error
				if (ooops.getMessage().equals("All articles is not final mode"))
					result = this.createEditModelAndView(newspaper, "newspaper.commit.error.articles");
				else if (ooops.getMessage().equals("Can not publicate a newspaper whitout articles"))
					result = this.createEditModelAndView(newspaper, "newspaper.commit.error.without.articles");
				else if (ooops.getMessage().equals("Must have a publication date"))
					result = this.createEditModelAndView(newspaper, "newspaper.commit.error.date");
				else
					result = this.createEditModelAndView(newspaper, "newspaper.commit.error");
			}
		return result;
	}
	// Delete ---------------------------------------------------------------
	@RequestMapping(value = "/editForm", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid final NewspaperEditForm newspaperEditForm, final BindingResult binding) {
		ModelAndView result;
		final Newspaper newspaper = this.newspaperService.reconstruct(newspaperEditForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(newspaper);
		else
			try {
				this.newspaperService.delete(newspaper);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable ooops) {
				result = this.createEditModelAndView(newspaper, "newspaper.commit.error");
			}
		return result;
	}

	// Auxiliary methods ----------------------------------------------------
	protected ModelAndView createEditModelAndView(final NewspaperEditForm newspaperEditForm) {
		final ModelAndView result;
		result = this.createEditModelAndView(newspaperEditForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final NewspaperEditForm newspaperEditForm, final String message) {
		final ModelAndView result;
		result = new ModelAndView("newspaper/user/editForm");
		result.addObject("newspaperEditForm", newspaperEditForm);
		result.addObject("message", message);

		return result;
	}

}
