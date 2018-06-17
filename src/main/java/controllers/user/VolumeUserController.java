
package controllers.user;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.Actor;
import domain.Newspaper;
import domain.User;
import domain.Volume;
import exceptions.HackingException;
import forms.VolumeForm;
import services.ActorService;
import services.NewspaperService;
import services.UserService;
import services.VolumeService;

@Controller
@RequestMapping("/volume/user")
public class VolumeUserController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private VolumeService		volumeService;
	@Autowired
	private UserService			userService;
	@Autowired
	private ActorService		actorService;
	@Autowired
	private NewspaperService	newspapersService;

	// Constructor -----------------------------------------------------------
	public VolumeUserController() {
		super();
	}

	// List ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final User user = this.userService.findByPrincipal();
		final Collection<Volume> volumes = this.volumeService.findAllByUser(user.getId());

		result = new ModelAndView("volume/list");
		result.addObject("requestUri", "volume/user/list.do");
		result.addObject("volumes", volumes);
		result.addObject("showEdit", true);

		return result;
	}

	// Create ---------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Actor actor = this.actorService.findByPrincipal();
		Assert.isTrue(actor instanceof User);
		final VolumeForm volume = new VolumeForm();
		volume.setId(0);
		final Collection<Newspaper> newspapers = this.newspapersService.findAllNewspaperPublicated();
		final Map<Newspaper, Boolean> volumeNewspapers = new TreeMap<>();
		for (final Newspaper newspaper : newspapers)
			volumeNewspapers.put(newspaper, null);
		volume.setNewspapers(volumeNewspapers);
		result = new ModelAndView("volume/user/create");
		result.addObject("volumeForm", volume);
		result.addObject("requestUri", "volume/user/create.do");

		return result;
	}

	// Edit ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(Integer volumeId) throws HackingException{
		ModelAndView result;
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);			
		try {			
			Assert.isTrue(actor instanceof User);			
		} catch (final Throwable oops) {
			throw new HackingException(oops);
		}
		final Collection<Volume> volumes = this.volumeService.findAllByUser(actor.getId());
		final Volume volume = this.volumeService.findOne(volumeId);
		try {			
			Assert.isTrue(volumes.contains(volume));					
		} catch (final Throwable oops) {
			throw new HackingException(oops);
		}
		final VolumeForm volumeForm = new VolumeForm(volume);
		final Collection<Newspaper> newspapers = this.newspapersService.findAllNewspaperPublicated();
		final Map<Newspaper, Boolean> volumeNewspapers = new TreeMap<>();
		for (Newspaper newspaper : newspapers)
			volumeNewspapers.put(newspaper, volume.getNewspapers().contains(newspaper));
		volumeForm.setNewspapers(volumeNewspapers);
		result = new ModelAndView("volume/user/create");
		result.addObject("volumeForm", volumeForm);
		result.addObject("requestUri", "volume/user/create.do");

		return result;
	}

	// AddNewspaper ---------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "addNewspaper")
	public ModelAndView addNewspaper(final VolumeForm volumeForm) {
		ModelAndView result;
		final Collection<Newspaper> newspapers = this.newspapersService.findAllNewspaperPublicated();
		final Map<Newspaper, Boolean> volumeNewspapers = new TreeMap<>();
		for (Newspaper newspaper : newspapers)
				volumeNewspapers.put(newspaper, volumeForm.getNewspapers().get(newspaper));
		volumeForm.setNewspapers(volumeNewspapers);
		result = this.createEditModelAndView(volumeForm);

		return result;
	}

	// Save ---------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final VolumeForm volumeForm, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(volumeForm);
		else
			try {
				final Volume volumen = this.volumeService.reconstruct(volumeForm, binding);
				this.volumeService.save(volumen);
				result = new ModelAndView("redirect:/volume/user/list.do");
			} catch (final Throwable ooops) {
				if (ooops.getMessage().startsWith("volume"))
					result = this.createEditModelAndView(volumeForm, "volume.newspapers.empty");
				else
					result = this.createEditModelAndView(volumeForm, "newspaper.commit.error");
			}
		return result;
	}

	// Auxiliary methods ----------------------------------------------------
	protected ModelAndView createEditModelAndView(final Volume volume) {
		final ModelAndView result;
		result = this.createEditModelAndView(volume, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Volume volume, final String message) {
		final ModelAndView result = new ModelAndView("volume/user/create");
		result.addObject("volume", volume);
		result.addObject("message", message);
		return result;
	}

	// Forms ----------------------------------------------------------------

	// Edit ---------------------------------------------------------------

	// Save ---------------------------------------------------------------

	// Delete ---------------------------------------------------------------

	// Auxiliary methods ----------------------------------------------------
	protected ModelAndView createEditModelAndView(final VolumeForm volume) {
		final ModelAndView result;
		result = this.createEditModelAndView(volume, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final VolumeForm volume, final String message) {
		final ModelAndView result = new ModelAndView("volume/user/create");
		result.addObject("volumeForm", volume);
		result.addObject("message", message);
		return result;
	}

}
