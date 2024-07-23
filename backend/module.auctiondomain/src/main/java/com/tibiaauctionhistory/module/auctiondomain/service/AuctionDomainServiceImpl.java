package com.tibiaauctionhistory.module.auctiondomain.service;

import com.tibiaauctionhistory.module.auctiondomain.model.AuctionDomainDTO;
import com.tibiaauctionhistory.module.common.model.Auction;
import com.tibiaauctionhistory.module.common.model.Vocation;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AuctionDomainServiceImpl implements AuctionDomainService {

    private static final String AUCTION_DOMAIN_ID = "auction_domain";
    private static final String AUCTION_DOMAIN_VIEW = "auction_domain_view";
    private static final List<String> VOCATIONS = Arrays
            .stream(Vocation.values())
            .map(Vocation::name)
            .toList();

    private final MongoTemplate mongoTemplate;

    @Autowired
    public AuctionDomainServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public AuctionDomainDTO getAuctionDomain() {
        return mongoTemplate.findById(AUCTION_DOMAIN_ID, AuctionDomainDTO.class, AUCTION_DOMAIN_VIEW)
                .withVocations(VOCATIONS);
    }

    @Override
    public void createOrRefreshAuctionDomainView() {
        final MatchOperation matchOperation = Aggregation.match(Criteria.where("inErrorState").is(false));

        final FacetOperation facetOperation = Aggregation.facet()
                .and(Aggregation.group().addToSet("world").as("worlds")).as("worlds")
                .and(unwindAndGroupStringArrayField("charms")).as("charms")
                .and(unwindAndGroupStringArrayField("mounts")).as("mounts")
                .and(unwindAndGroupStringArrayField("storeMounts")).as("storeMounts")
                .and(unwindAndGroupStringArrayField("imbuements")).as("imbuements")
                .and(unwindAndGroupStringArrayField("completedQuestLines")).as("completedQuestLines")
                .and(unwindAndGroupStringArrayField("titles")).as("titles")
                .and(unwindAndGroupStringArrayField("achivements")).as("achievements")
                .and(unwindAndGroupNamedObjectArrayField("items")).as("items")
                .and(unwindAndGroupNamedObjectArrayField("storeItems")).as("storeItems")
                .and(unwindAndGroupNamedObjectArrayField("outfits")).as("outfits")
                .and(unwindAndGroupNamedObjectArrayField("storeOutfits")).as("storeOutfits");

        final ProjectionOperation projectionOperation = Aggregation.project()
                .and(ConvertOperators.ToString.toString(AUCTION_DOMAIN_ID)).as("_id")
                .and(projectField("worlds")).as("worlds")
                .and(projectField("charms")).as("charms")
                .and(projectField("mounts")).as("mounts")
                .and(projectField("storeMounts")).as("storeMounts")
                .and(projectField("imbuements")).as("imbuements")
                .and(projectField("completedQuestLines")).as("completedQuestLines")
                .and(projectField("titles")).as("titles")
                .and(projectField("achievements")).as("achievements")
                .and(projectField("items")).as("items")
                .and(projectField("storeItems")).as("storeItems")
                .and(projectField("outfits")).as("outfits")
                .and(projectField("storeOutfits")).as("storeOutfits");

        final OutOperation outOperation = Aggregation.out(AUCTION_DOMAIN_VIEW);

        mongoTemplate.aggregate(
                Aggregation.newAggregation(
                        matchOperation,
                        facetOperation,
                        projectionOperation,
                        outOperation
                ),
                Auction.class,
                Object.class
        );
    }

    private AggregationOperation[] unwindAndGroupStringArrayField(String fieldName) {
        return new AggregationOperation[]{
                Aggregation.unwind(fieldName),
                Aggregation.group().addToSet(fieldName).as(fieldName),
                context -> new Document("$set",
                        new Document(fieldName,
                                new Document("$sortArray",
                                        new Document("input",
                                                new Document("$map",
                                                        new Document("input", "$" + fieldName)
                                                                .append("in", new Document("$toLower", "$$this"))
                                                )
                                        )
                                                .append("sortBy", 1)
                                )
                        )
                )
        };
    }

    private AggregationOperation[] unwindAndGroupNamedObjectArrayField(String fieldName) {
        return new AggregationOperation[]{
                Aggregation.unwind(fieldName),
                Aggregation.group().addToSet("%s.name".formatted(fieldName)).as(fieldName),
                context -> new Document("$set",
                        new Document(fieldName,
                                new Document("$sortArray",
                                        new Document("input",
                                                new Document("$map",
                                                        new Document("input", "$" + fieldName)
                                                                .append("in", new Document("$toLower", "$$this"))
                                                )
                                        )
                                                .append("sortBy", 1)
                                )
                        )
                )
        };
    }

    private ArrayOperators.ArrayElemAt projectField(String fieldName){
        return ArrayOperators.ArrayElemAt.arrayOf("%s.%s".formatted(fieldName, fieldName)).elementAt(0);
    }
}
