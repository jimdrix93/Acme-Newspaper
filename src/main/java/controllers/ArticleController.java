/*
 * ProfileController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

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

import services.ActorService;
import services.AdvertisementService;
import services.ArticleService;
import services.NewspaperService;
import services.UserService;
import services.VolumeService;
import domain.Actor;
import domain.Advertisement;
import domain.Article;
import domain.Customer;
import domain.Newspaper;
import domain.User;
import domain.Volume;
import forms.SearchForm;

@Controller
@RequestMapping("/article")
public class ArticleController extends AbstractController {

	@Autowired
	ArticleService			articleService;

	@Autowired
	private VolumeService	volumeService;

	@Autowired
	UserService				userService;

	@Autowired
	NewspaperService		newspaperService;

	@Autowired
	ActorService			actorService;

	@Autowired
	AdvertisementService	advertisementService;


	// Constructors -----------------------------------------------------------

	public ArticleController() {
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

		if (binding.hasErrors())
			result = this.createEditModelAndView(searchForm);
		else
			try {

				result = this.searchResult(searchForm.getWord());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(searchForm, "user.commit.error");
			}
		return result;
	}

	// searchResult ---------------------------------------------------------------
	@RequestMapping(value = "/searchResult", method = RequestMethod.GET)
	public ModelAndView searchResult(@RequestParam final String word) {
		ModelAndView result;
		final Collection<Article> articles = this.articleService.findArticleByKeyWord(word);

		result = new ModelAndView("article/searchResult");
		result.addObject("articles", articles);
		result.addObject("requestUri", "article/searchResult.do");
		result.addObject("backSearch", true);

		return result;
	}

	@RequestMapping(value = "/list")
	public ModelAndView list() {
		ModelAndView result;
		Collection<Article> articles;
		articles = this.articleService.findAllPublished();
		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("requestUri", "article/list.do");
		return result;
	}

	// Display article -----------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int articleId) {
		ModelAndView result;
		final Article article;
		final Advertisement advertisement;
		try {
			article = this.articleService.findOneToDisplay(articleId);
			if (article.getNewspaper().getIsPrivate()) {
				final Actor actor = this.actorService.findByPrincipal();
				if (actor instanceof Customer) {
					final Collection<Newspaper> subscribedNewspapers = this.newspaperService.findSubscribedNewspapers(actor.getId());
					for (final Volume volume : this.volumeService.findSubscribedVolumes(actor.getId()))
						subscribedNewspapers.addAll(volume.getNewspapers());
					if (subscribedNewspapers.contains(article.getNewspaper())) {
						result = new ModelAndView("article/display");
						result.addObject("article", article);
					} else {
						result = new ModelAndView("misc/private");
						result.addObject("message", "subscribers.only.alert");

					}
				} else if (actor instanceof User) {
					// TOREVISION
					final Collection<Article> userArticles = this.articleService.findAllByUser(actor.getId());
					if (userArticles.contains(article)) {
						result = new ModelAndView("article/display");
						result.addObject("article", article);
					} else {
						result = new ModelAndView("misc/private");
						result.addObject("message", "subscribers.only.alert");

					}
				} else {
					result = new ModelAndView("misc/private");
					result.addObject("message", "subscribers.only.alert");
				}

			} else {
				result = new ModelAndView("article/display");
				result.addObject("article", article);
			}

			advertisement = this.advertisementService.findByNewspaperId(article.getNewspaper().getId());
			if (advertisement != null) {
				result.addObject("advertisement", advertisement);
				result.addObject("show", true);
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The content is only for subscribers")) {
				final Collection<Newspaper> newspapers = this.newspaperService.findAllPrivate();
				result = this.createEditModelAndView2(newspapers, "article.commit.error.subscribers");
			} else if (oops.getMessage().equals("The content is only for users")) {
				final Collection<Newspaper> newspapers = this.newspaperService.findAllNewspaperPublicated();
				result = this.createEditModelAndView2(newspapers, "article.commit.error.users");
			} else if (oops.getMessage().equals("The content is only for customers")) {
				final Collection<Newspaper> newspapers = this.newspaperService.findAllNewspaperPublicated();
				result = this.createEditModelAndView2(newspapers, "article.commit.error.customers");
			} else {
				final Collection<Newspaper> newspapers = this.newspaperService.findAllPrivate();
				result = this.createEditModelAndView2(newspapers, "article.commit.error");
			}
		}

		return result;
	}

	//Ancillary Methods -----------------------------------------------------------

	protected ModelAndView createEditModelAndView2(final Collection<Newspaper> newspapers) {
		final ModelAndView result;
		result = this.createEditModelAndView2(newspapers, null);
		return result;
	}

	protected ModelAndView createEditModelAndView2(final Collection<Newspaper> newspapers, final String message) {
		ModelAndView result;

		result = new ModelAndView("newspaper/customer/list");
		result.addObject("newspapers", newspapers);
		result.addObject("message", message);

		return result;
	}

	// Ancillary Methods -----------------------------------------------------------

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
