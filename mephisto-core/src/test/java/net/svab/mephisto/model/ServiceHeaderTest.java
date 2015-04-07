package net.svab.mephisto.model;

import net.svab.mephisto.error.InvalidHeaderException;
import org.junit.Test;

import java.util.HashSet;

import static com.google.common.collect.Sets.newHashSet;
import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.then;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static java.util.Arrays.asList;
import static net.svab.mephisto.model.ResourceKeyBuilder.resourceKeyBuilder;
import static net.svab.mephisto.model.matcher.EqMatcher.eq;

public class ServiceHeaderTest {

    @Test
    public void shouldFailForHeaderWithWrongValueType() {
        Header header = new Header(eq("foo"), ParameterType.INTEGER, new HashSet<String>(), false);
        ResourceKey resourceKey = resourceKeyBuilder().build();
        ServiceHeader serviceHeader = new ServiceHeader("foo", asList("lol"));

        when(() -> serviceHeader.validate(header, resourceKey));

        then(caughtException())
                .isInstanceOf(InvalidHeaderException.class)
                .hasMessageContaining(resourceKey.toString())
                .hasMessageContaining("INTEGER")
                .hasMessageContaining("lol");
    }

    @Test
    public void shouldFailForHeaderWithWrongValueEnum() {
        Header header = new Header(eq("foo"), ParameterType.INTEGER, newHashSet("1", "2"), false);
        ResourceKey resourceKey = resourceKeyBuilder().build();
        ServiceHeader serviceHeader = new ServiceHeader("foo", asList("3"));

        when(() -> serviceHeader.validate(header, resourceKey));

        then(caughtException())
                .isInstanceOf(InvalidHeaderException.class)
                .hasMessageContaining(resourceKey.toString())
                .hasMessageContaining("3");
    }

}