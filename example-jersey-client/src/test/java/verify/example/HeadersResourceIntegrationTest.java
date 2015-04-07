package verify.example;

import com.google.common.collect.ImmutableSet;
import io.dropwizard.Configuration;
import io.dropwizard.testing.junit.DropwizardAppRule;
import net.svab.mephisto.error.InvalidHeaderException;
import net.svab.mephisto.error.RequiredHeaderMissingException;
import net.svab.mephisto.model.Header;
import net.svab.mephisto.model.ParameterType;
import net.svab.mephisto.model.ResourceKey;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyInvocation;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static com.google.common.collect.Sets.newHashSet;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static net.svab.mephisto.model.matcher.EqMatcher.eq;
import static org.assertj.core.api.Assertions.assertThat;
import static verify.example.TestUtils.configFilePath;
import static verify.example.TestUtils.jerseyClient;

public class HeadersResourceIntegrationTest {

    @ClassRule
    public static DropwizardAppRule<Configuration> APP = new DropwizardAppRule<Configuration>(ExampleApp.class, configFilePath());

    JerseyClient client = jerseyClient();

    @Test
    public void shouldPassForValidRequest() {
        Response response = client.target("http://localhost:9200/headers")
                .request()
                .header("Some-Header", "FOO")
                .header("Int-Header", "112")
                .header("Num-Header", "112.987")
                .header("Bool-Header", "false")
                .get(Response.class);

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void shouldPassForValidRequestWithRegexHeader() {
        Response response = client.target("http://localhost:9200/headers")
                .request()
                .header("Some-Header", "FOO")
                .header("Int-Header", "112")
                .header("Num-Header", "112.987")
                .header("Bool-Header", "false")
                .header("Regex-Header-foo", "115")
                .get(Response.class);

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void shouldFailForUnsupportedEnumValue() {
        String name = "Some-Header";
        String value = "lol";
        JerseyInvocation.Builder resource = client.target("http://localhost:9200/headers").request().header(name, value);

        when(() -> resource.get(ClientResponse.class));

        Response response = ((WebApplicationException) caughtException()).getResponse();
        ResponseAssert.assertThat(response).isBreakingContract(400, new InvalidHeaderException(name, ImmutableSet.of("FOO", "BAR"), value, new ResourceKey("/headers", "GET")));
    }

    @Test
    public void shouldFailWhenIntHeaderIsNotInt() {
        String name = "Int-Header";
        String value = "66.9";
        JerseyInvocation.Builder resource = client.target("http://localhost:9200/headers").request().header(name, value);

        when(() -> resource.get(ClientResponse.class));

        Response response = ((WebApplicationException) caughtException()).getResponse();
        ResponseAssert.assertThat(response).isBreakingContract(400, new InvalidHeaderException(name, ParameterType.INTEGER, value, new ResourceKey("/headers", "GET")));
    }

    @Test
    public void shouldFailWhenRegexIntHeaderIsNotInt() {
        String name = "Regex-Header-bar";
        String value = "xxx";
        JerseyInvocation.Builder resource = client.target("http://localhost:9200/headers").request().header(name, value);

        when(() -> resource.get(ClientResponse.class));

        Response response = ((WebApplicationException) caughtException()).getResponse();
        ResponseAssert.assertThat(response).isBreakingContract(400, new InvalidHeaderException(name, ParameterType.INTEGER, value, new ResourceKey("/headers", "GET")));
    }

    @Test
    public void shouldFailWhenNumberHeaderIsNotNumber() {
        String name = "Num-Header";
        String value = "xxx";
        JerseyInvocation.Builder resource = client.target("http://localhost:9200/headers").request().header(name, value);

        when(() -> resource.get(ClientResponse.class));

        Response response = ((WebApplicationException) caughtException()).getResponse();
        ResponseAssert.assertThat(response).isBreakingContract(400, new InvalidHeaderException(name, ParameterType.NUMBER, value, new ResourceKey("/headers", "GET")));
    }

    @Test
    public void shouldFailWhenBooleanHeaderIsNotBoolean() {
        String name = "Bool-Header";
        String value = "truedat";
        JerseyInvocation.Builder resource = client.target("http://localhost:9200/headers").request().header(name, value);

        when(() -> resource.get(ClientResponse.class));

        Response response = ((WebApplicationException) caughtException()).getResponse();
        ResponseAssert.assertThat(response).isBreakingContract(400, new InvalidHeaderException(name, ParameterType.BOOLEAN, value, new ResourceKey("/headers", "GET")));
    }

    @Test
    public void shouldFailForRequiredHeaderMissing() {
        JerseyInvocation.Builder resource = client.target("http://localhost:9200/headers").request();

        when(() -> resource.get(ClientResponse.class));

        Response response = ((WebApplicationException) caughtException()).getResponse();
        ResponseAssert.assertThat(response).isBreakingContract(400, new RequiredHeaderMissingException(newHashSet(new Header(eq("Some-Header"), ParameterType.STRING, new ArrayList<String>(), true)), new ResourceKey("/headers", "GET")));
    }
}
