/*
 * ProfileController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.user;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ChirpService;
import services.UserService;
import controllers.AbstractController;
import domain.Chirp;
import domain.User;

@Controller
@RequestMapping("/chirp/user")
public class ChirpUserController extends AbstractController {

	@Autowired
	ChirpService	chirpService;
	@Autowired
	UserService		userService;


	// Constructors -----------------------------------------------------------

	public ChirpUserController() {
		super();
	}

	// Create ------------------------------------------------------------------
	@RequestMapping(value = "/create")
	public ModelAndView create() {
		ModelAndView result;
		final Chirp chirp = this.chirpService.create();

		result = this.createEditModelAndView(chirp);

		return result;
	}

	// Create chirp ---------------------------------------------------------------	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Chirp chirp, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(chirp);
		else
			try {
				this.chirpService.save(chirp);
				result = new ModelAndView("redirect:/chirp/user/written.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(chirp, "chirp.commit.error");
			}
		return result;
	}

	// Written ------------------------------------------------------------------
	@RequestMapping(value = "/written")
	public ModelAndView written() {
		ModelAndView result;
		final Collection<Chirp> chirps = this.chirpService.findAllByPrincipal();

		result = new ModelAndView("chirp/user/written");
		result.addObject("chirps", chirps);
		result.addObject("requestUri", "chirp/user/written.do");

		return result;
	}

	// Timeline ------------------------------------------------------------------
	@RequestMapping(value = "/timeline")
	public ModelAndView timeline() {
		final ModelAndView result;
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);
		final Collection<User> followeds = user.getFolloweds();

		result = new ModelAndView("chirp/user/timeline");
		result.addObject("followeds", followeds);
		result.addObject("requestUri", "chirp/user/timeline.do");

		return result;
	}
	//Ancillary Methods -----------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Chirp chirp) {
		final ModelAndView result;
		result = this.createEditModelAndView(chirp, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Chirp chirp, final String message) {
		ModelAndView result;

		result = new ModelAndView("chirp/user/create");
		result.addObject("chirp", chirp);

		return result;
	}

}
