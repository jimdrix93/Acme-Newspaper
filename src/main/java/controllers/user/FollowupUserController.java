
package controllers.user;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import services.FollowupService;
import services.UserService;
import controllers.AbstractController;
import domain.Followup;

@Controller
@RequestMapping("/followup/user")
public class FollowupUserController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private FollowupService	followupService;
	@Autowired
	private UserService		userService;
	@Autowired
	private ArticleService	articleService;


	// Constructor -----------------------------------------------------------
	public FollowupUserController() {
		super();
	}

	// Create ---------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int articleId) {
		ModelAndView result;

		final Followup followup = this.followupService.create(articleId);

		result = this.createEditModelAndView(followup);
		result.addObject("articleId", articleId);
		return result;
	}

	// Save ---------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Followup followup, final BindingResult binding, @RequestParam final int articleId) {
		ModelAndView result;
		final String url = "list.do?articleId=" + articleId;
		if (binding.hasErrors()) {
			result = new ModelAndView("followup/user/create");
			result.addObject("followup", followup);
			result.addObject("articleId", articleId);
		} else
			try {
				this.followupService.save(followup, articleId);
				result = new ModelAndView("redirect:" + url);
			} catch (final Throwable ooops) {
				if (ooops.getMessage() == "followup.picture.not.url") {
					result = this.createEditModelAndView(followup, "followup.picture.not.url");
					result.addObject("articleId", articleId);
				} else
					result = this.createEditModelAndView(followup, "followup.commit.error");

			}
		return result;
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int articleId) {

		ModelAndView result;

		final Collection<Followup> followups = this.followupService.findAllByArticle(articleId);

		result = new ModelAndView("followup/user/list");
		result.addObject("requestUri", "followup/user/list.do");
		result.addObject("followups", followups);
		result.addObject("articleId", articleId);

		return result;
	}
	// Display article -----------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int followupId, @RequestParam final int articleId) {
		final ModelAndView result;
		final Followup followup;

		followup = this.followupService.findOneToDisplay(articleId, followupId);

		result = new ModelAndView("followup/user/display");
		result.addObject("followup", followup);
		result.addObject("articleId", articleId);

		return result;
	}

	// Auxiliary methods ----------------------------------------------------
	protected ModelAndView createEditModelAndView(final Followup followup) {
		final ModelAndView result;
		result = this.createEditModelAndView(followup, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Followup followup, final String message) {

		final ModelAndView result = new ModelAndView("followup/user/create");
		result.addObject("followup", followup);
		result.addObject("message", message);
		return result;
	}
}
