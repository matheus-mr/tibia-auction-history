package com.matheusmr.tibiaauctionhistory.auctionsearch.service;

import com.google.common.base.Preconditions;
import com.matheusmr.tibiaauctionhistory.auctionsearch.exception.AuctionSearchNotFoundException;
import com.matheusmr.tibiaauctionhistory.auctionsearch.model.*;
import com.matheusmr.tibiaauctionhistory.auctionsearch.repository.AuctionSearchRepository;
import com.matheusmr.tibiaauctionhistory.common.model.Auction;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static com.matheusmr.tibiaauctionhistory.auctionsearch.model.Operator.*;

@Service
public class AuctionSearchServiceImpl implements AuctionSearchService {

    public static final String INVALID_AMOUNT_OF_AUCTIONS_FETCHED_MSG =
            "The amount of auctions fetched per request should be between 1 and 1000.";
    public static final String INVALID_OFF_SET =
            "The offset must be non negative.";
    public static final String AUCTION_SEARCH_NOT_FOUND_MSG = "Auction search not found.";

    private final AuctionSearchRepository auctionSearchRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public AuctionSearchServiceImpl(AuctionSearchRepository auctionSearchRepository,
                                    MongoTemplate mongoTemplate) {
        this.auctionSearchRepository = auctionSearchRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public AuctionSearch getAuctionSearch(UUID id) throws AuctionSearchNotFoundException {
        // TODO: save the current time as last usage of this search
        return auctionSearchRepository.findById(id)
                .orElseThrow(() -> new AuctionSearchNotFoundException(AUCTION_SEARCH_NOT_FOUND_MSG));
    }

    @Override
    public AuctionSearch createAuctionSearch(AuctionSearchCriterion criteria) {
        //TODO: have some way to identify different criteria which produces the same result
        return auctionSearchRepository.save(
                new AuctionSearch(
                        UUID.randomUUID(),
                        criteria,
                        LocalDateTime.now(ZoneId.of("UTC"))
                )
        );
    }

    @Override
    public AuctionsResult getAuctionSearchResults(
            UUID auctionSearchId,
            int limit,
            int offset,
            SortableField sortBy,
            Sort.Direction orderBy
    ) throws AuctionSearchNotFoundException {
        Preconditions.checkArgument(limit > 0 && limit <= 1000, INVALID_AMOUNT_OF_AUCTIONS_FETCHED_MSG);
        Preconditions.checkArgument(offset >= 0, INVALID_OFF_SET);

        final AuctionSearch auctionSearch = getAuctionSearch(auctionSearchId);

        final Criteria criteria = Criteria
                .where("sold")
                .is(true)
                .andOperator(buildCriteria(auctionSearch.getCriteria()));
        final Query query = new Query(criteria).with(Sort.by(orderBy, sortBy.getFieldName()));

        final List<Auction> auctions = mongoTemplate.find(query, Auction.class);

        return new AuctionsResult(
                auctions.subList(
                        Math.min(offset, auctions.size()),
                        Math.min(offset + limit, auctions.size())
                ),
                auctions.size()
        );
    }

    private List<Criteria> buildCriteria(List<AuctionSearchCriterion> auctionSearchCriterionList){
        return auctionSearchCriterionList.stream().map(this::buildCriteria).toList();
    }

    private Criteria buildCriteria(AuctionSearchCriterion auctionSearchCriterion){
        final Operator operator = auctionSearchCriterion.getOperator();
        final List<AuctionSearchCriterion> criterias = auctionSearchCriterion.getCriterias();
        if (operator == AND){
            return new Criteria().andOperator(buildCriteria(criterias));
        } else if (operator == OR){
            return new Criteria().orOperator(buildCriteria(criterias));
        } else if (operator == ELEMENT_MATCH){
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
