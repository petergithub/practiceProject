package demo.frame.hibernate.hsqldb.contacts;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ContactInfo {

	private long id;
	private String firstName;
	private String lastName;
	private PhoneNumber phone;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public PhoneNumber getPhone() {
		return phone;
	}

	public void setPhone(PhoneNumber phone) {
		this.phone = phone;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
