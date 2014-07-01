package chirp.service.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.After;
import org.junit.Test;

import chirp.model.User;
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
		assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
		assertNotNull(users.getUser("john.doe"));
		
	}
	
	@Test
	public void createDuplicateUser() {
		
		User john = new User("john.doe", "John Doe");
		users.createUser(john.getUsername(), john.getRealname());
		
		Form userForm = new Form();
		userForm.param("realname", john.getRealname());
		userForm.param("username", john.getUsername());
		Entity<Form> uploadData = Entity.form(userForm);
		
		Response response = target("/users").request().post(uploadData);
		assertEquals(Status.FORBIDDEN.getStatusCode(), response.getStatus());
		
	}
	
	@Test
	public void getSingleUserAsText() {
		
		User john = new User("john.doe", "John Doe");
		users.createUser(john.getUsername(), john.getRealname());
		
		Response response = target("/users").path("john.doe").request().header("Accept", MediaType.TEXT_PLAIN).get();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(MediaType.TEXT_PLAIN_TYPE, response.getMediaType());

		Object entity = response.getEntity();
		assertEquals(ByteArrayInputStream.class, entity.getClass());

		// read the body of the response
		ByteArrayInputStream body = (ByteArrayInputStream) entity;
		StringBuffer sb = new StringBuffer();
		while (true) {
			int read = body.read();
			if ( read == -1 ) break;
			sb.append((char)read);
		}
		
		// close body stream
		try {
			body.close();
		} catch (IOException ioe) {
			fail("Caught IOException.");
		}
		
		assertEquals(john.toString(), sb.toString());		
		
	}

	@Test
	public void getUsersAsText() {
		
		Collection<User> set = new HashSet<>();
		set.add(new User("john.doe", "John Doe"));
		set.add(new User("jane.doe", "Jane Doe"));
		set.add(new User("jack.doe", "Jack Doe"));
		set.add(new User("jill.doe", "Jill Doe"));
		for ( User u: set) {
			users.createUser(u.getUsername(), u.getRealname());
		}
		
		Response response = target("/users").request().header("Accept", "text/plain").get();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(MediaType.TEXT_PLAIN_TYPE, response.getMediaType());

		Object entity = response.getEntity();
		assertEquals(ByteArrayInputStream.class, entity.getClass());

		// read the body of the response
		ByteArrayInputStream body = (ByteArrayInputStream) entity;
		StringBuffer sb = new StringBuffer();
		while (true) {
			int read = body.read();
			if ( read == -1 ) break;
			sb.append((char)read);
		}
		
		// close body stream
		try {
			body.close();
		} catch (IOException ioe) {
			fail("Caught IOException.");
		}
		
		// check that every user is in the body response
		// we cannot guarantee the order of the output user
		for (User u : set) {
			assertEquals(true,sb.toString().contains(u.toString()));
		}
		
	}

	@Test
	public void getSingleUserAsXml() {
		
		User john = new User("john.doe", "John Doe");
		users.createUser(john.getUsername(), john.getRealname());
		
		Response response = target("/users").path("john.doe").request().header("Accept", MediaType.APPLICATION_XML).get();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(MediaType.APPLICATION_XML_TYPE, response.getMediaType());
		
		// TODO: Implement testing the actual XML response
		
	}

	
	@Test
	public void getUsersAsXml() {
		
		Collection<User> set = new HashSet<>();
		set.add(new User("john.doe", "John Doe"));
		set.add(new User("jane.doe", "Jane Doe"));
		set.add(new User("jack.doe", "Jack Doe"));
		set.add(new User("jill.doe", "Jill Doe"));
		for ( User u: set) {
			users.createUser(u.getUsername(), u.getRealname());
		}
		
		Response response = target("/users").request().header("Accept", MediaType.APPLICATION_XML_TYPE).get();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(MediaType.APPLICATION_XML_TYPE, response.getMediaType());
		
		
	}

	@Test
	public void getSingleUserAsJson() {
	
		User john = new User("john.doe", "John Doe");
		users.createUser(john.getUsername(), john.getRealname());
		
		Response response = target("/users").path("john.doe").request().header("Accept", MediaType.APPLICATION_JSON_TYPE).get();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		
		// TODO: Implement testing the actual JSON response
		
	}

	@Test
	public void getUsersAsJson() {

		Collection<User> set = new HashSet<>();
		set.add(new User("john.doe", "John Doe"));
		set.add(new User("jane.doe", "Jane Doe"));
		set.add(new User("jack.doe", "Jack Doe"));
		set.add(new User("jill.doe", "Jill Doe"));
		for ( User u: set) {
			users.createUser(u.getUsername(), u.getRealname());
		}
		
		Response response = target("/users").request().header("Accept", MediaType.APPLICATION_JSON_TYPE).get();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		
		// TODO: Implement testing the actual JSON response
		
	}

}
