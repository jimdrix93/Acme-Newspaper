/*
 * ProfileController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.agent;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.NewspaperService;
import controllers.AbstractController;
import domain.Newspaper;

@Controller
@RequestMapping("/newspaper/agent")
public class NewspaperAgentController extends AbstractController {

	@Autowired
	NewspaperService	newspaperService;


	// Constructors -----------------------------------------------------------

	public NewspaperAgentController() {
		super();
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Newspaper> newspapers = this.newspaperService.findNewspapersWithoutAdvertisements();

		result = new ModelAndView("newspaper/agent/list");
		result.addObject("requestUri", "newspaper/agent/list.do");
		result.addObject("newspapers", newspapers);

		return result;
	}

	// ListAdvertisement ---------------------------------------------------------------
	@RequestMapping(value = "/listAdvertisement", method = RequestMethod.GET)
	public ModelAndView listAdvertisement() {
		ModelAndView result;
		final Collection<Newspaper> newspapers = this.newspaperService.findNewspapersWithAdvertisements();

		result = new ModelAndView("newspaper/agent/listAdvertisement");
		result.addObject("requestUri", "newspaper/agent/listAdvertisement.do");
		result.addObject("newspapers", newspapers);

		return result;
	}

}
