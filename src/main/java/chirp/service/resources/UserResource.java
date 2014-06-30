package chirp.service.resources;

import java.net.URI;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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

}
