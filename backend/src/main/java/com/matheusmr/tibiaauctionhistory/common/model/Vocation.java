package com.matheusmr.tibiaauctionhistory.common.model;

import java.util.Arrays;

public enum Vocation {

    KNIGHT("Knight", "Elite Knight"),
    PALADIN("Paladin", "Royal Paladin"),
    SORCERER("Sorcerer", "Master Sorcerer"),
    DRUID("Druid", "Elder Druid"),
    ;

    private final String name;
    private final String promotedName;

    Vocation(String name, String promotedName) {
        this.name = name;
        this.promotedName = promotedName;
    }

    public String getName() {
        return name;
    }

    public String getPromotedName() {
        return promotedName;
    }

    public static Vocation getByName(String name){
        return name == null ?
                null :
                Arrays.stream(values())
                        .filter(vocation ->
                                vocation.name.equalsIgnoreCase(name)
                                        || vocation.promotedName.equalsIgnoreCase(name)
                        )
                        .findFirst()
                        .orElse(null);
    }
}
