package net.svab.mephisto.model;

import net.svab.mephisto.error.InvalidQueryParameterException;
import org.junit.Test;

import java.util.HashSet;

import static com.google.common.collect.Sets.newHashSet;
import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.then;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static java.util.Arrays.asList;
import static net.svab.mephisto.model.ResourceKeyBuilder.resourceKeyBuilder;
import static net.svab.mephisto.model.matcher.EqMatcher.eq;

public class ServiceQueryParameterTest {

    @Test
    public void shouldFailForExistingUrlAndQueryParamWithWrongValueType() {
        QueryParameter queryParameter = new QueryParameter(eq("foo"), ParameterType.INTEGER, new HashSet<String>(), false);
        ResourceKey resourceKey = resourceKeyBuilder().build();
        ServiceQueryParameter serviceQueryParameter = new ServiceQueryParameter("foo", asList("lol"));

        when(serviceQueryParameter).validate(queryParameter, resourceKey);

        then(caughtException())
                .isInstanceOf(InvalidQueryParameterException.class)
                .hasMessageContaining(resourceKey.toString())
                .hasMessageContaining("INTEGER")
                .hasMessageContaining("lol");
    }

    @Test
    public void shouldFailForExistingUrlAndQueryParamWithWrongValueEnum() {
        QueryParameter queryParameter = new QueryParameter(eq("foo"), ParameterType.INTEGER, newHashSet("1", "2"), false);
        ResourceKey resourceKey = resourceKeyBuilder().build();
        ServiceQueryParameter serviceQueryParameter = new ServiceQueryParameter("foo", asList("3"));

        when(serviceQueryParameter).validate(queryParameter, resourceKey);

        then(caughtException())
                .isInstanceOf(InvalidQueryParameterException.class)
                .hasMessageContaining(resourceKey.toString())
                .hasMessageContaining("3");
    }

}