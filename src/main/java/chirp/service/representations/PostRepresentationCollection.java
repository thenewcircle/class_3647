package chirp.service.representations;

import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="posts")
public class PostRepresentationCollection {

	private Collection<PostRepresentation> posts;
	
	public PostRepresentationCollection() {
	}

	public PostRepresentationCollection(Collection<PostRepresentation> posts) {
		this.posts = posts;
	}

	public Collection<PostRepresentation> getPosts() {
		return posts;
	}

	public void setPosts(Collection<PostRepresentation> posts) {
		this.posts = posts;
	}
		
}
