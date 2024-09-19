package com.tibiaauctionhistory.service.auctionsearch.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.google.common.base.Preconditions;
import com.tibiaauctionhistory.service.auctionsearch.exception.AuctionSearchNotFoundException;
import com.tibiaauctionhistory.service.auctionsearch.model.AuctionSearch;
import com.tibiaauctionhistory.service.auctionsearch.model.AuctionSearchCriterion;
import com.tibiaauctionhistory.service.auctionsearch.model.Operator;
import com.tibiaauctionhistory.service.auctionsearch.repository.AuctionSearchRepository;
import com.tibiaauctionhistory.module.common.model.Auction;
import com.tibiaauctionhistory.module.common.model.AuctionDTO;
import com.tibiaauctionhistory.module.common.model.SortableField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tibiaauctionhistory.module.common.model.Auction.NOT_INDEXED_FIELDS;

@Service
@Slf4j
public class AuctionSearchServiceImpl implements AuctionSearchService {

    public static final String INVALID_AMOUNT_OF_AUCTIONS_FETCHED_MSG =
            "The amount of auctions fetched per request should be between 1 and 1000.";
    public static final String INVALID_OFFSET =
            "The offset must be non negative.";
    public static final String AUCTION_SEARCH_NOT_FOUND_MSG = "Auction search not found.";
    public static final String AUCTION_SEARCH_ID_NOT_NULL_MSG = "Auction search id must not be null.";
    public static final String AUCTION_SEARCH_NOT_NULL_MSG = "Auction search must not be null.";

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
    public AuctionSearch getAuctionSearch(String id) throws AuctionSearchNotFoundException {
        Preconditions.checkArgument(id != null, AUCTION_SEARCH_ID_NOT_NULL_MSG);

        log.info("Finding auction search with id {}...", id);
        final AuctionSearch auctionSearch = auctionSearchRepository.findById(id)
                .orElseThrow(() -> new AuctionSearchNotFoundException(AUCTION_SEARCH_NOT_FOUND_MSG));
        log.info("Found auction search with id {}", id);
        return auctionSearch;
    }

    @Override
    public AuctionSearch createAuctionSearch(AuctionSearchCriterion criteria) {
        Preconditions.checkArgument(criteria != null, AUCTION_SEARCH_NOT_NULL_MSG);

        //TODO: have some way to identify different criteria which produces the same result
        log.info("Saving auction search with criteria {}...", criteria);
        final AuctionSearch auctionSearch = auctionSearchRepository.save(
                new AuctionSearch(
                        NanoIdUtils.randomNanoId(),
                        criteria
                )
        );
        log.info("Saved auction search");
        return auctionSearch;
    }

    @Override
    public List<AuctionDTO> getAuctionSearchResults(
            String auctionSearchId,
            int limit,
            int offset,
            SortableField sortBy,
            Sort.Direction orderBy
    ) throws AuctionSearchNotFoundException {
        log.info(
                "Finding auctions with auction search {} limit {} offset {} sortBy {} orderBy {} ...",
                auctionSearchId, limit, offset, sortBy, orderBy
        );

        Preconditions.checkArgument(auctionSearchId != null, AUCTION_SEARCH_ID_NOT_NULL_MSG);
        Preconditions.checkArgument(limit > 0 && limit <= 1000, INVALID_AMOUNT_OF_AUCTIONS_FETCHED_MSG);
        Preconditions.checkArgument(offset >= 0, INVALID_OFFSET);

        final AuctionSearch auctionSearch = getAuctionSearch(auctionSearchId);
        final boolean usesNotIndexedField = auctionSearch.getCriteria().usesAnyField(NOT_INDEXED_FIELDS);

        if (usesNotIndexedField){
            return getAuctionSearchResultsUsingNonIndexedFields(auctionSearch, limit, offset, sortBy, orderBy);
        } else {
            return getAuctionSearchResultsUsingIndexedFieldsOnly(auctionSearch, limit, offset, sortBy, orderBy);
        }
    }

    private List<AuctionDTO> getAuctionSearchResultsUsingNonIndexedFields(
            AuctionSearch auctionSearch,
            int limit,
            int offset,
            SortableField sortBy,
            Sort.Direction orderBy
    ){
        log.info("Finding auctions using non indexed fields with search id {}", auctionSearch.getId());

        log.info("Finding auctions on cache with search id {} ...", auctionSearch.getId());
        final List<Integer> cachedAuctionsIds = auctionSearchCacheService.getAuctionsIds(
                auctionSearch.getId(), limit, offset, sortBy, orderBy
        );

        if (cachedAuctionsIds != null){
            log.info("Found auctions on cache with search id {}", auctionSearch.getId());
            return findAuctionsDTOById(cachedAuctionsIds);
        } else {
            log.info("Did not find auctions on cache with search id {}, querying db for ids...", auctionSearch.getId());
            final Criteria criteria = Criteria
                    .where("sold")
                    .is(true)
                    .andOperator(buildCriteria(auctionSearch.getCriteria()));
            final Query query = new Query(criteria)
                    .with(Sort.by(orderBy, sortBy.getFieldName()));
            query.fields().include("_id");
            final List<Integer> auctionsIds = mongoTemplate.find(query, Auction.class, Auction.AUCTIONS_COLLECTION_NAME)
                    .stream()
                    .map(Auction::getId)
                    .toList();

            log.info("Found {} auctions ids on db for search id {}, caching results...", auctionsIds.size(), auctionSearch.getId());
            auctionSearchCacheService.cacheAuctionsIds(auctionSearch.getId(), auctionsIds, sortBy, orderBy);
            log.info("Cached auctions for search id {}", auctionSearch.getId());

            final List<Integer> auctionsToReturn = auctionsIds.subList(
                    Math.min(offset, auctionsIds.size()),
                    Math.min(offset + limit, auctionsIds.size())
            );
            return findAuctionsDTOById(auctionsToReturn);
        }
    }

    private List<AuctionDTO> getAuctionSearchResultsUsingIndexedFieldsOnly(
            AuctionSearch auctionSearch,
            int limit,
            int offset,
            SortableField sortBy,
            Sort.Direction orderBy
    ){
        log.info("Finding auctions using indexed fields only with search id {}", auctionSearch.getId());
        final Criteria criteria = Criteria
                .where("sold")
                .is(true)
                .andOperator(buildCriteria(auctionSearch.getCriteria()));
        final Query query = new Query(criteria)
                .with(Sort.by(orderBy, sortBy.getFieldName()))
                .skip(offset)
                .limit(limit);
        final boolean usesAnyArrayField = auctionSearch.getCriteria().usesAnyField(Auction.ALL_ARRAY_FIELDS);
        if (! usesAnyArrayField){
            query.withHint(Auction.NON_ARRAY_FIELDS_INDEX_NAME);
        }
        final List<AuctionDTO> auctions = mongoTemplate.find(query, AuctionDTO.class, Auction.AUCTIONS_COLLECTION_NAME);
        log.info("Found auctions on db with search id {}", auctionSearch.getId());
        return auctions;
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

    private List<AuctionDTO> findAuctionsDTOById(List<Integer> ids){
        log.info("Finding auctions by ids...");
        final Criteria criteria = Criteria.where("_id").in(ids);
        final Query query = new Query(criteria);
        final List<AuctionDTO> auctions = mongoTemplate.find(query, AuctionDTO.class, Auction.AUCTIONS_COLLECTION_NAME);
        log.info("Found auctions by ids");
        return auctions;
    }
}
