package chirp.service.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Test;

import chirp.model.Post;
import chirp.model.Timestamp;
import chirp.model.User;
import chirp.model.UserRepository;
import chirp.service.representations.PostRepresentation;
import chirp.service.representations.PostRepresentationCollection;

public class PostResourceTest extends JerseyResourceTest<PostResource> {

	private UserRepository users = UserRepository.getInstance();
	
	@Before
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
	public void getAllPostsAsJson() {
		
		this.setupUsersAndPosts();

		Response response = target("/posts").request().header("Accept", MediaType.APPLICATION_JSON).get();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		
		try {
			PostRepresentationCollection postlist = response.readEntity(PostRepresentationCollection.class);
			assertNotNull(postlist);
			for (PostRepresentation post : postlist.getPosts()) {
				String username = post.getUser();
				assertNotNull(username);
				User user = users.getUser(username);
				assertNotNull(user);
				Post p = user.getPost(new Timestamp(post.getTimestamp()));
				assertNotNull(p);
				assertEquals(user.getRealname() + " " + post.getTimestamp(), p.getContent());
			}
		} catch (Exception e) {
			fail("Caught exception: " + e);
		}
		
	}
	
	@Test
	public void getAllPostsAsXml() {
		
		this.setupUsersAndPosts();

		Response response = target("/posts").request().header("Accept", MediaType.APPLICATION_XML).get();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		
		try {
			PostRepresentationCollection postlist = response.readEntity(PostRepresentationCollection.class);
			assertNotNull(postlist);
			for (PostRepresentation post : postlist.getPosts()) {
				String username = post.getUser();
				assertNotNull(username);
				User user = users.getUser(username);
				assertNotNull(user);
				Post p = user.getPost(new Timestamp(post.getTimestamp()));
				assertNotNull(p);
				assertEquals(user.getRealname() + " " + post.getTimestamp(), p.getContent());
			}
		} catch (Exception e) {
			fail("Caught exception: " + e);
		}
		
	}
	
	@Test
	public void getAllPostsByUserAsJson() {

		this.setupUsersAndPosts();

		Response response = target("/posts").path("john.doe").request().header("Accept", MediaType.APPLICATION_JSON).get();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		
		try {
			PostRepresentationCollection postlist = response.readEntity(PostRepresentationCollection.class);
			assertNotNull(postlist);
			for (PostRepresentation post : postlist.getPosts()) {
				assertEquals("john.doe", post.getUser());
				User user = users.getUser("john.doe");
				assertNotNull(user);
				Post p = user.getPost(new Timestamp(post.getTimestamp()));
				assertNotNull(p);
				assertEquals("John Doe " + post.getTimestamp(), p.getContent());
			}
		} catch (Exception e) {
			fail("Caught exception: " + e);
		}
		
	}
	
	@Test
	public void getAllPostsByUserAsXml() {

		this.setupUsersAndPosts();

		Response response = target("/posts").path("john.doe").request().header("Accept", MediaType.APPLICATION_XML).get();
		assertEquals(Status.OK.getStatusCode(), response.getStatus());
		
		try {
			PostRepresentationCollection postlist = response.readEntity(PostRepresentationCollection.class);
			assertNotNull(postlist);
			for (PostRepresentation post : postlist.getPosts()) {
				assertEquals("john.doe", post.getUser());
				User user = users.getUser("john.doe");
				assertNotNull(user);
				Post p = user.getPost(new Timestamp(post.getTimestamp()));
				assertNotNull(p);
				assertEquals("John Doe " + post.getTimestamp(), p.getContent());
			}
		} catch (Exception e) {
			fail("Caught exception: " + e);
		}
		
	}
	
	@Test
	public void getAllPostsByNonExistentUser() {

		this.setupUsersAndPosts();

		Response response = target("/posts").path("non-existent").request().header("Accept", MediaType.APPLICATION_JSON).get();
		assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
				
	}
	
	
	private void setupUsersAndPosts() {
		
		User john = new User("john.doe","John Doe");
		User jane = new User("jane.doe","Jane Doe");
		
		users.createUser(john.getUsername(),john.getRealname());
		users.createUser(jane.getUsername(),jane.getRealname());
		
		for (int i=0; i<4; i++) {
			users.getUser(john.getUsername()).createPost(john.getRealname() + " " + (new Timestamp()));
			users.getUser(jane.getUsername()).createPost(jane.getRealname() + " " + (new Timestamp()));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				fail("InterruptedException caught: " + e);
			}
		}
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
