
package forms;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

public class SearchForm {

	private int		id;
	private String	word;


	//Constructors -------------------------

	public SearchForm() {
		super();
		this.id = 0;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getWord() {
		return this.word;
	}

	public void setWord(final String word) {
		this.word = word;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}
}
