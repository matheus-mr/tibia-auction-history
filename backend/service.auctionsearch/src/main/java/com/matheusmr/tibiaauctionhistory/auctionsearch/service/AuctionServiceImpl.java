package com.matheusmr.tibiaauctionhistory.auctionsearch.service;

import com.matheusmr.tibiaauctionhistory.common.model.*;
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
    private final AuctionDomainCacheService auctionDomainCacheService;

    @Autowired
    public AuctionServiceImpl(
            MongoTemplate mongoTemplate,
            AuctionRepository auctionRepository,
            AuctionDomainCacheService auctionDomainCacheService
    ) {
        this.mongoTemplate = mongoTemplate;
        this.auctionRepository = auctionRepository;
        this.auctionDomainCacheService = auctionDomainCacheService;
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

    @Override
    public AuctionDomainDTO getDomain() {
        log.info("Finding auction domain on cache...");
        final AuctionDomainDTO cachedAuctionDomainDTO = auctionDomainCacheService.getAuctionDomainDTO();
        if (cachedAuctionDomainDTO != null){
            log.info("Found auction domain on cache, returning...");
            return cachedAuctionDomainDTO;
        } else {
            log.info("Auction domain not found on cache, getting from db...");
            final AuctionDomainDTO auctionDomainDTO = auctionRepository
                    .getDomain()
                    .withVocations(
                            Arrays.stream(Vocation.values())
                                    .map(Vocation::name)
                                    .collect(Collectors.toList())
                    );
            log.info("Got auction domain from db, caching result...");
            auctionDomainCacheService.cacheAuctionDomainDTO(auctionDomainDTO);
            log.info("Cached auction domain result, returning...");
            return auctionDomainDTO;
        }
    }
}
