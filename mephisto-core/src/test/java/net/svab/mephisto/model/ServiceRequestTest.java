package net.svab.mephisto.model;

import net.svab.mephisto.error.InvalidResourceBodyException;
import net.svab.mephisto.error.QueryParameterNotDefinedException;
import net.svab.mephisto.error.RequiredHeaderMissingException;
import net.svab.mephisto.error.RequiredQueryParametersMissingException;
import org.junit.Test;

import java.util.HashSet;

import static com.google.common.collect.Sets.newHashSet;
import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.then;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static java.util.Arrays.asList;
import static net.svab.mephisto.model.ResourceBuilder.resourceBuilder;
import static net.svab.mephisto.model.matcher.EqMatcher.eq;

public class ServiceRequestTest {

    @Test
    public void shouldPassForDefinedResource() {
        Resource resource = resourceBuilder().path("/foo/{bar}").method("POST").uriParameter(new UriParameter("bar", ParameterType.STRING)).build();
        ServiceRequest request = new ServiceRequest("/foo/rubbish", "POST", null, new HashSet<ServiceQueryParameter>(), new HashSet<ServiceHeader>());

        request.validate(resource);
    }

    @Test
    public void shouldFailForExistingUrlAndWrongBodyContentType() {
        Resource resource = resourceBuilder().path("/foo").requestContentType("application/json").build();
        ServiceRequest request = new ServiceRequest("/foo", "GET", "application/xml", new HashSet<ServiceQueryParameter>(), new HashSet<ServiceHeader>());

        when(() -> request.validate(resource));

        then(caughtException())
                .isInstanceOf(InvalidResourceBodyException.class)
                .hasMessageContaining("GET /foo")
                .hasMessageContaining("application/xml");
    }

    @Test
    public void shouldFailForExistingUrlAndUnknownQueryParam() {
        Resource resource = resourceBuilder().path("/foo").queryParameter("foo", new QueryParameter(eq("foo"), ParameterType.INTEGER, new HashSet<String>(), false)).build();
        ServiceRequest request = new ServiceRequest("/foo", "GET", null, newHashSet(new ServiceQueryParameter("bar", asList("lol"))), new HashSet<ServiceHeader>());

        when(() -> request.validate(resource));

        then(caughtException())
                .isInstanceOf(QueryParameterNotDefinedException.class)
                .hasMessageContaining("GET /foo")
                .hasMessageContaining("bar");
    }

    @Test
    public void shouldFailForExistingUrlAndRequiredQueryParamMissing() {
        Resource resource = resourceBuilder().path("/foo").queryParameter("foo", new QueryParameter(eq("foo"), ParameterType.INTEGER, new HashSet<String>(), true)).build();
        ServiceRequest request = new ServiceRequest("/foo", "GET", null, new HashSet<ServiceQueryParameter>(), new HashSet<ServiceHeader>());

        when(() -> request.validate(resource));

        then(caughtException())
                .isInstanceOf(RequiredQueryParametersMissingException.class)
                .hasMessageContaining("GET /foo")
                .hasMessageContaining("[foo]");
    }

    @Test
    public void shouldFailForRequiredHeaderMissing() {
        Resource resource = resourceBuilder().path("/foo").header(new Header(eq("foo"), ParameterType.INTEGER, new HashSet<String>(), true)).build();
        ServiceRequest request = new ServiceRequest("/foo", "GET", null, new HashSet<ServiceQueryParameter>(), new HashSet<ServiceHeader>());

        when(() -> request.validate(resource));

        then(caughtException())
                .isInstanceOf(RequiredHeaderMissingException.class)
                .hasMessageContaining("GET /foo")
                .hasMessageContaining("[foo]");
    }
}