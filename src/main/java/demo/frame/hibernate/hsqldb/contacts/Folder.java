package demo.frame.hibernate.hsqldb.contacts;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Folder {
	private long id;
	private String folderName;
	private Set<Folder> folders = new HashSet<Folder>();
	private Set<ContactInfo> contacts = new HashSet<ContactInfo>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public Set<Folder> getFolders() {
		return folders;
	}

	public void setFolders(Set<Folder> folders) {
		this.folders = folders;
	}

	public void addFolder(Folder folder) {
		getFolders().add(folder);
	}

	public void removeFolder(Folder folder) {
		getFolders().remove(folder);
	}

	public Set<ContactInfo> getContacts() {
		return contacts;
	}

	public void setContacts(Set<ContactInfo> contacts) {
		this.contacts = contacts;
	}

	public void addContact(ContactInfo contact) {
		getContacts().add(contact);
	}

	public void removeContact(ContactInfo contact) {
		getContacts().remove(contact);
	}

	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
