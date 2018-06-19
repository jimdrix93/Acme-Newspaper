
package services;

import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import services.ArticleService;
import services.TabooWordService;
import services.UserService;
import utilities.AbstractTest;
import domain.Article;
import domain.User;
import security.UserAccount;
import security.UserAccountService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class UserServiceTest extends AbstractTest {


	@Autowired
	private ArticleService	articleService;
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private UserService userService;
	@Autowired
	TabooWordService		tabooWordService;

	
	/*
	 * Requerimientos:
	 * 1. The system must support two kinds of actors, namely: administrators and users. It must
	 * store the following information regarding them: their names, surnames, postal addresses
	 * (optional), phone numbers (optional), and email addresses. Phone numbers are sequences
	 * of digits that may optionally start with a +.
	 * 4. An actor who is not authenticated must be able to:
	 * 		1. Register to the system as a user.
	 * 		3. List the users of the system and display their profiles, which must include their per-
	 * 		   sonal data and the list of articles that they have written as long as they are published
	 * 		   in a newspaper.
	 * 16. An actor who is authenticated as a user must be able to:
	 * 		2. Follow or unfollow another user.
	 * 		3. List the users who he or she follows.
	 * 		4. List the users who follow him or her.
	 */

	/*
	 * AGF
	 * Caso de uso:
	 * Usuario anónimo registrandose en el sistema
	 */
	@Test
	public void createUserTest() {
		
		//System.out.println("-----createUserTest. Positive 0 to 1, Negative 2 to 3");
		
		final Object testingData[][] = {
			{// Positive
				"Pedro", "Dominguez Lopez", "652956526", "test@gmail.com", "Address Test", true, "Username1", "Password1", null
			}, {//Positive
				"Amaia", "Fernandez Rodriguezáéíóú", "652956526", "test@gmail.com", "Address Test", false, "Username2", "Password2", null
			}, {// Negative: without name
				"", "Surname Test", "652956526", "emailTest", "Address Test", false, "Username3", "Password3", ConstraintViolationException.class
			}, {// Negative: with name null
				null, "Surname Test", "652956526", "emailTest", "Address Test", false, "Username4", "Password4", DataIntegrityViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateUser(
					i,
					(String) testingData[i][0], 
					(String) testingData[i][1], 
					(String) testingData[i][2], 
					(String) testingData[i][3], 
					(String) testingData[i][4], 
					(Boolean) testingData[i][5], 
					(String) testingData[i][6],
					(String) testingData[i][7], (Class<?>) testingData[i][8]);
	}


	protected void templateCreateUser(
			Integer i, 
			final String name, 
			final String surname, 
			final String phone, 
			final String email, 
			final String address, 
			final Boolean adult, 
			final String username,
			final String password, 
			final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			final UserAccount ua = this.userAccountService.createAsUser();
			ua.setUsername(username);
			ua.setPassword(password);

			final User user = this.userService.create();

			user.setAddress(address);
			user.setEmail(email);
			user.setName(name);
			user.setSurname(surname);
			user.setUserAccount(ua);

			User saved = this.userService.save(user);
			this.userService.flush();
			
			final int userId = saved.getId(); 
			Assert.isTrue(this.userService.findOne(userId) != null);
			
			//System.out.println(i.toString() + " ok ");
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//System.out.println(i.toString() + oops.getMessage());
		}
		super.checkExceptions(expected, caught);
	}
	

	
	/*
	 * Caso de uso:
	 * AGF 
	 * Listar los usuarios del sistema y mostrar sus perfiles(incluyen sus datos personales y los artículos 
	 * que han escrito y que están publicados en un newspaper)
	 */
	@Test
	public void anonymousUserLookingUsersProfilesTest() {
		
		//System.out.println("-----anonymousUserLookingUsersProfilesTest. Positive 0, Negative 1 to 2");	

		final Object testingData[][] = {
			//Positive cases
			{"user1", null},

			//Negative cases
			//Whitout user
			{"user123123123", NumberFormatException.class},
			//With user with no articles
			{"user4", IllegalArgumentException.class}
		};

		Class<?> caught = null;
		for (Integer i = 0; i < testingData.length; i++) {
			caught = null;

			try {
				super.unauthenticate();
				final Collection<User> users = this.userService.findAll();
				
				for (User user : users) {
					if (user.getId() == this.getEntityId((String) testingData[i][0])) {
						final Collection<Article> articles = this.articleService.findAllByUser(user.getId());
						
						Assert.isTrue(user.getName() != null);
						Assert.isTrue(articles.size() != 0);
						
					}
				}
				
				//System.out.println(i.toString() + " ok ");
			} catch (final Throwable oops) {
				caught = oops.getClass();
				//System.out.println(i.toString() + oops.getMessage());
			}

			super.checkExceptions((Class<?>) testingData[i][1], caught);
		}
	}

	
	/*
	 * AGF
	 * Caso de uso: 
	 * Usuario siguiendo a otro usuario
	 */
	@Test
	public void userFollowUserTest() {
		
		//System.out.println("-----userFollowUserTest. Positive 0, Negative 1 to 2");			

		final Object testingData[][] = {
			//Positive cases
			{"user1", "user4", null	},

			//Negative cases
			// Usuario siguiendo a usuario sin identificar
			{"user1", "", NumberFormatException.class	},
			// Usuario siguiendo a un usuario no existente
			{"user1", "userABC", NumberFormatException.class	}
		};

		Class<?> caught = null;
		for (Integer i = 0; i < testingData.length; i++) {
			caught = null;

			try {
				super.authenticate((String) testingData[i][0]);
				final User user = this.userService.findByPrincipal();
				final User userToFollow = this.userService.findOne(this.getEntityId((String) testingData[i][1]));
				
				this.userService.follow(userToFollow.getId());
				this.userService.flush();
				Assert.isTrue(user.getFolloweds().contains(userToFollow));
				
				super.unauthenticate();
				
				//System.out.println(i.toString() + " ok ");
			} catch (final Throwable oops) {
				caught = oops.getClass();
				//System.out.println(i.toString() + oops.getMessage());
			}

			this.checkExceptions((Class<?>) testingData[i][2], caught);
		}
	}
	
	
	/*
	 * AGF
	 * Caso de uso: 
	 * Usuario listando usuarios a los que sigue
	 */
	@Test
	public void userListFollowedUsersTest() {
		
		//System.out.println("-----userListFollowedUsersTest. Positive 0, Negative 1 to 2");					

		final Object testingData[][] = {
			//Positive cases
			{"user1", null},

			//Negative cases
			//Sin usuario
			{"", IllegalArgumentException.class},
			//Con usuario que no sigue a nadie
			{"user4", IllegalArgumentException.class}
			
		};

		Class<?> caught = null;
		for (Integer i = 0; i < testingData.length; i++) {
			caught = null;

			try {
				super.authenticate((String) testingData[i][0]);
				final User user = this.userService.findByPrincipal();
				final Collection<User> users = user.getFolloweds();
				
				Assert.isTrue(users.size() != 0);
				super.unauthenticate();
								
				//System.out.println(i.toString() + " ok ");
			} catch (final Throwable oops) {
				caught = oops.getClass();
				//System.out.println(i.toString() + oops.getMessage());
			}

			this.checkExceptions((Class<?>) testingData[i][1], caught);
		}
	}
	
	/*
	 * AGF
	 * Caso de uso: 
	 * Usuario listando usuarios seguidores
	 */
	@Test
	public void userListFollowersUsersTest() {
		
		//System.out.println("-----userListFollowersUsersTest. Positive 0, Negative 1 to 2");			

		final Object testingData[][] = {
			//Positive cases
			{"user1", null	},

			//Negative cases
			//Sin usuario
			{"", IllegalArgumentException.class},
			//Con usuario al que no le sigue nadie
			{"user4", IllegalArgumentException.class}		
		};

		Class<?> caught = null;
		for (Integer i = 0; i < testingData.length; i++) {
			caught = null;

			try {
				super.authenticate((String) testingData[i][0]);
				final User user = this.userService.findByPrincipal();
				final Collection<User> users = this.userService.findFollowersByUser(user);
				
				Assert.isTrue(users.size() != 0);
				super.unauthenticate();
								
				//System.out.println(i.toString() + " ok ");
			} catch (final Throwable oops) {
				caught = oops.getClass();
				//System.out.println(i.toString() + oops.getMessage());
			}

			this.checkExceptions((Class<?>) testingData[i][1], caught);
		}
	}
	
}