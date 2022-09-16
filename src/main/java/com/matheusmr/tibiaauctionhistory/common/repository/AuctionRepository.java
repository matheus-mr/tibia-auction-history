package com.matheusmr.tibiaauctionhistory.common.repository;

import com.matheusmr.tibiaauctionhistory.common.entity.Auction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends MongoRepository<Auction, Integer> {
}
