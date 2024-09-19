package com.tibiaauctionhistory.module.auctiondomain.service;

import com.tibiaauctionhistory.module.auctiondomain.model.AuctionDomainDTO;
import com.tibiaauctionhistory.module.common.model.Vocation;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
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
        String aggregationPipeline =
                """
                    {
                    aggregate: "auctions",
                    pipeline : [
                        {
                          $match: {
                            inErrorState: false
                          }
                        },
                        {
                            $facet: {
                              worlds: [{ $group: { _id: "$world" } }, { $sort: { _id: 1 } }],
                              charms: [
                                { $unwind: "$charms" },
                                { $group: { _id: "$charms" } },
                                { $addFields: { lowerCaseName: { $toLower: "$_id" } } },
                                { $sort: { lowerCaseName: 1 } }
                              ],
                              items: [
                                { $unwind: "$items" },
                                { $group: { _id: "$items.name" } },
                                { $addFields: { lowerCaseName: { $toLower: "$_id" } } },
                                { $sort: { lowerCaseName: 1 } }
                              ],
                              storeItems: [
                                { $unwind: "$storeItems" },
                                { $group: { _id: "$storeItems.name" } },
                                { $addFields: { lowerCaseName: { $toLower: "$_id" } } },
                                { $sort: { lowerCaseName: 1 } }
                              ],
                              mounts: [
                                { $unwind: "$mounts" },
                                { $group: { _id: "$mounts" } },
                                { $addFields: { lowerCaseName: { $toLower: "$_id" } } },
                                { $sort: { lowerCaseName: 1 } }
                              ],
                              storeMounts: [
                                { $unwind: "$storeMounts" },
                                { $group: { _id: "$storeMounts" } },
                                { $addFields: { lowerCaseName: { $toLower: "$_id" } } },
                                { $sort: { lowerCaseName: 1 } }
                              ],
                              outfits: [
                                { $unwind: "$outfits" },
                                { $group: { _id: "$outfits.name" } },
                                { $addFields: { lowerCaseName: { $toLower: "$_id" } } },
                                { $sort: { lowerCaseName: 1 } }
                              ],
                              storeOutfits: [
                                { $unwind: "$storeOutfits" },
                                { $group: { _id: "$storeOutfits.name" } },
                                { $addFields: { lowerCaseName: { $toLower: "$_id" } } },
                                { $sort: { lowerCaseName: 1 } }
                              ],
                              imbuements: [
                                { $unwind: "$imbuements" },
                                { $group: { _id: "$imbuements" } },
                                { $addFields: { lowerCaseName: { $toLower: "$_id" } } },
                                { $sort: { lowerCaseName: 1 } }
                              ],
                              completedQuestLines: [
                                { $unwind: "$completedQuestLines" },
                                { $group: { _id: "$completedQuestLines" } },
                                { $addFields: { lowerCaseName: { $toLower: "$_id" } } },
                                { $sort: { lowerCaseName: 1 } }
                              ],
                              titles: [
                                { $unwind: "$titles" },
                                { $group: { _id: "$titles" } },
                                { $addFields: { lowerCaseName: { $toLower: "$_id" } } },
                                { $sort: { lowerCaseName: 1 } }
                              ],
                              achievements: [
                                { $unwind: "$achievements" },
                                { $group: { _id: "$achievements" } },
                                { $addFields: { lowerCaseName: { $toLower: "$_id" } } },
                                { $sort: { lowerCaseName: 1 } }
                              ],
                            }
                        },
                        {
                            $project: {
                              _id: { $literal: "%s" },
                              worlds: {
                                $map: {
                                  input: "$worlds",
                                  in: "$$this._id"
                                }
                              },
                              charms: {
                                $map: {
                                  input: "$charms",
                                  in: "$$this._id"
                                }
                              },
                              items: {
                                $map: {
                                  input: "$items",
                                  in: "$$this._id"
                                }
                              },
                              storeItems: {
                                $map: {
                                  input: "$storeItems",
                                  in: "$$this._id"
                                }
                              },
                              mounts: {
                                $map: {
                                  input: "$mounts",
                                  in: "$$this._id"
                                }
                              },
                              storeMounts: {
                                $map: {
                                  input: "$storeMounts",
                                  in: "$$this._id"
                                }
                              },
                              outfits: {
                                $map: {
                                  input: "$outfits",
                                  in: "$$this._id"
                                }
                              },
                              storeOutfits: {
                                $map: {
                                  input: "$storeOutfits",
                                  in: "$$this._id"
                                }
                              },
                              imbuements: {
                                $map: {
                                  input: "$imbuements",
                                  in: "$$this._id"
                                }
                              },
                              completedQuestLines: {
                                $map: {
                                  input: "$completedQuestLines",
                                  in: "$$this._id"
                                }
                              },
                              titles: {
                                $map: {
                                  input: "$titles",
                                  in: "$$this._id"
                                }
                              },
                              achievements: {
                                $map: {
                                  input: "$achievements",
                                  in: "$$this._id"
                                }
                              },
                            }
                        },
                        { $merge: { into: "%s", whenMatched: "replace" } }
                    ],
                    cursor: {},
                    }
                """.formatted(AUCTION_DOMAIN_ID, AUCTION_DOMAIN_VIEW);

        Document pipeline = Document.parse(aggregationPipeline);
        mongoTemplate.executeCommand(pipeline);
    }
}
