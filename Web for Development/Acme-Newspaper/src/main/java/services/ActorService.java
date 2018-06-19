/*
 * ActorService.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Actor;
import forms.ActorRegisterForm;
import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class ActorService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private ActorRepository actorRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private Validator	validator;
	
	// Constructors -----------------------------------------------------------

	public ActorService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Collection<Actor> findAll() {
		Collection<Actor> result;

		result = this.actorRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Actor findOne(final int actorId) {
		Assert.isTrue(actorId != 0);

		Actor result;

		result = this.actorRepository.findOne(actorId);
		Assert.notNull(result);

		return result;
	}

	public Actor save(final Actor actor) {
		Assert.notNull(actor);
		Actor result;
		Assert.isTrue(actor.equals(this.findByPrincipal()));
		
		result = this.actorRepository.save(actor);
		return result;
	}

	public void delete(final Actor actor) {
		Assert.notNull(actor);
		Assert.isTrue(actor.getId() != 0);
		Assert.isTrue(this.actorRepository.exists(actor.getId()));
		Assert.isTrue(actor.equals(this.findByPrincipal()));

		this.actorRepository.delete(actor);
	}

	// Other business methods -------------------------------------------------

	public UserAccount findUserAccount(final Actor actor) {
		Assert.notNull(actor);

		UserAccount result;

		result = actor.getUserAccount();

		return result;
	}

	public Actor findByPrincipal() {
		Actor result = null;
		UserAccount userAccount;

		try {
			userAccount = LoginService.getPrincipal();
			Assert.notNull(userAccount);
			result = this.findByUserAccount(userAccount);
			Assert.notNull(result);

		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public Actor reconstruct(final ActorRegisterForm actorForm, BindingResult binding) {
		Actor res = findByPrincipal();
		Md5PasswordEncoder encoder;
		encoder = new Md5PasswordEncoder();
		String pass = encoder.encodePassword(actorForm.getPassword().split(",")[0], null);
		Assert.isTrue(pass.equals(res.getUserAccount().getPassword()), "profile.wrong.password");
		res.setName(actorForm.getName());
		res.setPhone(actorForm.getPhone());
		res.setSurname(actorForm.getSurname());
		res.setEmail(actorForm.getEmail());
		res.setAddress(actorForm.getAddress());
		res.getUserAccount().setUsername(actorForm.getUsername());		
		this.validator.validate(res, binding);
		
		if(actorForm.getPassword().split(",").length>1) {
			Assert.isTrue(actorForm.getPassword().split(",")[1].length()>4, "profile.userAccount.password.toShort");
			Assert.isTrue(actorForm.getPassword().split(",")[1].length()<33, "profile.userAccount.password.toLong");
			Assert.isTrue(actorForm.getPassword().split(",")[1].equals(actorForm.getPassword().split(",")[2]), "profile.userAccount.repeatPassword.mismatch");
			res.getUserAccount().setPassword(actorForm.getPassword().split(",")[2]);	
			this.validator.validate(res, binding);
			res.getUserAccount().setPassword(encoder.encodePassword(actorForm.getPassword().split(",")[2], null));	
		}
		
		return res;
	}

	public Actor findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		Actor result;

		result = this.actorRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	public String getType(UserAccount userAccount) {

		List<Authority> authorities = new ArrayList<Authority>(userAccount.getAuthorities());

		return authorities.get(0).getAuthority();
	}
}
