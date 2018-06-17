package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Actor;
import domain.Folder;
import domain.MyMessage;
import services.ActorService;
import services.FolderService;
import services.MyMessageService;

@Controller
@RequestMapping("/myMessage")
public class MyMessageController extends AbstractController {
	
	@Autowired
	private MyMessageService myMessageService;

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private FolderService folderService;

	//Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		MyMessage m;
		m = myMessageService.create();
		result = this.createEditModelAndView(m);
		return result;
	}
	
	//Create to move
	@RequestMapping(value = "/move", method = RequestMethod.GET)
	public ModelAndView createMove(@RequestParam int messageId) {
		ModelAndView result;
		MyMessage m;
		Folder f;
		
		f = folderService.getFolderFromMyMessageId(messageId);
		m = myMessageService.findOne(messageId);
		Actor principal = actorService.findByPrincipal();
		Collection<Folder> folders = principal.getFolders();
		result = new ModelAndView("myMessage/move");
		result.addObject("m", m);
		result.addObject("folder", f);
		result.addObject("message", null);
		result.addObject("folders", folders);
		
		return result;

	}
	
	
	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("modelMessage") @Valid MyMessage m, BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(m);

		} else {
			try {

				myMessageService.save(m);
				result = new ModelAndView("redirect:/");

			} catch (Throwable oops) {

				result = createEditModelAndView(m, "ms.commit.error");

			}
		}

		return result;
	}
	
	//Save to move
	@RequestMapping(value = "/saveMove", method = RequestMethod.GET)
	public ModelAndView saveMove(@RequestParam(required=true) int messageId,
			@RequestParam(required=true) int folderId){
		ModelAndView result;
		MyMessage m = myMessageService.findOne(messageId);
		Folder choosedFolder = folderService.findOne(folderId);
		
		try {
			
			myMessageService.saveToMove(m,choosedFolder);
			result = new ModelAndView("redirect:/folder/display.do?folderId="+choosedFolder.getId());

		} catch (Throwable oops) {
			Actor principal = actorService.findByPrincipal();
			Collection<Folder> folders = principal.getFolders();
			result = new ModelAndView("message/move");
			result.addObject("m", m);
			result.addObject("message", "ms.commit.error");
			result.addObject("folders", folders);

			

		}
		
		return result;
	}
	
	//Display
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam int messageId) {

		ModelAndView result;
		MyMessage m;
		Folder folder;

		m = myMessageService.findOne(messageId);
		folder = folderService.getFolderFromMyMessageId(messageId);

		result = new ModelAndView("myMessage/display");
		result.addObject("m", m);
		result.addObject("folder", folder);

		return result;

	}
	
	//Delete
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam int messageId) {
		ModelAndView result;
		MyMessage m;
		
		m = myMessageService.findOne(messageId);
		Folder folder = folderService.getFolderFromMyMessageId(m.getId());
		try {
			this.myMessageService.delete(m);
			result = new ModelAndView("redirect:/folder/display.do?folderId="
					+ folder.getId());
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/myMessage/display.do?messageId="
					+ m.getId());

		}

		return result;

	}
	
	//Ancillary methods
	
	protected ModelAndView createEditModelAndView(final MyMessage m) {
		ModelAndView result;

		result = this.createEditModelAndView(m, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final MyMessage m,
			final String messageCode) {
		ModelAndView result;
		Collection<Actor> actors = actorService.findAll();
		result = new ModelAndView("myMessage/create");
		result.addObject("modelMessage", m);
		result.addObject("message", messageCode);
		result.addObject("actors", actors);
		result.addObject("requestUri", "myMessage/edit.do");

		return result;
	}

	


}
