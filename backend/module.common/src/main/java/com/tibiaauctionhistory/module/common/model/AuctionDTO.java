package com.tibiaauctionhistory.module.common.model;

import java.time.LocalDateTime;

public record AuctionDTO (
        Integer id,
        String name,
        Integer level,
        String vocation,
        String world,
        Integer winningBid,
        LocalDateTime auctionEnd
){}
