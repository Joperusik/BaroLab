package com.volosinzena.barolab.exception;

public class GuideNotFoundException extends RuntimeException {
    public GuideNotFoundException(Long modId) {
        super("Guide for Mod with ID " + modId + " not found");
    }

    public GuideNotFoundException(java.util.UUID guideId) {
        super("Guide with ID " + guideId + " not found");
    }
}
