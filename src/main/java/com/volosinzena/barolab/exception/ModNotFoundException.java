package com.volosinzena.barolab.exception;

public class ModNotFoundException extends RuntimeException {
    public ModNotFoundException(Long externalId) {
        super("Mod with external id " + externalId + " not found");
    }
}
