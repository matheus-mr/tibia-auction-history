package com.tibiaauctionhistory.module.auctionsearch.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document("auction_search")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AuctionSearch {

    @Id
    @EqualsAndHashCode.Include
    UUID id;

    AuctionSearchCriterion criteria;
}
