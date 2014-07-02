package chirp.service.resources;

import java.net.URI;
import java.util.Collection;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chirp.model.Post;
import chirp.model.Timestamp;
import chirp.model.User;
import chirp.model.UserRepository;

@Path("/posts")
public class PostResource {

	private final UserRepository database = UserRepository.getInstance();
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/*
	 * POST /posts/{username}
	 */
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
	
	/*
	 * GET /posts/{username}/{timestamp}
	 */
	@GET
	@Path("{username}/{timestamp}")
	public Post getPost(
				@PathParam("username") String username,
				@PathParam("timestamp") String timestamp
			) {
		logger.info("Searching for a post posted by user with username={} and timestamp={}", username, timestamp);
		Post post = database.getUser(username).getPost(new Timestamp(timestamp));
		logger.info("Found a post: " + post);
		return post;
	}
	
	/*
	 * GET /posts
	 */
	@GET
	public Collection<Post> getPosts() {
		logger.info("Getting all posts.");
		Collection<Post> result = database.getPosts();
		logger.info("Found posts: " + result.size());
		return result;
	}

	/* 
	 * GET /posts/{username}
	 */
	@GET
	@Path("{username}")
	public Collection<Post> getPostsByUser(@PathParam("username") String username) {
		logger.info("Getting all posts.");
		Collection<Post> result = database.getUser(username).getPosts();
		logger.info("Found posts: " + result.size());
		return result;
	}
	
}