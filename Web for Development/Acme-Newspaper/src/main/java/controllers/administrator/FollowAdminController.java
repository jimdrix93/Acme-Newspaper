
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.UserService;
import controllers.AbstractController;
import domain.User;

@Controller
@RequestMapping("/follow/admin")
public class FollowAdminController extends AbstractController {

	// Supporting services -----------------------------------------------------
	@Autowired
	UserService		userService;
	@Autowired
	ActorService	actorService;


	// Constructors -----------------------------------------------------------

	public FollowAdminController() {
		super();
	}

	// List ------------------------------------------------------------------
	@RequestMapping(value = "/list")
	public ModelAndView list() {
		ModelAndView result;
		final User user = this.userService.findByPrincipal();
		final Collection<User> followeds = user.getFolloweds();

		final Collection<User> users = this.userService.findAll();
		users.remove(user);

		result = new ModelAndView("follow/user/list");
		result.addObject("users", users);
		result.addObject("requestUri", "follow/user/list.do");
		result.addObject("followeds", followeds);

		return result;
	}

	// Follow ------------------------------------------------------------------
	@RequestMapping(value = "/follow", method = RequestMethod.GET)
	public ModelAndView follow(@RequestParam final int userId) {
		ModelAndView result;
		this.userService.follow(userId);
		final Collection<User> followeds = this.userService.findByPrincipal().getFolloweds();

		result = new ModelAndView("redirect:/follow/user/list.do");

		return result;
	}

	// Unfollow ------------------------------------------------------------------
	@RequestMapping(value = "/unfollow", method = RequestMethod.GET)
	public ModelAndView unfollow(@RequestParam final int userId) {
		ModelAndView result;
		this.userService.unfollow(userId);
		final Collection<User> followeds = this.userService.findByPrincipal().getFolloweds();

		result = new ModelAndView("redirect:/follow/user/list.do");

		return result;
	}

	// Followeds ------------------------------------------------------------------
	@RequestMapping(value = "/followeds")
	public ModelAndView followeds() {
		ModelAndView result;
		final User user = this.userService.findByPrincipal();
		final Collection<User> followeds = user.getFolloweds();

		result = new ModelAndView("follow/user/followeds");
		result.addObject("users", followeds);
		result.addObject("requestUri", "follow/user/followeds.do");
		result.addObject("followeds", followeds);

		return result;
	}

	// Followers ------------------------------------------------------------------
	@RequestMapping(value = "/followers")
	public ModelAndView followers() {
		ModelAndView result;
		final User user = this.userService.findByPrincipal();
		Assert.notNull(user);
		final Collection<User> followers = this.userService.findFollowersByUser(user);

		result = new ModelAndView("follow/user/followers");
		result.addObject("users", followers);
		result.addObject("requestUri", "follow/user/followers.do");
		result.addObject("followeds", user.getFolloweds());

		return result;
	}

}
