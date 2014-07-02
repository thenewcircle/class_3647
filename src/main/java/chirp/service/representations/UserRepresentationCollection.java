package chirp.service.representations;

import java.net.URI;
import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="users")
public class UserRepresentationCollection {

	private Collection<UserRepresentation> users;
	private URI self;
	
	public UserRepresentationCollection() {
	}

	public UserRepresentationCollection(Collection<UserRepresentation> users, URI self) {
		this.self = self;
		this.users = users;
	}

	public Collection<UserRepresentation> getUsers() {
		return users;
	}

	public void setUsers(Collection<UserRepresentation> users) {
		this.users = users;
	}

	public URI getSelf() {
		return self;
	}

	public void setSelf(URI self) {
		this.self = self;
	}
		
}
