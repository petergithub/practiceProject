package demo.frame.hibernate.hsqldb.contacts;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PhoneNumber {

	private String countryCode;
	private String areaCode;
	private String number;

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
