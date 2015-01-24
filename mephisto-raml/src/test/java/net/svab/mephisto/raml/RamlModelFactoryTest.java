package net.svab.mephisto.raml;

import org.junit.Test;
import net.svab.mephisto.model.Contract;
import net.svab.mephisto.model.Header;
import net.svab.mephisto.model.ParameterType;
import net.svab.mephisto.model.QueryParameter;
import net.svab.mephisto.model.Response;
import net.svab.mephisto.model.UriParameter;

import java.util.HashSet;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static net.svab.mephisto.model.ResourceBuilder.resourceBuilder;
import static net.svab.mephisto.model.matcher.EqMatcher.eq;
import static net.svab.mephisto.model.matcher.RegexMatcher.regex;

public class RamlModelFactoryTest {

    @Test
    public void shouldBuildContractFromRamlFile() {

        Contract contract = RamlModelFactory.contractFromRaml("api.raml");

        assertThat(contract.getResources()).hasSize(10);

        assertThat(contract.getResources())
                .contains(resourceBuilder().path("/basic").method("GET").response(200, new Response(newHashSet("application/json"))).build());

        assertThat(contract.getResources())
                .contains(resourceBuilder().path("/basic").method("POST").requestContentType("application/json").requestContentType("application/xml").build());

        assertThat(contract.getResources())
                .contains(resourceBuilder()
                        .path("/parametrized/{key}/data")
                        .method("GET")
                        .uriParameter(new UriParameter("key", ParameterType.STRING, new HashSet<String>()))
                        .response(200, new Response(new HashSet<String>())).build());

        assertThat(contract.getResources())
                .contains(resourceBuilder()
                        .path("/parametrized/{key}/data/subresource")
                        .method("GET")
                        .uriParameter(new UriParameter("key", ParameterType.STRING, new HashSet<String>()))
                        .response(200, new Response(new HashSet<String>())).build());

        assertThat(contract.getResources())
                .contains(resourceBuilder().path("/uri-params/enum/{enum}").method("GET").uriParameter(new UriParameter("enum", ParameterType.STRING, newHashSet("foo", "bar"))).build());

        assertThat(contract.getResources())
                .contains(resourceBuilder().path("/uri-params/int/{int}").method("GET").uriParameter(new UriParameter("int", ParameterType.INTEGER, new HashSet<String>())).build());

        assertThat(contract.getResources())
                .contains(resourceBuilder().path("/uri-params/num/{num}").method("GET").uriParameter(new UriParameter("num", ParameterType.NUMBER, new HashSet<String>())).build());

        assertThat(contract.getResources())
                .contains(resourceBuilder().path("/uri-params/bool/{bool}").method("GET").uriParameter(new UriParameter("bool", ParameterType.BOOLEAN, new HashSet<String>())).build());

        assertThat(contract.getResources())
                .contains(resourceBuilder()
                        .path("/query-params")
                        .method("GET")
                        .queryParameter("action", new QueryParameter(eq("action"), ParameterType.STRING, newHashSet("lol", "rofl"), false))
                        .queryParameter("version", new QueryParameter(eq("version"), ParameterType.INTEGER, new HashSet<String>(), true)).build());

        assertThat(contract.getResources())
                .contains(resourceBuilder()
                        .path("/headers")
                        .method("GET")
                        .header(new Header(eq("Some-Header"), ParameterType.STRING, asList("lol", "lmao"), true))
                        .header(new Header(eq("Other-Header"), ParameterType.INTEGER, new HashSet<String>(), false))
                        .header(new Header(regex("^Regex-Header-.*?$"), ParameterType.INTEGER, new HashSet<String>(), false))
                        .build());
    }
}