package verify.example.resource;

import verify.example.model.Status;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.ok;

@Path("/query-params")
public class QueryParamsResource {

    @GET
    public Response get(@QueryParam("status") Status status, @QueryParam("version") int version) {
        return ok().build();
    }
}
