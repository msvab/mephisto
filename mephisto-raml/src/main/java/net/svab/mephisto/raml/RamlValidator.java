package net.svab.mephisto.raml;

import org.raml.parser.rule.ValidationResult;
import org.raml.parser.visitor.RamlValidationService;

import java.util.List;

public class RamlValidator {

    public static void validateRaml(String location) {
        List<ValidationResult> results = RamlValidationService.createDefault().validate(location);
        if (!results.isEmpty()) {
            throw new InvalidApiContractException(results);
        }
    }
}
