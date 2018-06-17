
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "title")
})
public class Advertisement extends DomainEntity {

	private String	title;
	private String	bannerURL;
	private String	targetURL;


	@NotBlank
	@SafeHtml
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	@URL
	@SafeHtml
	public String getBannerURL() {
		return this.bannerURL;
	}

	public void setBannerURL(final String bannerURL) {
		this.bannerURL = bannerURL;
	}

	@NotBlank
	@URL
	@SafeHtml
	public String getTargetURL() {
		return this.targetURL;
	}

	public void setTargetURL(final String targetURL) {
		this.targetURL = targetURL;
	}


	//Relationships

	private Newspaper	newspaper;
	private CreditCard	creditCard;
	private Agent		agent;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Newspaper getNewspaper() {
		return this.newspaper;
	}

	public void setNewspaper(final Newspaper newspaper) {
		this.newspaper = newspaper;
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
	public Agent getAgent() {
		return this.agent;
	}

	public void setAgent(final Agent agent) {
		this.agent = agent;
	}
}
