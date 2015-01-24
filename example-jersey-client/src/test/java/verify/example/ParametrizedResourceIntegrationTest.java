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
import javax.ws.rs.core.Response;

import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.Assertions.assertThat;
import static verify.example.TestUtils.configFilePath;
import static verify.example.TestUtils.jerseyClient;

public class ParametrizedResourceIntegrationTest {

    @ClassRule
    public static DropwizardAppRule<Configuration> APP = new DropwizardAppRule<Configuration>(ExampleApp.class, configFilePath());

    JerseyClient client = jerseyClient();

    @Test
    public void shouldPassForValidRequest() {
        Response response = client.target("http://localhost:9200/parametrized/lol/data").request().get();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void shouldPassForValidRequestForSubresource() {
        Response response = client.target("http://localhost:9200/parametrized/lmao/data/subresource").request().get();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void shouldFailForUnknownSubresource() {
        JerseyInvocation.Builder resource = client.target("http://localhost:9200/parametrized/lmao/data/unknown").request();

        when(resource).get(ClientResponse.class);

        Response response = ((WebApplicationException) caughtException()).getResponse();
        ResponseAssert.assertThat(response).isBreakingContract(400, new ResourceNotDefinedException("GET", "/parametrized/lmao/data/unknown"));
    }
}
