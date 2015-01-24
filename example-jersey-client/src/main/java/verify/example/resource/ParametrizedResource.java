package verify.example.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.ok;

@Path("/parametrized/{key}/data")
public class ParametrizedResource {

    @GET
    public Response get(@PathParam("key") String key) {
        return ok().build();
    }

    @GET
    @Path("/subresource")
    public Response getSubresource(@PathParam("key") String key) {
        return ok().build();
    }
}
