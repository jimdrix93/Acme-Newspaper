/*
 * ProfileController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

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
import services.ActorService;
import services.ArticleService;
import services.NewspaperService;
import services.UserService;
import domain.Actor;
import domain.Administrator;
import domain.Article;
import domain.Newspaper;
import exceptions.HackingException;
import forms.SearchForm;

@Controller
@RequestMapping("/article/administrator")
public class ArticleAdminController extends AbstractController {

	@Autowired
	ArticleService	articleService;
	// Services ---------------------------------------------------------------
		@Autowired
		private NewspaperService newspaperService;
		@Autowired
		private ActorService actorService;


	// Constructors -----------------------------------------------------------

	public ArticleAdminController() {
		super();
	}

	@RequestMapping(value = "/search")
	public ModelAndView create() {
		ModelAndView result;
		final SearchForm searchForm = new SearchForm();

		result = this.createEditModelAndView(searchForm);

		return result;
	}
	@RequestMapping(value = "/search", method = RequestMethod.POST, params = "searchButton")
	public ModelAndView search(@Valid final SearchForm searchForm, final BindingResult binding) {
		ModelAndView result;
		Collection<Article> articles;

		if (binding.hasErrors())
			result = this.createEditModelAndView(searchForm);
		else
			try {
				articles = this.articleService.findArticleByKeyWord(searchForm.getWord());

				result = new ModelAndView("article/searchResult");
				result.addObject("articles", articles);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(searchForm, "user.commit.error");
			}
		return result;
	}

	// Display article -----------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int articleId) {
		final ModelAndView result;
		final Article article;

		article = this.articleService.findOne(articleId);

		result = new ModelAndView("article/display");
		result.addObject("article", article);

		return result;
	}
	
	// Delete ---------------------------------------------------------------
		@RequestMapping(value = "/delete", method = RequestMethod.GET)
		public ModelAndView delete(@RequestParam(required = false) Integer id) throws HackingException {
			ModelAndView result;
			Actor actor = actorService.findByPrincipal();
			try {
				Assert.notNull(actor);
				Assert.isTrue(actor instanceof Administrator);
			} catch (Throwable oops) {
				throw new HackingException(oops);
			}
			try {
				Assert.notNull(id);
			} catch (final Throwable ooops) {
				result =  new ModelAndView("mis/oops");
				result.addObject("message", "assert.error.cause.nullId");

			}
			Article articulo = articleService.findOne(id);
			Assert.notNull(articulo);
			try {
				this.articleService.delete(articulo);			 
				result = new ModelAndView("redirect:/article/list.do");
				result.addObject("requestUri", "article/list.do");
			} catch (final Throwable ooops) {
				result = new ModelAndView("article/display");
				result.addObject("newspaper", articulo);
				result.addObject("message", "newspaper.commit.error");
			}
			return result;
		}


	//Ancillary Methods -----------------------------------------------------------

	protected ModelAndView createEditModelAndView(final SearchForm searchForm) {
		final ModelAndView result;
		result = this.createEditModelAndView(searchForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final SearchForm searchForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("article/search");
		result.addObject("searchForm", searchForm);

		return result;
	}

}
