package chirp.service.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Test;

import chirp.model.UserRepository;

public class UserResourceTest extends JerseyResourceTest<UserResource> {

	private UserRepository users = UserRepository.getInstance();
	
	@After
	public void cleanup() {
		users.clear();
	}
	
	@Test
	public void createUserWithPOSTSuccess() {
		
		Form userForm = new Form();
		userForm.param("realname", "John Doe");
		userForm.param("username", "john.doe");
		Entity<Form> uploadData = Entity.form(userForm);
		
		Response response = target("/users").request().post(uploadData);
		assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
		assertNotNull(users.getUser("john.doe"));
		
	}
	
	@Test
	public void createDuplicateUser() {
		
		Form userForm = new Form();
		userForm.param("realname", "John Doe");
		userForm.param("username", "john.doe");
		Entity<Form> uploadData = Entity.form(userForm);
		
		Response response1 = target("/users").request().post(uploadData);
		assertEquals(Response.Status.CREATED.getStatusCode(), response1.getStatus());
		assertNotNull(users.getUser("john.doe"));
		
		Response response2 = target("/users").request().post(uploadData);
		assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response2.getStatus());
		
	}

}
