
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Volume extends DomainEntity {

	private String title;
	private String description;
	private String year;

	// Relationships
	private Collection<Newspaper> newspapers;
	private User user;

	@NotBlank
	@SafeHtml
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	@SafeHtml
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotBlank
	@SafeHtml
	@Pattern(regexp = "^\\d{4}$")
	public String getYear() {
		return this.year;
	}

	public void setYear(final String year) {
		this.year = year;
	}

	@NotEmpty
	@ManyToMany
	@Valid
	public Collection<Newspaper> getNewspapers() {
		return this.newspapers;
	}

	public void setNewspapers(final Collection<Newspaper> newspapers) {
		this.newspapers = newspapers;
	}

	@NotNull
	@ManyToOne(optional = false)
	@Valid
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
