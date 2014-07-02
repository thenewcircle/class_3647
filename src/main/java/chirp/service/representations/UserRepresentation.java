package chirp.service.representations;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import chirp.model.User;

@XmlRootElement(name="user")
public class UserRepresentation {
	
	private String username;
	private String realname;
	
	public UserRepresentation() {	
	}
	
	public UserRepresentation(User user, boolean summary) {
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

	public void setUsername(String username) {
		this.username = username;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

}
