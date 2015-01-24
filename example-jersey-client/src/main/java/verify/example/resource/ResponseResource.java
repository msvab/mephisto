package verify.example.resource;

import verify.example.model.BasicData;

import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.notModified;
import static javax.ws.rs.core.Response.ok;

@Path("/response")
public class ResponseResource {

    @HEAD
    public Response head() {
        return notModified().build();
    }

    @GET
    @Produces("application/json")
    public Response get() {
        return ok(new BasicData("name", 11)).build();
    }

    @GET
    @Path("/wrong-content-type")
    public Response getWrongContentType() {
        return ok().header("Content-Type", "text/plain").build();
    }
}
