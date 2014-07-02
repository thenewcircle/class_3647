package chirp.service.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.After;
import org.junit.Test;

import chirp.model.Post;
import chirp.model.User;
import chirp.model.UserRepository;

public class PostResourceTest extends JerseyResourceTest<PostResource> {

	private UserRepository users = UserRepository.getInstance();
	
	@After
	public void cleanup() {
		users.clear();
	}
	
	@Test
	public void createPostSuccess() {
		
		User user = new User("john.doe","John Doe");
		users.createUser(user.getUsername(), user.getRealname());
		
		String content = "Hello from John.";
		
		Form userForm = new Form();
		userForm.param("username", "john.doe");
		userForm.param("content", content);
		Entity<Form> uploadData = Entity.form(userForm);
		
		Response response = target("/posts").path(user.getUsername()).request().post(uploadData);
		assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
		
		Post post = users.getUser(user.getUsername()).getPosts().iterator().next();
		assertEquals(content, post.getContent());	
	}

	@Test
	public void createPostOnNonExistentUser() {
		
		String content = "Hello from nobody.";
		
		Form userForm = new Form();
		userForm.param("username", "nobody");
		userForm.param("content", content);
		Entity<Form> uploadData = Entity.form(userForm);
		
		Response response = target("/posts").path("nobody").request().post(uploadData);
		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		
	}
	
	@Test
	public void getAllPosts() {
		
		User john = new User("john.doe","John Doe");
		User jane = new User("jane.doe","Jane Doe");
		
		users.createUser(john.getUsername(),john.getRealname());
		users.createUser(jane.getUsername(),jane.getRealname());
		
		for (int i=0; i<4; i++) {
			users.getUser(john.getUsername()).createPost("John's post #" + i + ".");
			users.getUser(jane.getUsername()).createPost("Jane's post #" + i + ".");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				fail("InterruptedException caught: " + e);
			}
		}

		Response response = target("/posts").request().header("Accept", MediaType.APPLICATION_JSON).get();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		
		// TODO: Add checks for representation returned back
		
	}
	
	@Test
	public void getAllPostsByUser() {

		User john = new User("john.doe","John Doe");
		User jane = new User("jane.doe","Jane Doe");
		
		users.createUser(john.getUsername(),john.getRealname());
		users.createUser(jane.getUsername(),jane.getRealname());

		for (int i=0; i<4; i++) {
			users.getUser(john.getUsername()).createPost("John's post #" + i + ".");
			users.getUser(jane.getUsername()).createPost("Jane's post #" + i + ".");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				fail("InterruptedException caught: " + e);
			}
		}



		Response response = target("/posts").path(john.getUsername()).request().header("Accept", MediaType.APPLICATION_JSON).get();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		
		// TODO: Add checks for representation returned back
		
	}
	
	@Test
	public void getAllPostsByNonExistentUser() {

		User john = new User("john.doe","John Doe");
		User jane = new User("jane.doe","Jane Doe");
		
		users.createUser(john.getUsername(),john.getRealname());
		users.createUser(jane.getUsername(),jane.getRealname());
		
		for (int i=0; i<4; i++) {
			users.getUser(john.getUsername()).createPost("John's post #" + i + ".");
			users.getUser(jane.getUsername()).createPost("Jane's post #" + i + ".");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				fail("InterruptedException caught: " + e);
			}
		}

		Response response = target("/posts").path("non-existent").request().header("Accept", MediaType.APPLICATION_JSON).get();
		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
		
		// TODO: Add checks for representation returned back
		
	}
	
//	@Test
//	public void deleteUser() {
//		fail("Test not implemented.");
//	}
//	
//	@Test void updateUser() {
//		fail("Test not implemented.");
//	}
	
}
