package chirp.service.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/greeting")
public class HelloResource {

	@GET
	public String getHello(@QueryParam("name") String name) {
		return "Hello" + ((name == null) ? "" : ", " + name + "!");
	}

}
