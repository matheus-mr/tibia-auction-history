package com.matheusmr.tibiaauctionhistory.common.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuctionDTO {

    Integer id;
    String name;
    Integer level;
    String vocation;
    String world;
    LocalDateTime creationDate;
    LocalDateTime auctionEnd;
    Integer winningBid;

    Integer axeFighting;
    Integer clubFighting;
    Integer distanceFighting;
    Integer fishing;
    Integer fistFighting;
    Integer magicLevel;
    Integer shielding;
    Integer swordFighting;
}
