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

public class UriParamsResourceIntegrationTest {

    @ClassRule
    public static DropwizardAppRule<Configuration> APP = new DropwizardAppRule<Configuration>(ExampleApp.class, configFilePath());

    JerseyClient client = jerseyClient();

    @Test
    public void shouldPassForValidRequest() {
        Response response = client.target("http://localhost:9200/uri-params/enum/FOO").request().get();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void shouldFailForUnsupportedEnumValue() {
        JerseyInvocation.Builder resource = client.target("http://localhost:9200/uri-params/enum/lol").request();

        when(resource).get(ClientResponse.class);

        Response response = ((WebApplicationException) caughtException()).getResponse();
        ResponseAssert.assertThat(response).isBreakingContract(400, new ResourceNotDefinedException("GET", "/uri-params/enum/lol"));
    }

    @Test
    public void shouldPassWhenIntParamIsInt() {
        Response response = client.target("http://localhost:9200/uri-params/int/99").request().get();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void shouldFailWhenIntParamIsNotInt() {
        JerseyInvocation.Builder resource = client.target("http://localhost:9200/uri-params/int/99.9").request();

        when(resource).get(ClientResponse.class);

        Response response = ((WebApplicationException) caughtException()).getResponse();
        ResponseAssert.assertThat(response).isBreakingContract(400, new ResourceNotDefinedException("GET", "/uri-params/int/99.9"));
    }

    @Test
    public void shouldPassWhenNumberParamIsNumber() {
        Response response = client.target("http://localhost:9200/uri-params/num/99.99").request().get();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void shouldFailWhenNumberParamIsNotNumber() {
        JerseyInvocation.Builder resource = client.target("http://localhost:9200/uri-params/num/lol").request();

        when(resource).get(ClientResponse.class);

        Response response = ((WebApplicationException) caughtException()).getResponse();
        ResponseAssert.assertThat(response).isBreakingContract(400, new ResourceNotDefinedException("GET", "/uri-params/num/lol"));
    }

    @Test
    public void shouldPassWhenBoolParamIsBool() {
        Response response = client.target("http://localhost:9200/uri-params/bool/false").request().get();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void shouldFailWhenBoolParamIsNotBool() {
        JerseyInvocation.Builder resource = client.target("http://localhost:9200/uri-params/bool/truedat").request();

        when(resource).get(ClientResponse.class);

        Response response = ((WebApplicationException) caughtException()).getResponse();
        ResponseAssert.assertThat(response).isBreakingContract(400, new ResourceNotDefinedException("GET", "/uri-params/bool/truedat"));
    }
}
