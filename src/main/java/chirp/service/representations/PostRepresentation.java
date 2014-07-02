package chirp.service.representations;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import chirp.model.Post;

@XmlRootElement(name="post")
public class PostRepresentation {
	
	private String timestamp;
	private String content;
	
	public PostRepresentation() {
	}
	
	public PostRepresentation(Post post, boolean summary) {
		timestamp = post.getTimestamp().toString();
		content = summary ? null : post.getContent();
	}

	@XmlAttribute
	public String getTimestamp() {
		return timestamp;
	}
	
	@XmlAttribute
	public String getContent() {
		return content;
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
}
