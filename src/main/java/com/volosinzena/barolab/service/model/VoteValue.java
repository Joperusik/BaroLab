package com.volosinzena.barolab.service.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VoteValue {
    DISLIKE(-1),
    LIKE(1);

    private final int value;

    VoteValue(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    public static VoteValue fromValue(Integer value) {
        if (value == null) {
            return null;
        }
        if (value == 1) {
            return LIKE;
        }
        if (value == -1) {
            return DISLIKE;
        }
        throw new IllegalArgumentException("Vote value must be +1 or -1");
    }
}
