
package forms;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

public class NewspaperEditForm {

	//Attributes
	private String	title;
	private String	description;
	private Date	publicationDate;
	private boolean	isPrivate;
	private boolean	isPublicated;
	private String	picture;
	private int		id;


	//Constructors -------------------------

	public NewspaperEditForm() {
		super();
		this.id = 0;
	}

	public void setPrivate(final boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitle() {
		return this.title;
	}
	public void setTitle(final String title) {
		this.title = title;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	public String getDescription() {
		return this.description;
	}
	public void setDescription(final String description) {
		this.description = description;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getPublicationDate() {
		return this.publicationDate;
	}
	public void setPublicationDate(final Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	@NotNull
	public boolean getIsPrivate() {
		return this.isPrivate;
	}
	public void setIsPrivate(final boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	@NotNull
	public boolean getIsPublicated() {
		return this.isPublicated;
	}

	public void setIsPublicated(final boolean isPublicated) {
		this.isPublicated = isPublicated;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	@URL
	public String getPicture() {
		return this.picture;
	}
	public void setPicture(final String picture) {
		this.picture = picture;
	}

}
