package chirp.service.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.junit.Test;

import chirp.model.UserRepository;

public class UserResourceTest extends JerseyResourceTest<UserResource> {

	private UserRepository users = UserRepository.getInstance();
	
	@Test
	public void createUserWithPOSTSuccess() {
		Form userForm = new Form();
		userForm.param("realname", "John Doe");
		userForm.param("username", "john.doe");
		Entity<Form> uploadData = Entity.form(userForm);
		Response response = target("/users").request().post(uploadData);
		assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
		assertNotNull(users.getUser("john.doe"));
		String realName = target("/users").path("john.doe").request().accept(MediaType.TEXT_PLAIN).get(String.class);
		assertEquals("John Doe", realName);
	}

}
