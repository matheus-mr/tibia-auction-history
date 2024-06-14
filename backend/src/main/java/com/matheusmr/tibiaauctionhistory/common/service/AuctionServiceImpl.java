package com.matheusmr.tibiaauctionhistory.common.service;

import com.matheusmr.tibiaauctionhistory.auctionsearch.model.SortableField;
import com.matheusmr.tibiaauctionhistory.common.model.Auction;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionDomainDTO;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionsResult;
import com.matheusmr.tibiaauctionhistory.common.model.Vocation;
import com.matheusmr.tibiaauctionhistory.common.repository.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuctionServiceImpl implements AuctionService {

    private final MongoTemplate mongoTemplate;
    private final AuctionRepository auctionRepository;

    @Autowired
    public AuctionServiceImpl(MongoTemplate mongoTemplate,
                              AuctionRepository auctionRepository) {
        this.mongoTemplate = mongoTemplate;
        this.auctionRepository = auctionRepository;
    }

    @Override
    public AuctionsResult getAuctions(
            int limit,
            int offset,
            SortableField sortBy,
            Sort.Direction orderBy
    ) {
        final Criteria criteria = Criteria
                .where("sold")
                .is(true);
        final Query query = new Query(criteria).with(Sort.by(orderBy, sortBy.getFieldName()));
        final List<Auction> result = mongoTemplate.find(query, Auction.class);
        return new AuctionsResult(result.subList(offset, Math.min(offset + limit, result.size())), result.size());
    }

    @Override
    public AuctionDomainDTO getDomain() {
        return auctionRepository
                .getDomain()
                .withVocations(
                        Arrays.stream(Vocation.values())
                                .map(Vocation::getName)
                                .collect(Collectors.toList())
                );
    }
}
