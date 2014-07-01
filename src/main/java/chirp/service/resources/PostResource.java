package chirp.service.resources;

import java.net.URI;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chirp.model.Post;
import chirp.model.User;
import chirp.model.UserRepository;

@Path("/posts")
public class PostResource {

	private final UserRepository database = UserRepository.getInstance();
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@POST
	@Path("{username}")
	public Response createPost(
			@PathParam("username") String username,
			@FormParam("content") String content) {
		logger.info("Creating a post with username={} and content={}", username, content);
		User user = database.getUser(username);
		Post post = user.createPost(content);
		URI location = UriBuilder
							.fromResource(this.getClass())
							.path(username)
							.path(post.getTimestamp().toString())
							.build();
		return Response.created(location).build();
	}
		
}