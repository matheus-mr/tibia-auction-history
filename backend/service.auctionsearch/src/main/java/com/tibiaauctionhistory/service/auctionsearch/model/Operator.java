package com.tibiaauctionhistory.service.auctionsearch.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public enum Operator {

    AND("and", null, null, 30),
    OR("or", null, null, 30),
    EQUALS("eq", 1, null, null),
    NOT_EQUALS("ne", 1, null, null),
    IN("in", null, 50, null),
    NOT_IN("nin", null, 50, null),
    GREATER_THAN("gt", 1, null, null),
    GREATER_OR_EQUALS_THAN("gte", 1, null, null),
    LESS_THAN("lt", 1, null, null),
    LESS_OR_EQUALS_THAN("lte", 1, null, null),
    BETWEEN("bt", 2, null, null),
    ELEMENT_MATCH("elemMatch", null,null, 30),
    REGEX("regex", 1, null, null)
    ;

    private final String key;
    @Getter private final Integer expectedValues;
    @Getter private final Integer maxValues;
    @Getter private final Integer maxCriterias;

    Operator(String key, Integer expectedValues, Integer maxValues, Integer maxCriterias) {
        this.key = key;
        this.expectedValues = expectedValues;
        this.maxValues = maxValues;
        this.maxCriterias = maxCriterias;
    }

    @JsonValue
    public String getKey() {
        return key;
    }

    public boolean hasExpectedValuesAmount(){
        return this.expectedValues != null;
    }

    public boolean hasMaxValuesAmount(){
        return this.maxValues != null;
    }

    public boolean hasMaxCriteriasAmount(){
        return this.maxCriterias != null;
    }

    public static Optional<Operator> getByKey(String key){
        return Arrays.stream(values())
                .filter(operator -> operator.getKey().equalsIgnoreCase(key))
                .findFirst();
    }
}
