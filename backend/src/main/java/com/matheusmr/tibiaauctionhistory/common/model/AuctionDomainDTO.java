package com.matheusmr.tibiaauctionhistory.common.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.With;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuctionDomainDTO {

    @With
    List<String> vocations;
    List<String> worlds;
    List<String> charms;
    List<String> items;
    List<String> storeItems;
    List<String> mounts;
    List<String> storeMounts;
    List<String> outfits;
    List<String> storeOutfits;
    List<String> imbuements;
    List<String> completedQuestLines;
    List<String> titles;
    List<String> achievements;
}
