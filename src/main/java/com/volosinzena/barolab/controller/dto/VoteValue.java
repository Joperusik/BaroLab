package com.volosinzena.barolab.controller.dto;

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
}
