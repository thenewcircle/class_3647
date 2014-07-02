package chirp.service.representations;

import java.net.URI;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import chirp.model.Post;

@XmlRootElement(name="post")
public class PostRepresentation {
	
	private String timestamp;
	private String content;
	private String user;	
	private URI self;
	
	public PostRepresentation() {
	}
	
	public PostRepresentation(Post post, boolean summary, URI self) {
		this.self = self;
		timestamp = post.getTimestamp().toString();
		content = summary ? null : post.getContent();
		user = summary ? null : post.getUser().getUsername();
	}

	@XmlAttribute
	public String getTimestamp() {
		return timestamp;
	}
	
	@XmlAttribute
	public String getContent() {
		return content;
	}
	
	@XmlAttribute
	public String getUser() {
		return user;
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setUser(String user) {
		this.user = user;
	}

	public URI getSelf() {
		return self;
	}

	public void setSelf(URI self) {
		this.self = self;
	}
		
}
