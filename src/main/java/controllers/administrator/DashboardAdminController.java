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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.Administrator;
import domain.Newspaper;
import exceptions.HackingException;
import forms.SearchForm;
import services.AdministratorService;
import services.AdvertisementService;
import services.ArticleService;
import services.ChirpService;
import services.FollowupService;
import services.NewspaperService;
import services.SubscriptionService;
import services.UserService;

@Controller
@RequestMapping("/dashboard/administrator")
public class DashboardAdminController extends AbstractController {

	@Autowired
	ArticleService					articleService;
	// Services ---------------------------------------------------------------
	@Autowired
	private NewspaperService		newspaperService;
	@Autowired
	private AdministratorService	administratorService;
	@Autowired
	UserService						userService;
	@Autowired
	FollowupService					followupService;
	@Autowired
	ChirpService					chirpService;
	@Autowired
	SubscriptionService				subscriptionService;
	@Autowired
	private AdvertisementService	advertisementService;


	// Constructors -----------------------------------------------------------

	public DashboardAdminController() {
		super();
	}

	@RequestMapping(value = "/display")
	public ModelAndView create() throws HackingException {
		ModelAndView result;
		result = new ModelAndView("dashboard/display");

		final Administrator admin = this.administratorService.findByPrincipal();
		if (admin == null)
			throw new HackingException();

		// C1 The average and the standard deviation of newspapers created per user.
		final Collection<Object> consultaC1 = this.userService.sumAvgAndCountfNewspPerUser();
		Double c1Stdev = 0.0;
		if (!consultaC1.isEmpty()) {
			final Object[] datos = (Object[]) consultaC1.iterator().next();
			final Double sum = (datos[0] != null) ? ((Long) datos[0]).doubleValue() : 0.0;
			final Double avg = (datos[1] != null) ? (Double) datos[1] : 0.0;
			final Double count = (datos[2] != null) ? ((Long) datos[2]).doubleValue() : 0.0;
			if (count != 0.0)
				c1Stdev = Math.sqrt(sum / count - avg * avg);
		}
		result.addObject("c1Datos", consultaC1);
		result.addObject("c1Stdev", c1Stdev);

		// C2 The average and the standard deviation of articles written by writer.
		final Collection<Object> consultaC2 = this.userService.sumAvgAndCountfArticlesPerUser();
		Double c2Stdev = 0.0;
		if (!consultaC2.isEmpty()) {
			final Object[] datos = (Object[]) consultaC2.iterator().next();
			final Double sum = (datos[0] != null) ? ((Long) datos[0]).doubleValue() : 0.0;
			final Double avg = (datos[1] != null) ? (Double) datos[1] : 0.0;
			final Double count = (datos[2] != null) ? ((Long) datos[2]).doubleValue() : 0.0;
			if (count != 0.0)
				c2Stdev = Math.sqrt(sum / count - avg * avg);
		}
		result.addObject("c2Datos", consultaC2);
		result.addObject("c2Stdev", c2Stdev);

		// C3 The average and the standard deviation of articles per newspaper.
		final Collection<Object> consultaC3 = this.newspaperService.sumAvgAndCountfArticlesPerNewspaper();
		Double c3Stdev = 0.0;
		Double avg = 0.0;
		if (!consultaC3.isEmpty()) {
			final Object[] datos = (Object[]) consultaC3.iterator().next();
			final Double sum = (datos[0] != null) ? ((Long) datos[0]).doubleValue() : 0.0;
			avg = (datos[1] != null) ? (Double) datos[1] : 0.0;
			final Double count = (datos[2] != null) ? ((Long) datos[2]).doubleValue() : 0.0;
			if (count != 0.0)
				c3Stdev = Math.sqrt(sum / count - avg * avg);
		}
		result.addObject("c3Datos", consultaC3);
		result.addObject("c3Stdev", c3Stdev);

		// C4 The newspapers that have at least 10% more articles than the average.
		final int min = ((Double) (1.1 * avg)).intValue();
		final Collection<Newspaper> consultaC4 = this.newspaperService.findNewspapersOverAvgOfArticles(1.1 * avg);

		result.addObject("newspapersC4", consultaC4);
		result.addObject("avg", avg);
		result.addObject("rasero1", 1.1 * avg);

		// C5 The newspapers that have at least 10% less articles than the average.
		final int max = ((Double) (0.9 * avg)).intValue();
		final Collection<Newspaper> consultaC5 = this.newspaperService.findNewspapersUnderAvgOfArticles(0.9 * avg);

		result.addObject("newspapersC5", consultaC5);
		result.addObject("rasero2", 0.9 * avg);

		// C6 The ratio of users who have ever created a newspaper
		final Collection<Object> consultaC6 = this.userService.countsForRatioOfNewspaperWriters();
		Double ratioEditors = 0.0;
		if (!consultaC6.isEmpty()) {
			final Object[] datos = (Object[]) consultaC6.iterator().next();
			final Double count = (datos[0] != null) ? ((Long) datos[0]).doubleValue() : 0.0;
			final Double all = (datos[1] != null) ? ((Long) datos[1]).doubleValue() : 0.0;
			if (all != 0.0)
				ratioEditors = count / all;
		}
		result.addObject("ratioEditors", ratioEditors);

		// C7 The ratio of users who have ever written an article.
		final Collection<Object> consultaC7 = this.userService.countsForRatioOfArticleWriters();
		Double ratioWriters = 0.0;
		if (!consultaC7.isEmpty()) {
			final Object[] datos = (Object[]) consultaC7.iterator().next();
			final Double count = (datos[0] != null) ? ((Long) datos[0]).doubleValue() : 0.0;
			final Double all = (datos[1] != null) ? ((Long) datos[1]).doubleValue() : 0.0;
			if (all != 0.0)
				ratioWriters = count / all;
		}
		result.addObject("ratioWriters", ratioWriters);

		// N2.0 C3 The ratio of newspapers that have at least one advertisement versus the newspapers that haven't any.
		final Double ratioNewspaper = this.newspaperService.ratioAtLeastOneAdvertisement();
		result.addObject("ratioNewspaper", ratioNewspaper);

		// N2.0 C3 The ratio of advertisements that have taboo words.
		final Double ratioTabooAdvertisements = this.advertisementService.ratioAdvertisementWithTabooWords();
		result.addObject("ratioTabooAdvertisements", ratioTabooAdvertisements);

		// B1 The average number of follow-ups per article
		final Double b1 = this.followupService.avgFolloupsPerArticle();
		result.addObject("b1", b1);

		// B2 The average number of follow-ups per article up to one week after the cor-
		// responding newspapers been published.
		final Double b2 = this.articleService.findAvgFollowupPerArticle1Week();
		result.addObject("b2", b2);

		// B3 The average number of follow-ups per article up to two week after the cor-
		// responding newspapers been published.
		final Double b3 = this.articleService.findAvgFollowupPerArticle2Week();
		result.addObject("b3", b3);

		// B4 The average and the standard deviation of the number of chirps per user
		final Collection<Object> b4 = this.chirpService.avgAndStdevOfChirpsPerUser();
		result.addObject("b4", b4);

		// B5 The ratio of users who have posted above 75% the average number of chirps
		// per user
		final Double b5 = this.userService.ratioOfUsersAbove75ChirpsPerUser();
		result.addObject("b5", b5);

		// N2.0 B The average number of newspapers per volume.
		final Double avgNewspaperPerVolume = this.newspaperService.avgOfNewspaperPerVolume();
		result.addObject("avgNewspaperPerVolume", avgNewspaperPerVolume);

		// N2.0 B The ratio of subscriptions to volumes versus subscriptions to newspapers
		final Double ratioSubscriptionsVSubscriptions = this.subscriptionService.ratioSubscriptionsVSubcriptions();
		result.addObject("ratioSubscriptionsVSubscriptions", ratioSubscriptionsVSubscriptions);

		// A Dashboard
		//	A1	The ratio of public versus private newspapers.
		final Collection<Object> consultaA1 = this.newspaperService.countsForRatioOfPublicNewspapers();
		Double ratioPublic = 0.0;
		Double count = 0.0;
		Double all = 0.0;
		if (!consultaA1.isEmpty()) {
			final Object[] datos = (Object[]) consultaA1.iterator().next();
			count = (datos[0] != null) ? ((Long) datos[0]).doubleValue() : 0.0;
			all = (datos[1] != null) ? ((Long) datos[1]).doubleValue() : 0.0;
			if (all != 0.0)
				ratioPublic = count / all;
		}
		result.addObject("consultaA1", consultaA1);
		result.addObject("ratioPublic", ratioPublic);
		result.addObject("publico", count);
		result.addObject("privado", all - count);

		//	A2	The average number of articles per private newspapers.
		final Double avgA2 = this.newspaperService.avgOfArticleInPrivateNewspapers();
		result.addObject("avgA2", avgA2);

		//	A3	The average number of articles per public newspapers.
		final Double avgA3 = this.newspaperService.avgOfArticleInPublicNewspapers();
		result.addObject("avgA3", avgA3);

		//	A4	The ratio of subscribers per private newspaper versus the total number of customers.
		final Collection<Object> consultaA4 = this.subscriptionService.countsForRatioOfPrivateSubscribers();
		Double ratioSubscribers = 0.0;
		Double countSubscribers = 0.0;
		Double allSubscribers = 0.0;
		if (!consultaA4.isEmpty()) {
			final Object[] datos = (Object[]) consultaA4.iterator().next();
			countSubscribers = (datos[0] != null) ? ((Long) datos[0]).doubleValue() : 0.0;
			allSubscribers = (datos[1] != null) ? ((Long) datos[1]).doubleValue() : 0.0;
			if (allSubscribers != 0.0)
				ratioSubscribers = countSubscribers / allSubscribers;
		}
		result.addObject("consultaA4", consultaA4);
		result.addObject("ratioSubscribers", ratioSubscribers);
		result.addObject("countSubscribers", countSubscribers);
		result.addObject("allSubscribers", allSubscribers);

		//	A5	The average ratio of private versus public newspapers per publisher.
		final Double avgA5 = this.newspaperService.avgRatioOfPrivateNewspapersPerPublisher();
		result.addObject("avgA5", avgA5);

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
