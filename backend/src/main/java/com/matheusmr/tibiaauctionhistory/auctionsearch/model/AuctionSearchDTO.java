package com.matheusmr.tibiaauctionhistory.auctionsearch.model;

import lombok.Data;

import java.util.UUID;

@Data
public class AuctionSearchDTO {

    UUID id;

    AuctionSearchCriterion criteria;
}
