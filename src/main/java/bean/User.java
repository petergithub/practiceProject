package bean;

import java.util.Date;

public class User {

	private Long id;
	private String name;
	private int age;
	private Date creationDate;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Bean [id=").append(id).append(", name=").append(name).append(", age=")
				.append(age).append(", creationDate=").append(creationDate).append("]");
		return builder.toString();
	}

	public User(Long id, String name, int age, Date creationDate) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.creationDate = creationDate;
	}

	public User(Long id, String name, int age) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
	}

	public User() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setValue(int age) {
		this.age = age;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public interface GetBeanCheck {
	}

}