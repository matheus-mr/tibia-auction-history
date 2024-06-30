package com.matheusmr.tibiaauctionhistory.common.service;

import com.matheusmr.tibiaauctionhistory.auctionsearch.model.SortableField;
import com.matheusmr.tibiaauctionhistory.common.model.Auction;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionDomainDTO;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionsResult;
import com.matheusmr.tibiaauctionhistory.common.model.Vocation;
import com.matheusmr.tibiaauctionhistory.common.repository.AuctionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuctionServiceImpl implements AuctionService {

    private final MongoTemplate mongoTemplate;
    private final AuctionRepository auctionRepository;
    private final AuctionCacheService auctionCacheService;

    @Autowired
    public AuctionServiceImpl(MongoTemplate mongoTemplate,
                              AuctionRepository auctionRepository,
                              AuctionCacheService auctionCacheService) {
        this.mongoTemplate = mongoTemplate;
        this.auctionRepository = auctionRepository;
        this.auctionCacheService = auctionCacheService;
    }

    @Override
    public AuctionsResult getAuctions(
            int limit,
            int offset,
            SortableField sortBy,
            Sort.Direction orderBy
    ) {
        log.info("Finding auctions with limit {} offset {} sortBy {} orderBy {}", limit, offset, sortBy, orderBy);

        List<Auction> auctions = auctionCacheService.getAuctions(limit, offset, sortBy, orderBy);

        if (auctions != null){
            log.info("Found auctions in cache, finding total count...");
            long totalCount = auctionCacheService.countAuctions(sortBy, orderBy);
            log.info("There is {} auctions in cache, returning result...", totalCount);

            return new AuctionsResult(auctions, totalCount);
        } else {
            log.info("Did not found auctions in cache, searching on db...");

            final Criteria criteria = Criteria
                    .where("sold")
                    .is(true);
            final Query query = new Query(criteria).with(Sort.by(orderBy, sortBy.getFieldName()));
            auctions = mongoTemplate.find(query, Auction.class);

            log.info("Found {} auctions on db, caching results...", auctions.size());
            auctionCacheService.cacheAuctions(auctions, sortBy, orderBy);
            log.info("Cached auctions, returning result...");

            return new AuctionsResult(
                    auctions.subList(
                            Math.min(offset, auctions.size()),
                            Math.min(offset + limit, auctions.size())
                    ),
                    auctions.size()
            );
        }
    }

    @Override
    public AuctionDomainDTO getDomain() {
        return auctionRepository
                .getDomain()
                .withVocations(
                        Arrays.stream(Vocation.values())
                                .map(Vocation::name)
                                .collect(Collectors.toList())
                );
    }
}
