package com.tibiaauctionhistory.module.auctionsearch.repository;

import com.tibiaauctionhistory.module.auctionsearch.model.AuctionSearch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuctionSearchRepository extends MongoRepository<AuctionSearch, UUID> {
}
