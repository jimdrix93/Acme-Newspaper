/*
 * ManagerService.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package services;

import java.util.Collection;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AgentRepository;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Actor;
import domain.Agent;
import domain.Folder;
import forms.ActorRegisterForm;

@Service
@Transactional
public class AgentService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private AgentRepository		agentRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private UserAccountService	userAccountService;
	@Autowired
	private ActorService		actorService;
	@Autowired
	private FolderService		folderService;


	// Constructors -----------------------------------------------------------

	public AgentService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Agent create() {
		UserAccount useraccount;
		final Agent result = new Agent();

		useraccount = this.userAccountService.createAsAgent();

		final Collection<Folder> folders = new LinkedList<Folder>();
		result.setUserAccount(useraccount);
		result.setFolders(folders);

		return result;
	}

	public Collection<Agent> findAll() {
		return this.agentRepository.findAll();
	}

	public Agent findOne(final int agentId) {
		final Agent result = this.agentRepository.findOne(agentId);
		Assert.notNull(result);

		return result;
	}

	public Agent save(final Agent agent) {
		Assert.notNull(agent);
		Agent result;
		Boolean create = false;

		if (agent.getId() == 0) {
			create = true;
			Md5PasswordEncoder encoder;

			encoder = new Md5PasswordEncoder();

			agent.getUserAccount().setPassword(encoder.encodePassword(agent.getUserAccount().getPassword(), null));
		}

		result = this.agentRepository.save(agent);

		if (create)
			this.folderService.createSystemFolders(result);

		return result;
	}
	public void delete(final Agent agent) {
		Assert.isTrue(agent.getId() != 0);

		this.agentRepository.delete(agent);
	}

	public void flush() {
		this.agentRepository.flush();
	}

	// Other business methods -------------------------------------------------

	public Agent findByPrincipal() {
		Agent result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findByUserAccount(userAccount);
		Assert.isTrue(actor instanceof Agent);
		result = (Agent) actor;
		Assert.notNull(result);

		return result;
	}

	public Agent reconstruct(final ActorRegisterForm agentForm) {
		Agent res;
		if (agentForm.getId() == 0)
			res = this.create();
		else
			res = this.agentRepository.findOne(agentForm.getId());

		res.setName(agentForm.getName());
		res.setPhone(agentForm.getPhone());
		res.setSurname(agentForm.getSurname());
		res.setEmail(agentForm.getEmail());
		res.setAddress(agentForm.getAddress());
		res.getUserAccount().setUsername(agentForm.getUsername());
		res.getUserAccount().setPassword(agentForm.getPassword());

		return res;
	}

}
