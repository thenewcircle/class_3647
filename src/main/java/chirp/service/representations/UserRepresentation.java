package chirp.service.representations;

import java.net.URI;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import chirp.model.User;

@XmlRootElement(name="user")
public class UserRepresentation {
	
	private String username;
	private String realname;
	private URI self;
	
	public UserRepresentation() {	
	}
	
	public UserRepresentation(User user, boolean summary, URI self) {
		this.self = self;
		username = user.getUsername();
		realname = summary ? null : user.getRealname();
	}

	@XmlAttribute
	public String getUsername() {
		return username;
	}

	@XmlAttribute
	public String getRealname() {
		return realname;
	}
	
	@XmlAttribute
	public URI getSelf() {
		return self;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public void setSelf(URI self) {
		this.self = self;
	}
}
