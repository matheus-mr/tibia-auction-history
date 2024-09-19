package com.tibiaauctionhistory.service.auctionsearch.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("auction_search")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AuctionSearch {

    @Id
    @EqualsAndHashCode.Include
    String id;

    AuctionSearchCriterion criteria;
}
