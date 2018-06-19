
package controllers.user;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.Article;
import domain.Chirp;
import domain.User;
import forms.ActorRegisterForm;
import services.ActorService;
import services.ArticleService;
import services.ChirpService;
import services.NewspaperService;
import services.UserService;

@Controller
@RequestMapping("/user/user")
public class UserUserController extends AbstractController {

	// Supporting services -----------------------------------------------------
	@Autowired
	UserService userService;
	@Autowired
	ActorService actorService;

	@Autowired
	NewspaperService newspaperService;

	@Autowired
	ArticleService articleService;

	@Autowired
	ChirpService chirpService;

	// Constructors -----------------------------------------------------------

	public UserUserController() {
		super();
	}

	// Edit ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		ActorRegisterForm userRegisterForm;

		final User user = this.userService.findByPrincipal();
		userRegisterForm = new ActorRegisterForm(user);
		
		result = new ModelAndView("user/user/edit");
		result.addObject("userModel", userRegisterForm);
		result.addObject("requestUri", "user/user/edit.do");
		return result;
	}

	// Create user ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ActorRegisterForm userRegisterForm, final BindingResult binding) {
		ModelAndView result;
		User user;

		if (binding.hasErrors())
			result = this.createEditModelAndView(userRegisterForm);
		else
			try {
				user = this.userService.reconstruct(userRegisterForm);
				this.userService.save(user);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable oops) {
				result = new ModelAndView("user/register");
				result.addObject("userRegisterForm", userRegisterForm);
				result.addObject("message", "user.commit.error");
			}
		return result;
	}

	// Display user -----------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		final ModelAndView result;

		final User user;

		user = this.userService.findByPrincipal();

		final Collection<Article> articles = this.articleService.findArticlesPublishedInNewspapersByUser(user);
		final Collection<Chirp> chirps = user.getChirps();

		result = new ModelAndView("user/display");
		result.addObject("user", user);
		result.addObject("articles", articles);
		result.addObject("chirps", chirps);
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

		result = new ModelAndView("user/user/edit");
		result.addObject("user", user);
		result.addObject("message", message);

		return result;
	}
	// Auxiliary methods Form ---------------------------------------------------------------
	protected ModelAndView createEditModelAndView(final ActorRegisterForm userRegisterForm) {
		final ModelAndView result;
		result = this.createEditModelAndView(userRegisterForm, null);
		return result;
	}
	protected ModelAndView createEditModelAndView(final ActorRegisterForm userRegisterForm, final String message) {
		final ModelAndView result;

		result = new ModelAndView("user/user/edit");
		result.addObject("userModel", userRegisterForm);
		result.addObject("requestUri", "user/user/edit.do");
		result.addObject("message", message);

		return result;
	}
}
