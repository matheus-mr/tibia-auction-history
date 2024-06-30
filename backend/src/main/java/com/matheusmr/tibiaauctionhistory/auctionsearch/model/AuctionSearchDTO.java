package com.matheusmr.tibiaauctionhistory.auctionsearch.model;

import java.util.UUID;

public record AuctionSearchDTO (UUID id, AuctionSearchCriterion criteria){
}
