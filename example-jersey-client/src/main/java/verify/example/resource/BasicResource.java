package verify.example.resource;

import verify.example.model.BasicData;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

@Path("/basic")
public class BasicResource {

    @GET
    @Produces("application/json")
    public Response getBasic() {
        return ok(new BasicData("lol", 15)).build();
    }

    @POST
    @Consumes({"application/json", "application/xml"})
    public Response postBasic(BasicData data) {
        return status(Response.Status.UNAUTHORIZED).build();
    }
}
