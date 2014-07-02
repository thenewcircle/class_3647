package chirp.service.representations;

import java.net.URI;
import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="posts")
public class PostRepresentationCollection {

	private Collection<PostRepresentation> posts;
	
	// 	@InjectLink(rel="self", value="/posts/{user}", style=Style.ABSOLUTE)
	private URI self;
	
	public PostRepresentationCollection() {
	}

	public PostRepresentationCollection(Collection<PostRepresentation> posts, URI self) {
		this.self = self;
		this.posts = posts;
	}

	public Collection<PostRepresentation> getPosts() {
		return posts;
	}

	public void setPosts(Collection<PostRepresentation> posts) {
		this.posts = posts;
	}

	public URI getSelf() {
		return self;
	}

	public void setSelf(URI self) {
		this.self = self;
	}
		
}
