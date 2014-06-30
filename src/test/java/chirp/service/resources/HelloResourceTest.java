package chirp.service.resources;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HelloResourceTest extends JerseyResourceTest<HelloResource> {

	@Test
	public void helloResourceMustSayHello() {
		String hello = target("/greeting/NewCircle").request().get(String.class);
		assertEquals("Hello, NewCircle!", hello);
	}

}
