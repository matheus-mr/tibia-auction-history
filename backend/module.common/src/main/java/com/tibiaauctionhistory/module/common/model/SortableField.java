package com.matheusmr.tibiaauctionhistory.common.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum SortableField {

    NAME("name"),
    LEVEL("level"),
    VOCATION("vocation"),
    WORLD("world"),
    WINNING_BID("winningBid"),
    AUCTION_END("auctionEnd"),
    ;

    private final String fieldName;

    SortableField(String fieldName) {
        this.fieldName = fieldName;
    }

    public static Optional<SortableField> getByFieldName(String fieldName){
        return Arrays.stream(values())
                .filter(sortableField -> sortableField.getFieldName().equalsIgnoreCase(fieldName))
                .findFirst();
    }
}
