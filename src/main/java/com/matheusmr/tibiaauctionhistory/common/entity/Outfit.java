package com.matheusmr.tibiaauctionhistory.common.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Outfit {

    String name;
    boolean hasFirstAddon;
    boolean hasSecondAddon;
}
