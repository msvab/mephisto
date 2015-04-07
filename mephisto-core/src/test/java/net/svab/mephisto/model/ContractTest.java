package net.svab.mephisto.model;

import net.svab.mephisto.error.ResourceNotDefinedException;
import org.junit.Test;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.then;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static java.util.Arrays.asList;
import static net.svab.mephisto.model.ResourceBuilder.resourceBuilder;

public class ContractTest {

    Contract contract = new Contract(asList(
            resourceBuilder().method("GET").path("/foo/{bar}").build(),
            resourceBuilder().method("POST").path("/foo/{bar}").build()
    ));

    @Test
    public void shouldPassForDefinedResource() {
        contract.validateResource("GET", "/foo/{bar}");
    }

    @Test
    public void shouldFailForUnknownMethod() {
        when(() -> contract.validateResource("DELETE", "/foo/{bar}"));

        then(caughtException()).isInstanceOf(ResourceNotDefinedException.class);
    }

    @Test
    public void shouldFailForUnknownMethodAndUrl() {
        when(() -> contract.validateResource("DELETE", "/foo"));

        then(caughtException()).isInstanceOf(ResourceNotDefinedException.class);
    }
}