package com.tibiaauctionhistory.module.common.model;

import java.time.LocalDateTime;

public record AuctionDTO (
        Integer id,
        String name,
        Integer level,
        String vocation,
        String world,
        LocalDateTime creationDate,
        LocalDateTime auctionEnd,
        Integer winningBid,
        Integer axeFighting,
        Integer clubFighting,
        Integer distanceFighting,
        Integer fishing,
        Integer fistFighting,
        Integer magicLevel,
        Integer shielding,
        Integer swordFighting
){}
