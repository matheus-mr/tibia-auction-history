package com.matheusmr.tibiaauctionhistory.common.model;

import java.util.List;

public record AuctionsResult(List<Auction> auctions, int totalCount) {
}
