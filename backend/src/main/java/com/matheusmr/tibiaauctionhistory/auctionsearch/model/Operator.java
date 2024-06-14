package com.matheusmr.tibiaauctionhistory.auctionsearch.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Operator {

    AND("and"),
    OR("or"),
    EQUALS("eq"),
    NOT_EQUALS("ne"),
    IN("in"),
    NOT_IN("nin"),
    GREATER_THAN("gt"),
    GREATER_OR_EQUALS_THAN("gte"),
    LESS_THAN("lt"),
    LESS_OR_EQUALS_THAN("lte"),
    BETWEEN("bt"),
    ELEMENT_MATCH("elemMatch"),
    REGEX("regex")
    ;

    private final String key;

    Operator(String key) {
        this.key = key;
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}
