package com.tibiaauctionhistory.module.auctionsearch.service;

import com.tibiaauctionhistory.module.common.model.Auction;
import com.tibiaauctionhistory.module.common.model.SortableField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AuctionServiceImpl implements AuctionService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public AuctionServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Auction> getAuctions(
            int limit,
            int offset,
            SortableField sortBy,
            Sort.Direction orderBy
    ) {
        log.info(
                "Finding all sold auctions on db with limit {} offset {} sortBy {} orderBy {} ...",
                limit, offset, sortBy, orderBy
        );
        final Criteria criteria = Criteria
                .where("sold")
                .is(true);
        final Query query = new Query(criteria)
                .with(Sort.by(orderBy, sortBy.getFieldName()))
                .skip(offset)
                .limit(limit);
        List<Auction> auctions = mongoTemplate.find(query, Auction.class);
        log.info("Found auctions on db with limit {} offset {} sortBy {} orderBy {}", limit, offset, sortBy, orderBy);
        return auctions;
    }
}
