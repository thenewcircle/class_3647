package chirp.service.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.HashSet;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;

import chirp.model.NoSuchEntityException;
import chirp.model.User;
import chirp.model.UserRepository;
import chirp.service.representations.UserRepresentation;
import chirp.service.representations.UserRepresentationCollection;

public class UserResourceTest extends JerseyResourceTest<UserResource> {

	private UserRepository users = UserRepository.getInstance();
	
	@Before
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
	public void getSingleUserAsXml() {
		
		User john = new User("john.doe", "John Doe");
		users.createUser(john.getUsername(), john.getRealname());
		
		Response response = target("/users").path("john.doe").request().header("Accept", MediaType.APPLICATION_XML).get();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(MediaType.APPLICATION_XML_TYPE, response.getMediaType());
		
		try {
			UserRepresentation user = response.readEntity(UserRepresentation.class);
			assertNotNull(user);
			assertEquals(john.getUsername(), user.getUsername());
			assertEquals(john.getRealname(), user.getRealname());
		} catch (Exception e) {
			fail("Caught exception: " + e);
		}
		
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
		
		Response response = target("/users").request().header("Accept", MediaType.APPLICATION_XML).get();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(MediaType.APPLICATION_XML_TYPE, response.getMediaType());
		
		try {
			UserRepresentationCollection userlist = response.readEntity(UserRepresentationCollection.class);
			assertNotNull(userlist);
			for (UserRepresentation user : userlist.getUsers()) {
				assertNotNull(users.getUser(user.getUsername()));
			}
		} catch (Exception e) {
			fail("Caught exception: " + e);
		}
		
	}

	@Test
	public void getSingleUserAsJson() {
	
		User john = new User("john.doe", "John Doe");
		users.createUser(john.getUsername(), john.getRealname());
		
		Response response = target("/users").path("john.doe").request().header("Accept", MediaType.APPLICATION_JSON).get();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		
		try {
			UserRepresentation user = response.readEntity(UserRepresentation.class);
			assertNotNull(user);
			assertEquals(john.getUsername(), user.getUsername());
			assertEquals(john.getRealname(), user.getRealname());
		} catch (Exception e) {
			fail("Caught exception: " + e);
		}
		
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
		
		try {
			UserRepresentationCollection userlist = response.readEntity(UserRepresentationCollection.class);
			assertNotNull(userlist);
			for (UserRepresentation user : userlist.getUsers()) {
				assertNotNull(users.getUser(user.getUsername()));
			}
		} catch (Exception e) {
			fail("Caught exception: " + e);
		}
	}
	
	@Test
	public void deleteUser() {

		User john = new User("john.doe", "John Doe");
		users.createUser(john.getUsername(), john.getRealname());

		Response response = target("/users").path("john.doe").request().delete();
		assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());

		try {
			User user = users.getUser(john.getUsername());
			fail("Deleted user found: " + user);
		} catch (NoSuchEntityException nsee) {
			assertEquals(null,nsee.getMessage());
		}

	}

	
	@Test
	public void updateUser() {
		
		User john = new User("john.doe", "John Doe");
		users.createUser(john.getUsername(), john.getRealname());

		Form userForm = new Form();
		userForm.param("realname", "Joe Doe");
		Entity<Form> uploadData = Entity.form(userForm);

		Response response = target("/users").path("john.doe").request().put(uploadData);
		assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatus());

		try {
			User user = users.getUser(john.getUsername());
			assertEquals("Joe Doe", user.getRealname());
		} catch (NoSuchEntityException nsee) {
			fail("Updated user not found: " + nsee.getMessage());
		}
		
	}
	
	
}
