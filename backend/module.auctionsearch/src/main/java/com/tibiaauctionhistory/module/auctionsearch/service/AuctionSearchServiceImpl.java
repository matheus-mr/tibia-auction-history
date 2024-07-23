package com.tibiaauctionhistory.module.auctionsearch.service;

import com.google.common.base.Preconditions;
import com.tibiaauctionhistory.module.auctionsearch.exception.AuctionSearchNotFoundException;
import com.tibiaauctionhistory.module.auctionsearch.model.AuctionSearch;
import com.tibiaauctionhistory.module.auctionsearch.model.AuctionSearchCriterion;
import com.tibiaauctionhistory.module.auctionsearch.model.Operator;
import com.tibiaauctionhistory.module.auctionsearch.repository.AuctionSearchRepository;
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
import java.util.UUID;

@Service
@Slf4j
public class AuctionSearchServiceImpl implements AuctionSearchService {

    public static final String INVALID_AMOUNT_OF_AUCTIONS_FETCHED_MSG =
            "The amount of auctions fetched per request should be between 1 and 1000.";
    public static final String INVALID_OFF_SET =
            "The offset must be non negative.";
    public static final String AUCTION_SEARCH_NOT_FOUND_MSG = "Auction search not found.";

    private final AuctionSearchRepository auctionSearchRepository;
    private final AuctionSearchCacheService auctionSearchCacheService;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public AuctionSearchServiceImpl(AuctionSearchRepository auctionSearchRepository,
                                    AuctionSearchCacheService auctionSearchCacheService,
                                    MongoTemplate mongoTemplate) {
        this.auctionSearchRepository = auctionSearchRepository;
        this.auctionSearchCacheService = auctionSearchCacheService;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public AuctionSearch getAuctionSearch(UUID id) throws AuctionSearchNotFoundException {
        log.info("Finding auction search with id {}...", id);
        final AuctionSearch auctionSearch = auctionSearchRepository.findById(id)
                .orElseThrow(() -> new AuctionSearchNotFoundException(AUCTION_SEARCH_NOT_FOUND_MSG));
        log.info("Found auction search with id {}", id);
        return auctionSearch;
    }

    @Override
    public AuctionSearch createAuctionSearch(AuctionSearchCriterion criteria) {
        //TODO: have some way to identify different criteria which produces the same result
        log.info("Saving auction search with criteria {}...", criteria);
        final AuctionSearch auctionSearch = auctionSearchRepository.save(
                new AuctionSearch(
                        UUID.randomUUID(),
                        criteria
                )
        );
        log.info("Saved auction search");
        return auctionSearch;
    }

    @Override
    public List<Auction> getAuctionSearchResults(
            UUID auctionSearchId,
            int limit,
            int offset,
            SortableField sortBy,
            Sort.Direction orderBy
    ) throws AuctionSearchNotFoundException {
        log.info(
                "Finding auctions with auction search {} limit {} offset {} sortBy {} orderBy {} ...",
                auctionSearchId, limit, offset, sortBy, orderBy
        );

        Preconditions.checkArgument(limit > 0 && limit <= 1000, INVALID_AMOUNT_OF_AUCTIONS_FETCHED_MSG);
        Preconditions.checkArgument(offset >= 0, INVALID_OFF_SET);

        log.info("Finding auction search with id {} ...", auctionSearchId);
        final AuctionSearch auctionSearch = getAuctionSearch(auctionSearchId);
        log.info("Found auction search with id {}", auctionSearchId);

//        log.info("Finding auctions on cache with search id {} ...", auctionSearchId);
//        final List<Auction> cachedAuctions = auctionSearchCacheService.getAuctions(auctionSearchId, limit, offset, sortBy, orderBy);

//        if (cachedAuctions != null){
//            log.info("Found auctions on cache with search id {}", auctionSearchId);
//            return cachedAuctions;
//        } else {
            log.info("Did not found auctions on cache with search id {}, querying db...", auctionSearchId);
            final Criteria criteria = Criteria
                    .where("sold")
                    .is(true)
                    .andOperator(buildCriteria(auctionSearch.getCriteria()));
            final Query query = new Query(criteria).with(Sort.by(orderBy, sortBy.getFieldName())).skip(offset).limit(limit);

            final List<Auction> auctions = mongoTemplate.find(query, Auction.class);
            log.info("Found {} auctions on db with search id {}, caching results...", auctions.size(), auctionSearchId);
            return auctions;
//            auctionSearchCacheService.cacheAuctions(auctionSearchId, auctions, sortBy, orderBy);
//            log.info("Cached auctions for search id {}", auctionSearchId);
//            return auctions.subList(
//                    Math.min(offset, auctions.size()),
//                    Math.min(offset + limit, auctions.size())
//            );
//        }
    }

    private List<Criteria> buildCriteria(List<AuctionSearchCriterion> auctionSearchCriterionList){
        return auctionSearchCriterionList.stream().map(this::buildCriteria).toList();
    }

    private Criteria buildCriteria(AuctionSearchCriterion auctionSearchCriterion){
        final Operator operator = auctionSearchCriterion.getOperator();
        final List<AuctionSearchCriterion> criterias = auctionSearchCriterion.getCriterias();
        if (operator == Operator.AND){
            return new Criteria().andOperator(buildCriteria(criterias));
        } else if (operator == Operator.OR){
            return new Criteria().orOperator(buildCriteria(criterias));
        } else if (operator == Operator.ELEMENT_MATCH){
            return Criteria
                    .where(auctionSearchCriterion.getField())
                    .elemMatch(new Criteria().andOperator(buildCriteria(criterias)));
        } else {
            return buildFieldCriteria(auctionSearchCriterion);
        }
    }

    private Criteria buildFieldCriteria(AuctionSearchCriterion auctionSearchCriterion){
        final String field = auctionSearchCriterion.getField();
        final Operator operator = auctionSearchCriterion.getOperator();
        final Object firstValue = auctionSearchCriterion.getFirstValue();
        final List<Object> allValues = auctionSearchCriterion.getValues();
        final Criteria criteria = Criteria.where(field);
        return switch (operator){
            case EQUALS -> criteria.is(firstValue);
            case NOT_EQUALS -> criteria.ne(firstValue);
            case GREATER_THAN -> criteria.gt(firstValue);
            case GREATER_OR_EQUALS_THAN -> criteria.gte(firstValue);
            case LESS_THAN -> criteria.lt(firstValue);
            case LESS_OR_EQUALS_THAN -> criteria.lte(firstValue);
            case IN -> criteria.in(allValues);
            case NOT_IN -> criteria.nin(allValues);
            case BETWEEN -> criteria.gte(firstValue).lte(allValues.get(1));
            case REGEX -> criteria.regex(String.valueOf(firstValue));
            default -> throw new RuntimeException("Operator not supported: " + operator);
        };
    }
}
