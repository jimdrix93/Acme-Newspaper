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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Volume;
import services.CustomerService;
import services.NewspaperService;
import services.SubscriptionService;
import services.VolumeService;

@Controller
@RequestMapping("/volume")
public class VolumeController extends AbstractController {

	@Autowired
	NewspaperService	newspaperService;
	
	@Autowired
	VolumeService	volumeService;

	@Autowired
	CustomerService		customerService;

	@Autowired
	SubscriptionService	subscriptionService;


	// Constructors -----------------------------------------------------------

	public VolumeController() {
		super();
	}


	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Volume> volumes = this.volumeService.findAll();

		result = new ModelAndView("volume/list");
		result.addObject("requestUri", "volume/list.do");
		result.addObject("volumes", volumes);

		return result;
	}

	// Display -----------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int volumeId) {
		final ModelAndView result;
		final Volume volume;

		volume = this.volumeService.findOne(volumeId);
		if (volume != null) {
			result = new ModelAndView("volume/display");
			result.addObject("volume", volume);
			result.addObject("newspapers", volume.getNewspapers());
			} else {
			result = new ModelAndView("volume/display");
			result.addObject("message", "user.commit.error");
		}

		return result;
	}

	//Ancillary Methods -----------------------------------------------------------

	

}
