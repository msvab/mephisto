package net.svab.mephisto.raml;

import com.google.common.base.Function;
import org.raml.parser.rule.ValidationResult;

import java.util.List;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Lists.transform;

public class InvalidApiContractException extends RuntimeException {

    public InvalidApiContractException(List<ValidationResult> results) {
        super("API Contract isn't a valid RAML file:\n" + printResults(results));
    }

    private static String printResults(List<ValidationResult> results) {
        return on("\n").join(transform(results, new Function<ValidationResult, String>() {
            @Override public String apply(ValidationResult result) {
                return result.getLevel() + ": " + result.getMessage();
            }
        }));
    }
}
