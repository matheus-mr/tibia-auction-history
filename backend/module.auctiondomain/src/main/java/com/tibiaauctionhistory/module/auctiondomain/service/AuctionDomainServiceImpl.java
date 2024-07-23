package com.matheusmr.tibiaauctionhistory.auctiondomain.service;

import com.matheusmr.tibiaauctionhistory.auctiondomain.model.AuctionDomainDTO;
import com.matheusmr.tibiaauctionhistory.common.model.Auction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.MergeOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

@Service
public class AuctionDomainServiceImpl implements AuctionDomainService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public AuctionDomainServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public AuctionDomainDTO getAuctionDomain() {
        return null;
    }

    @Override
    public void createOrRefreshAuctionDomainView() {
        final MatchOperation matchOperation = Aggregation.match(Criteria.where("sold").is(true));

        final GroupOperation groupOperation = Aggregation.group("world");

        MergeOperation mergeOperation = Aggregation
                .merge()
                .intoCollection("auction_domain_view")
                .whenMatched(MergeOperation.WhenDocumentsMatch.replaceDocument())
                .build();

        // Execute the aggregation to populate/update the view
        mongoTemplate.aggregate(
                Aggregation.newAggregation(matchOperation, groupOperation, mergeOperation),
                Auction.class,
                Object.class
        );
    }
}
