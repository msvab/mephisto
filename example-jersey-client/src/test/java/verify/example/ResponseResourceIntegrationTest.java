package verify.example;

import io.dropwizard.Configuration;
import io.dropwizard.testing.junit.DropwizardAppRule;
import net.svab.mephisto.error.ResponseNotDefinedException;
import net.svab.mephisto.model.ResourceKey;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyInvocation;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static verify.example.TestUtils.configFilePath;
import static verify.example.TestUtils.jerseyClient;

public class ResponseResourceIntegrationTest {

    @ClassRule
    public static DropwizardAppRule<Configuration> APP = new DropwizardAppRule<Configuration>(ExampleApp.class, configFilePath());

    JerseyClient client = jerseyClient();

    @Test
    public void shouldSucceedOnValidRequest() {
        Response response = client.target("http://localhost:9200/response").request().get();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void shouldFailOnUndefinedResponseCode() {
        JerseyInvocation.Builder resource = client.target("http://localhost:9200/response").request();

        Response response = resource.head();

        ResponseAssert.assertThat(response).isBreakingContract(500, new ResponseNotDefinedException(304, new ResourceKey("/response", "HEAD")));
    }

    @Test
    public void shouldFailOnUndefinedResponseContentType() {
        JerseyInvocation.Builder resource = client.target("http://localhost:9200/response/wrong-content-type").request();

        Response response = resource.get();

        ResponseAssert.assertThat(response).isBreakingContract(500, new ResponseNotDefinedException(200, new ResourceKey("/response/wrong-content-type", "GET"), "text/plain"));
    }
}
