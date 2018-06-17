
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
@RequestMapping("/user")
public class UserController extends AbstractController {

	// Supporting services -----------------------------------------------------
	@Autowired
	UserService			userService;
	@Autowired
	ActorService		actorService;

	@Autowired
	NewspaperService	newspaperService;

	@Autowired
	ArticleService		articleService;

	@Autowired
	ChirpService		chirpService;


	// Constructors -----------------------------------------------------------

	public UserController() {
		super();
	}

	// List ------------------------------------------------------------------
	@RequestMapping(value = "/list")
	public ModelAndView list() {
		ModelAndView result;

		final Collection<User> users = this.userService.findAll();

		result = new ModelAndView("user/list");
		result.addObject("users", users);
		result.addObject("requestUri", "user/list.do");
		result.addObject("hideFollow", true);
		
		return result;
	}

	// Create user ---------------------------------------------------------------
	@RequestMapping("/create")
	public ModelAndView create() {
		ModelAndView result;
		final User user = this.userService.create();

		result = this.createEditModelAndView(user);

		return result;
	}

	// Create user ---------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final User user, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(user);
		else
			try {
				this.userService.save(user);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(user, "user.commit.error");
			}
		return result;
	}

	// Display user -----------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId) {
		final ModelAndView result;

		final User user;

		user = this.userService.findOne(userId);

		final Collection<Article> articles = this.articleService.findArticlesPublishedInNewspapersByUser(user);
		final Collection<Chirp> chirps = user.getChirps();

		result = new ModelAndView("user/display");
		result.addObject("user", user);
		result.addObject("articles", articles);
		result.addObject("chirps", chirps);

		return result;
	}

	@RequestMapping(value = "/displayByArticle", method = RequestMethod.GET)
	public ModelAndView displayByArticle(@RequestParam final int articleId) {
		final ModelAndView result;
		final User user;
		user = this.userService.findOneByArticle(articleId);

		final Collection<Article> articles = this.articleService.findArticlesPublishedInNewspapersByUser(user);
		final Collection<Chirp> chirps = user.getChirps();

		result = new ModelAndView("user/display");
		result.addObject("user", user);
		result.addObject("articles", articles);
		result.addObject("chirps", chirps);

		return result;
	}
	// Auxiliary methods ---------------------------------------------------------------
	protected ModelAndView createEditModelAndView(final User user) {
		final ModelAndView result;
		result = this.createEditModelAndView(user, null);
		return result;
	}
	protected ModelAndView createEditModelAndView(final User user, final String message) {
		final ModelAndView result;

		result = new ModelAndView("user/register");
		result.addObject("user", user);
		result.addObject("uri", "user/register.do");
		result.addObject("message", message);

		return result;
	}

	// User Register Form ------------------------------------------------------------------

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		ModelAndView result;
		ActorRegisterForm actorRegisterForm;

		actorRegisterForm = new ActorRegisterForm();

		result = new ModelAndView("user/register");
		result.addObject("actorRegisterForm", actorRegisterForm);
		result.addObject("uri", "user/register.do");
		return result;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ActorRegisterForm actorRegisterForm, final BindingResult binding) {
		ModelAndView result;
		User user;

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorRegisterForm);
		else
			try {
				user = this.userService.reconstruct(actorRegisterForm);
				this.userService.save(user);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable oops) {
				result = new ModelAndView("user/register");
				result.addObject("actorRegisterForm", actorRegisterForm);
				result.addObject("uri", "user/register.do");
				result.addObject("message", "user.commit.error");
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

		result = new ModelAndView("user/register");
		result.addObject("user", actorRegisterForm);
		result.addObject("uri", "user/register.do");
		result.addObject("message", message);

		return result;
	}
}
