package com.matheusmr.tibiaauctionhistory.common.entity;

import java.util.Arrays;
import java.util.List;

public enum Vocation {

    KNIGHT(List.of("Elite Knight", "Knight")),
    PALADIN(List.of("Royal Paladin", "Paladin")),
    SORCERER(List.of("Master Sorcerer", "Sorcerer")),
    DRUID(List.of("Elder Druid", "Druid")),
    ;

    private final List<String> names;

    Vocation(List<String> names) {
        this.names = names;
    }

    public static Vocation getByName(String name){
        return name == null ?
                null :
                Arrays.stream(values())
                        .filter(vocation -> vocation.names.contains(name))
                        .findFirst()
                        .orElse(null);
    }
}
