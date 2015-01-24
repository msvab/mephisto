package verify.example.resource;

import verify.example.model.Status;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.ok;

@Path("/uri-params")
public class UriParamsResource {

    @GET
    @Path("/enum/{enum}")
    public Response get(@PathParam("enum") Status param) {
        return ok().build();
    }

    @GET
    @Path("/int/{int}")
    public Response get(@PathParam("int") int param) {
        return ok().build();
    }

    @GET
    @Path("/num/{num}")
    public Response get(@PathParam("num") double param) {
        return ok().build();
    }

    @GET
    @Path("/bool/{bool}")
    public Response get(@PathParam("bool") boolean param) {
        return ok().build();
    }
}
