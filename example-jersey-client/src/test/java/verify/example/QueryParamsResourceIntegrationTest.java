package verify.example;

import com.google.common.collect.ImmutableSet;
import io.dropwizard.Configuration;
import io.dropwizard.testing.junit.DropwizardAppRule;
import net.svab.mephisto.error.InvalidQueryParameterException;
import net.svab.mephisto.error.QueryParameterNotDefinedException;
import net.svab.mephisto.error.RequiredQueryParametersMissingException;
import net.svab.mephisto.model.ResourceKey;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyInvocation;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static com.google.common.collect.Sets.newHashSet;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.Assertions.assertThat;
import static verify.example.TestUtils.configFilePath;
import static verify.example.TestUtils.jerseyClient;

public class QueryParamsResourceIntegrationTest {

    @ClassRule
    public static DropwizardAppRule<Configuration> APP = new DropwizardAppRule<Configuration>(ExampleApp.class, configFilePath());

    JerseyClient client = jerseyClient();

    @Test
    public void shouldPassForValidRequest() {
        Response response = client.target("http://localhost:9200/query-params").queryParam("status", "FOO").queryParam("version", "112").request().get();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void shouldFailForUnsupportedEnumValue() {
        JerseyInvocation.Builder resource = client.target("http://localhost:9200/query-params").queryParam("status", "lol").request();

        when(() -> resource.get(ClientResponse.class));

        Response response = ((WebApplicationException) caughtException()).getResponse();
        ResponseAssert.assertThat(response).isBreakingContract(400, new InvalidQueryParameterException("status", ImmutableSet.of("FOO", "BAR"), "lol", new ResourceKey("/query-params", "GET")));
    }

    @Test
    public void shouldFailWhenRequiredQueryParamIsMissing() {
        JerseyInvocation.Builder resource = client.target("http://localhost:9200/query-params").request();

        when(() -> resource.get(ClientResponse.class));

        Response response = ((WebApplicationException) caughtException()).getResponse();
        ResponseAssert.assertThat(response).isBreakingContract(400, new RequiredQueryParametersMissingException(newHashSet("version"), new ResourceKey("/query-params", "GET")));
    }

    @Test
    public void shouldFailForUnknownQueryParam() {
        JerseyInvocation.Builder resource = client.target("http://localhost:9200/query-params").queryParam("custom", "112").request();

        when(() -> resource.get(ClientResponse.class));

        Response response = ((WebApplicationException) caughtException()).getResponse();
        ResponseAssert.assertThat(response).isBreakingContract(400, new QueryParameterNotDefinedException("custom", new ResourceKey("/query-params", "GET")));
    }
}
