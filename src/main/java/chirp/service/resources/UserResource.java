package chirp.service.resources;

import java.net.URI;
import java.util.Collection;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chirp.model.User;
import chirp.model.UserRepository;

@Path("/users")
public class UserResource {

	private final UserRepository database = UserRepository.getInstance();
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
	
	@GET
	@Path("{username}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserAsText(@PathParam("username") String username) {
		logger.info("Searching for a TEXT_PLAIN representation of user with username={}", username);
		User user = database.getUser(username);
		logger.info("Found a user: " + user);
		return user.toString();
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getUsersAsText(@PathParam("username") String username) {
		logger.info("Searching for a TEXT_PLAIN representation of all users.");
		Collection<User> users = database.getUsers();
		logger.info("Found users: " + users.size());
		return users.toString();
	}
	
}
