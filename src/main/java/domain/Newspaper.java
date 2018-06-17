
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "title, description, publicationDate")
})
public class Newspaper extends DomainEntity implements Comparable<Newspaper> {

	//Attributes
	private String				title;
	private String				description;
	private Date				publicationDate;
	private boolean				isPrivate;
	private boolean				isPublicated;
	private String				picture;
	//Relationships
	private Collection<Article>	articles;


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
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
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
	@URL
	@SafeHtml
	public String getPicture() {
		return this.picture;
	}
	public void setPicture(final String picture) {
		this.picture = picture;
	}

	@Valid
	@OneToMany(mappedBy = "newspaper")
	public Collection<Article> getArticles() {
		return this.articles;
	}
	public void setArticles(final Collection<Article> articles) {
		this.articles = articles;
	}
	@Override
	public int compareTo(Newspaper otroNewspaper) {
		int res = this.getTitle().compareTo(otroNewspaper.getTitle());
		if(res==0) {
			res = this.getId() - otroNewspaper.getId();
		}
		return res;
	}

}
