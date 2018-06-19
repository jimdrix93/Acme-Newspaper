
package forms;

import java.util.Map;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import domain.Newspaper;
import domain.Volume;

public class VolumeForm {

	//Attributes
	private String	title;
	private String	description;
	private String	year;
	private Map<Newspaper, Boolean> newspapers;
	private int		id;
	
	//Constructors -------------------------

	public VolumeForm() {
		super();
	}

	public VolumeForm(Volume volume) {
		super();
		this.id = volume.getId();
		this.setTitle(volume.getTitle());
		this.setDescription(volume.getDescription());
		this.setYear(volume.getYear());
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
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

	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	@Pattern(regexp = "^\\d{4}$")
	public String getYear() {
		return this.year;
	}

	public void setYear(final String year) {
		this.year = year;
	}

	public Map<Newspaper, Boolean> getNewspapers() {
		return newspapers;
	}

	public void setNewspapers(Map<Newspaper, Boolean> newspapers) {
		this.newspapers = newspapers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


}
