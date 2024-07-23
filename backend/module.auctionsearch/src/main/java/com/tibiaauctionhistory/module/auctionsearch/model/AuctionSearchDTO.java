package com.tibiaauctionhistory.module.auctionsearch.model;

import java.util.UUID;

public record AuctionSearchDTO (UUID id, AuctionSearchCriterion criteria){
}
