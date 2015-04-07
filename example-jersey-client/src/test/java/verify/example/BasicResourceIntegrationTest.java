package verify.example;

import io.dropwizard.Configuration;
import io.dropwizard.testing.junit.DropwizardAppRule;
import net.svab.mephisto.error.ResourceNotDefinedException;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyInvocation;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.Assertions.assertThat;
import static verify.example.TestUtils.configFilePath;
import static verify.example.TestUtils.jerseyClient;

public class BasicResourceIntegrationTest {

    @ClassRule
    public static DropwizardAppRule<Configuration> APP = new DropwizardAppRule<Configuration>(ExampleApp.class, configFilePath());

    JerseyClient client = jerseyClient();

    @Test
    public void shouldSucceedOnValidRequest() {
        Response response = client.target("http://localhost:9200/basic").request().get();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity()).isNotNull();
    }

    @Test
    public void shouldFailOnUnknownUrl() throws IOException {
        JerseyInvocation.Builder resource = client.target("http://localhost:9200/basics").request();

        when(() -> resource.get(ClientResponse.class));

        Response response = ((WebApplicationException) caughtException()).getResponse();
        ResponseAssert.assertThat(response).isBreakingContract(400, new ResourceNotDefinedException("GET", "/basics"));
    }

    @Test
    public void shouldFailOnUnsupportedMethod() {
        JerseyInvocation.Builder resource = client.target("http://localhost:9200/basic").request();

        when(() -> resource.delete(ClientResponse.class));

        Response response = ((WebApplicationException) caughtException()).getResponse();
        ResponseAssert.assertThat(response).isBreakingContract(400, new ResourceNotDefinedException("DELETE", "/basic"));
    }

    @Test
    public void shouldFailOnUnknownRequestHeader() {
        Response resource = client.target("http://localhost:9200/basic").request().header("Foo", "Bar").get();

        assertThat(resource.getStatus()).isEqualTo(400);
    }

    @Test
    public void shouldPassForUndefinedIgnoredRequestHeader() {
        JerseyInvocation.Builder resource = client.target("http://localhost:9200/basic").request().header("X-Requested-With", "XMLHttpRequest");

        Response response = resource.get();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity()).isNotNull();
    }

    @Test
    public void shouldFailOnUnsupportedContentTypeOfBody() {
        Response resource = client.target("http://localhost:9200/basic").request().post(Entity.entity("foo;bar", MediaType.valueOf("text/csv")));

        assertThat(resource.getStatus()).isEqualTo(400);
    }
}
