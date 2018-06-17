
package controllers.administrator;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.TabooWordService;
import controllers.AbstractController;
import domain.Advertisement;
import domain.Article;
import domain.Chirp;
import domain.Newspaper;
import domain.TabooWord;

@Controller
@RequestMapping("/tabooWord/administrator")
public class TabooWordAdminController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private TabooWordService	tabooWordService;


	// Constructor -----------------------------------------------------------
	public TabooWordAdminController() {
		super();
	}

	// Create ---------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final TabooWord tabooWord = this.tabooWordService.create();

		final ModelAndView result = new ModelAndView("tabooWord/administrator/create");

		result.addObject("tabooWord", tabooWord);
		return result;
	}

	// Edit  ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int tabooWordId) {
		ModelAndView result;
		final TabooWord tabooWord = this.tabooWordService.findOne(tabooWordId);

		result = this.createEditModelAndView(tabooWord);
		return result;
	}

	// Save ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final TabooWord tabooWord, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(tabooWord);
		else
			try {
				this.tabooWordService.save(tabooWord);
				result = new ModelAndView("redirect:/tabooWord/administrator/list.do");
			} catch (final Throwable ooops) {
				result = this.createEditModelAndView(tabooWord, "tabooWord.commit.error");
			}
		return result;
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;

		final Collection<TabooWord> tabooWords = this.tabooWordService.findAll();

		result = new ModelAndView("tabooWord/administrator/list");
		result.addObject("tabooWords", tabooWords);

		return result;
	}

	@RequestMapping(value = "/listNewspapers", method = RequestMethod.GET)
	public ModelAndView listNewspapers() {

		ModelAndView result;

		final Collection<Newspaper> newspapers = this.tabooWordService.findAllNewspapersWithTabooWord();

		result = new ModelAndView("tabooWord/administrator/listNewspapers");
		result.addObject("newspapers", newspapers);

		return result;
	}

	@RequestMapping(value = "/listArticles", method = RequestMethod.GET)
	public ModelAndView listArticles() {

		ModelAndView result;

		final Collection<Article> articles = this.tabooWordService.findAllArticlesWithTabooWord();

		result = new ModelAndView("tabooWord/administrator/listArticles");
		result.addObject("articles", articles);

		return result;
	}

	@RequestMapping(value = "/listChirps", method = RequestMethod.GET)
	public ModelAndView listChirps() {

		ModelAndView result;

		final Collection<Chirp> chirps = this.tabooWordService.findAllChirpsWithTabooWord();

		result = new ModelAndView("tabooWord/administrator/listChirps");
		result.addObject("chirps", chirps);

		return result;
	}

	@RequestMapping(value = "/listAdvertisement", method = RequestMethod.GET)
	public ModelAndView listAdvertisement() {

		ModelAndView result;

		final Collection<Advertisement> advertisements = this.tabooWordService.findAllAdvertisementWithTabooWord();
		result = new ModelAndView("tabooWord/administrator/listAdvertisements");
		result.addObject("advertisements", advertisements);

		return result;
	}

	// Auxiliary methods ----------------------------------------------------
	protected ModelAndView createEditModelAndView(final TabooWord tabooWord) {
		final ModelAndView result;
		result = this.createEditModelAndView(tabooWord, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final TabooWord tabooWord, final String message) {
		final ModelAndView result = new ModelAndView("tabooWord/administrator/edit");

		result.addObject("tabooWord", tabooWord);
		result.addObject("message", message);
		return result;
	}
}
