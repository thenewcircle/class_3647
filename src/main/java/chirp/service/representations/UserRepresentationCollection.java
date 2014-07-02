package chirp.service.representations;

import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="users")
public class UserRepresentationCollection {

	private Collection<UserRepresentation> users;
	
	public UserRepresentationCollection() {
	}

	public UserRepresentationCollection(Collection<UserRepresentation> users) {
		this.users = users;
	}

	public Collection<UserRepresentation> getUsers() {
		return users;
	}

	public void setUsers(Collection<UserRepresentation> users) {
		this.users = users;
	}
		
}
