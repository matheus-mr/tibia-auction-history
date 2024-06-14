package com.matheusmr.tibiaauctionhistory.auctionsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document("auction_search")
@Data
@AllArgsConstructor
public class AuctionSearch {

    @Id
    UUID id;

    AuctionSearchCriterion criteria;

    LocalDateTime lastUsageAt;
}
