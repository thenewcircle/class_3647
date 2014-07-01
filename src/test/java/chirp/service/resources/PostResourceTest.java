package chirp.service.resources;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
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

}
