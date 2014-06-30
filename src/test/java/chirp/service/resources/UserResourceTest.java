package chirp.service.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.junit.Test;

import chirp.model.UserRepository;

public class UserResourceTest extends JerseyResourceTest<UserResource> {

	private UserRepository users = UserRepository.getInstance();
	
	@Test
	public void createUserWithPOSTSuccess() {
		Form user = new Form().param("realname", "John Doe").param("username", "john.doe");
		Response response = target("/users").request().post(Entity.form(user));
		assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
		assertNotNull(users.getUser("john.doe"));
	}

}
