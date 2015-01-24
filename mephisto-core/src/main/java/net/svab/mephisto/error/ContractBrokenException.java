package net.svab.mephisto.error;

public abstract class ContractBrokenException extends RuntimeException {

    public ContractBrokenException(String message) {
        super(message);
    }
}
