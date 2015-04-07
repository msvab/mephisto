package net.svab.mephisto.model;

import net.svab.mephisto.error.ResponseNotDefinedException;
import org.junit.Test;

import java.util.HashSet;

import static com.google.common.collect.Sets.newHashSet;
import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.then;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static net.svab.mephisto.model.ResourceBuilder.resourceBuilder;

public class ServiceResponseTest {

    @Test
    public void shouldPassForDefinedResponse() {
        Resource resource = resourceBuilder()
                .path("/foo/{bar}")
                .method("POST")
                .uriParameter(new UriParameter("bar", ParameterType.STRING))
                .response(200, new Response(newHashSet("application/json"))).build();
        ServiceResponse response = new ServiceResponse("/foo/rubbish", "POST", 200, null);

        response.validate(resource);
    }

    @Test
    public void shouldFailForExistingUrlAndWrongResponse() {
        Resource resource = resourceBuilder().path("/foo/{bar}").method("POST").uriParameter(new UriParameter("bar", ParameterType.STRING)).response(422, new Response(new HashSet<String>())).build();
        ServiceResponse response = new ServiceResponse("/foo/rubbish", "POST", 200, null);

        when(() -> response.validate(resource));

        then(caughtException())
                .isInstanceOf(ResponseNotDefinedException.class)
                .hasMessageContaining("POST /foo/{bar}")
                .hasMessageContaining("200");
    }

    @Test
    public void shouldFailForExistingUrlAndWrongResponseContentType() {
        Resource resource = resourceBuilder()
                .path("/foo/{bar}")
                .method("POST")
                .uriParameter(new UriParameter("bar", ParameterType.STRING))
                .response(422, new Response(newHashSet("application/json"))).build();
        ServiceResponse response = new ServiceResponse("/foo/rubbish", "POST", 422, "application/xml");

        when(() -> response.validate(resource));

        then(caughtException())
                .isInstanceOf(ResponseNotDefinedException.class)
                .hasMessageContaining("POST /foo/{bar}")
                .hasMessageContaining("application/xml");
    }
}