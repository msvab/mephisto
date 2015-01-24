package net.svab.mephisto.error;

import static java.lang.String.format;

public class InvalidResourceBodyException extends ContractBrokenException {

    public InvalidResourceBodyException(String method, String path, String contentType) {
        super(format("Resource %s %s doesn't accept Content-Type %s", method, path, contentType));
    }
}
