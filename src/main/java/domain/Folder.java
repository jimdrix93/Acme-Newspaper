
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "name")
})
public class Folder extends DomainEntity {

	private String	name;
	private boolean	systemFolder;


	@NotBlank
	@SafeHtml
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotNull
	public boolean getSystemFolder() {
		return this.systemFolder;
	}

	public void setSystemFolder(final boolean systemFolder) {
		this.systemFolder = systemFolder;
	}


	//Relationships
	private Folder					parentFolder;
	private Collection<MyMessage>	mymessages;


	@ManyToOne(optional = true)
	@Valid
	public Folder getParentFolder() {
		return this.parentFolder;
	}

	public void setParentFolder(final Folder parentFolder) {
		this.parentFolder = parentFolder;
	}

	@ManyToMany
	@Valid
	public Collection<MyMessage> getMymessages() {
		return this.mymessages;
	}

	public void setMymessages(final Collection<MyMessage> mymessages) {
		this.mymessages = mymessages;
	}

}
