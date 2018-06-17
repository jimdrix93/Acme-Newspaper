
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
import services.NewspaperService;
import services.UserService;
import controllers.AbstractController;
import domain.Article;
import domain.Newspaper;
import domain.User;

@Controller
@RequestMapping("/article/user")
public class ArticleUserController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private ArticleService		articleService;
	@Autowired
	private UserService			userService;
	@Autowired
	private NewspaperService	newspaperService;


	// Constructor -----------------------------------------------------------
	public ArticleUserController() {
		super();
	}

	// Create ---------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Article article = this.articleService.create();

		result = this.createEditModelAndView(article);
		return result;
	}

	// Edit  ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int articleId) {
		ModelAndView result;
		final Article article = this.articleService.findOneToEdit(articleId);

		result = this.createEditModelAndView(article);
		return result;
	}

	// Save ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Article article, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(article);
		else
			try {
				this.articleService.save(article);
				result = new ModelAndView("redirect:/article/user/list.do");
			} catch (final Throwable ooops) {
				if (ooops.getMessage() == "article.picture.not.url")
					result = this.createEditModelAndView(article, "article.picture.not.url");
				else
					result = this.createEditModelAndView(article, "article.commit.error");

			}
		return result;
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		final User user = this.userService.findByPrincipal();

		final Collection<Article> articles = this.articleService.findAllByUser(user.getId());

		result = new ModelAndView("article/user/list");
		result.addObject("requestUri", "article/user/list.do");
		result.addObject("articles", articles);
		result.addObject("user", user);
		result.addObject("showCreateFollowup", true);

		return result;
	}

	// Auxiliary methods ----------------------------------------------------
	protected ModelAndView createEditModelAndView(final Article article) {
		final ModelAndView result;
		result = this.createEditModelAndView(article, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Article article, final String message) {
		final ModelAndView result = new ModelAndView("article/user/create");

		final Collection<Newspaper> newspapers = this.newspaperService.findAllNotPublished();

		result.addObject("newspapers", newspapers);
		result.addObject("article", article);
		result.addObject("message", message);
		return result;
	}
}
