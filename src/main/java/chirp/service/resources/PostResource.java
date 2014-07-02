package chirp.service.resources;

import java.net.URI;
import java.util.Collection;
import java.util.LinkedList;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chirp.model.Post;
import chirp.model.Timestamp;
import chirp.model.User;
import chirp.model.UserRepository;
import chirp.service.representations.PostRepresentation;
import chirp.service.representations.PostRepresentationCollection;

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
	public PostRepresentation getPost(
				@Context UriInfo uriInfo,
				@PathParam("username") String username,
				@PathParam("timestamp") String timestamp
			) {
		logger.info("Searching for a post posted by user with username={} and timestamp={}", username, timestamp);
		Post post = database.getUser(username).getPost(new Timestamp(timestamp));
		logger.info("Found a post: " + post);
		URI selflink = UriBuilder
				.fromResource(this.getClass())
				.path(username)
				.path(post.getTimestamp().toString())
				.build();
		return new PostRepresentation(post, false,selflink);
	}
	
	/*
	 * GET /posts
	 */
	@GET
	public PostRepresentationCollection getPosts(@Context UriInfo uriInfo) {
		logger.info("Getting all posts.");
		Collection<PostRepresentation> result = new LinkedList<>();
		for (Post post : database.getPosts()) {
			URI selflink = UriBuilder
					.fromResource(this.getClass())
					.path(post.getUser().getUsername())
					.path(post.getTimestamp().toString())
					.build();
			result.add(new PostRepresentation(post,false,selflink));
		}
		logger.info("Found posts: " + result.size());
		URI selflink = UriBuilder
				.fromResource(this.getClass())
				.build();
		return new PostRepresentationCollection(result,selflink);
	}

	/* 
	 * GET /posts/{username}
	 */
	@GET
	@Path("{username}")
	public PostRepresentationCollection getPostsByUser(@Context UriInfo uriInfo, @PathParam("username") String username) {
		logger.info("Getting all posts.");
		Collection<PostRepresentation> result = new LinkedList<>();
		for (Post post : database.getUser(username).getPosts()) {
			URI selflink = UriBuilder
					.fromResource(this.getClass())
					.path(post.getUser().getUsername())
					.path(post.getTimestamp().toString())
					.build();
			result.add(new PostRepresentation(post, false,selflink));
		}
		logger.info("Found posts: " + result.size());
		URI selflink = UriBuilder
				.fromResource(this.getClass())
				.path(username)
				.build();
		return new PostRepresentationCollection(result,selflink);
	}
	
}