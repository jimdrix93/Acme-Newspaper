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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CustomerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Actor;
import domain.Customer;
import forms.ActorRegisterForm;

@Service
@Transactional
public class CustomerService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private CustomerRepository	customerRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private UserAccountService	userAccountService;
	@Autowired
	private ActorService		actorService;
	@Autowired
	private FolderService folderService;


	// Constructors -----------------------------------------------------------

	public CustomerService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Customer create() {
		UserAccount useraccount;
		final Customer result = new Customer();
		final Authority authority = new Authority();

		authority.setAuthority(Authority.CUSTOMER);
		useraccount = this.userAccountService.createAsCustomer();

		result.setUserAccount(useraccount);

		return result;
	}

	public Collection<Customer> findAll() {
		Collection<Customer> result;

		result = this.customerRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Customer findOne(final int customerId) {
		Customer result;

		result = this.customerRepository.findOne(customerId);
		Assert.notNull(result);

		return result;
	}

	public Customer save(final Customer customer) {
		Assert.notNull(customer);
		Customer result;
		Boolean create = false;

		if (customer.getId() == 0) {
			create = true;
			Md5PasswordEncoder encoder;

			encoder = new Md5PasswordEncoder();

			customer.getUserAccount().setPassword(encoder.encodePassword(customer.getUserAccount().getPassword(), null));
		}
		
		result = this.customerRepository.save(customer);
		if(create) {
			this.folderService.createSystemFolders(result);
		}

		return result;
	}

	public void delete(final Customer customer) {
		Assert.notNull(customer);
		Assert.isTrue(customer.getId() != 0);

		this.customerRepository.delete(customer);
	}

	public void flush() {
		this.customerRepository.flush();
	}

	// Other business methods -------------------------------------------------

	public Customer findByPrincipal() {
		Customer result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		final Actor actor = this.actorService.findByUserAccount(userAccount);
		Assert.isTrue(actor instanceof Customer);
		result = (Customer) actor;
		Assert.notNull(result);

		return result;
	}

	public Customer reconstruct(final ActorRegisterForm customerForm) {
		Customer res;
		if (customerForm.getId() == 0)
			res = this.create();
		else
			res = this.customerRepository.findOne(customerForm.getId());
		

		res.setName(customerForm.getName());
		res.setPhone(customerForm.getPhone());
		res.setSurname(customerForm.getSurname());
		res.setEmail(customerForm.getEmail());
		res.setAddress(customerForm.getAddress());
		res.getUserAccount().setUsername(customerForm.getUsername());
		res.getUserAccount().setPassword(customerForm.getPassword());

		return res;
	}

}
