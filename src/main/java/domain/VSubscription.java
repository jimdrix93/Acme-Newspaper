
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class VSubscription extends DomainEntity {

	//Relationships
	private Customer	customer;
	private CreditCard	creditCard;
	private Volume		volume;


	@Valid
	@ManyToOne(optional = false)
	@NotNull
	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(final Customer customer) {
		this.customer = customer;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Volume getVolume() {
		return this.volume;
	}

	public void setVolume(final Volume volume) {
		this.volume = volume;
	}

}
