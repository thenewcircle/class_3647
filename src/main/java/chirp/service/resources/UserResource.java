package chirp.service.resources;

import java.net.URI;
import java.util.Collection;
import java.util.LinkedList;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chirp.model.User;
import chirp.model.UserRepository;
import chirp.service.representations.UserRepresentation;
import chirp.service.representations.UserRepresentationCollection;

@Path("/users")
public class UserResource {

	private final UserRepository database = UserRepository.getInstance();
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/*
	 * POST /users
	 */
	@POST
	public Response createUser(
			@FormParam("realname") String realname,
			@FormParam("username") String username) {
		logger.info("Creating a user with realname={} and username={}", realname, username);
		User user = database.createUser(username, realname);
		logger.info("Created a user: " + user);
		URI location = UriBuilder.fromResource(this.getClass()).path(username).build();
		return Response.created(location).build();
	}
	
	/*
	 * GET /users/{username}
	 */
	@GET
	@Path("{username}")
	public UserRepresentation getUser(@PathParam("username") String username) {
		logger.info("Searching for a user with username={}", username);
		User user = database.getUser(username);
		logger.info("Found a user: " + user);
		URI selflink = UriBuilder
				.fromResource(this.getClass())
				.path(username)
				.build();
		return new UserRepresentation(user,false,selflink);
	}
	
	/*
	 * GET /users
	 */
	@GET
	public UserRepresentationCollection getUsers() {
		logger.info("Searching for users.");
		Collection<UserRepresentation> users = new LinkedList<>();
		for (User user : database.getUsers()) {
			URI selflink = UriBuilder
					.fromResource(this.getClass())
					.path(user.getUsername())
					.build();
			users.add(new UserRepresentation(user,false,selflink));
		}
		logger.info("Found users: " + users.size());
		URI selflink = UriBuilder
				.fromResource(this.getClass())
				.build();
		return new UserRepresentationCollection(users,selflink);
	}
	
	/*
	 * DELETE /users/{username}
	 */
	@DELETE
	@Path("{username}")
	public Response deleteUser(@PathParam("username") String username) {
		logger.info("Deleting user: username=" + username);
		database.deleteUser(username);
		logger.info("Deleted user: username=" + username);
		return Response.noContent().build();
	}
	
	/*
	 * UPDATE /users/{username}
	 */
	@PUT
	@Path("{username}")
	public Response updateUser(
			@PathParam("username") String username,
			@FormParam("realname") String realname
		) {
		logger.info("Updating user username={} and realname={}", username, realname);
		database.updateUser(username, realname);
		logger.info("User updated: username={} and realname={} ", username, realname);
		return Response.noContent().build();
	}
	
}
