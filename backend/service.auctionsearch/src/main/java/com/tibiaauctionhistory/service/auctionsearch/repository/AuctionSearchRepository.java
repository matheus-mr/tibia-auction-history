package com.tibiaauctionhistory.service.auctionsearch.repository;

import com.tibiaauctionhistory.service.auctionsearch.model.AuctionSearch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionSearchRepository extends MongoRepository<AuctionSearch, String> {
}
