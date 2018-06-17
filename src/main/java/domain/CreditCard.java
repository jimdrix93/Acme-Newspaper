
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "number, cvv")
})
public class CreditCard extends DomainEntity {

	//Attributes
	private String	holderName;
	private String	brandName;
	private String	number;
	private Integer	expirationMonth;
	private Integer	expirationYear;
	private Integer	cvv;


	@NotBlank
	@SafeHtml
	public String getHolderName() {
		return this.holderName;
	}
	public void setHolderName(final String holderName) {
		this.holderName = holderName;
	}
	@NotBlank
	@SafeHtml
	public String getBrandName() {
		return this.brandName;
	}
	public void setBrandName(final String brandName) {
		this.brandName = brandName;
	}
	@CreditCardNumber
	@SafeHtml
	@Pattern(regexp = "^\\d{4}\\s?\\d{4}\\s?\\d{4}\\s?\\d{4}$")
	public String getNumber() {
		return this.number;
	}
	public void setNumber(final String number) {
		this.number = number;
	}
	@Range(min = 1, max = 12)
	@NotNull
	public Integer getExpirationMonth() {
		return this.expirationMonth;
	}
	public void setExpirationMonth(final Integer expirationMonth) {
		this.expirationMonth = expirationMonth;
	}
	@Range(min = 18, max = 99)
	@NotNull
	public Integer getExpirationYear() {
		return this.expirationYear;
	}
	public void setExpirationYear(final Integer expirationYear) {
		this.expirationYear = expirationYear;
	}
	@Range(min = 100, max = 999)
	@NotNull
	public Integer getCvv() {
		return this.cvv;
	}
	public void setCvv(final Integer cvv) {
		this.cvv = cvv;
	}

}
