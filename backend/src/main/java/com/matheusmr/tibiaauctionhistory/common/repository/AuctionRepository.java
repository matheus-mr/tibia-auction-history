package com.matheusmr.tibiaauctionhistory.common.repository;

import com.matheusmr.tibiaauctionhistory.common.model.Auction;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionDomainDTO;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends MongoRepository<Auction, Integer> {

    @Aggregation(pipeline = {
            "{ $match: { sold: true } }",
            """
            {
                $facet: {
                  worlds: [{ $group: { _id: "$world" } }, { $sort: { _id: 1 } }],
                  charms: [
                    { $unwind: "$charms" },
                    { $group: { _id: "$charms" } },
                    { $sort: { _id: 1 } }
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
            }
            """,
            """
            {
                $project: {
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
            }
            """
    })
    AuctionDomainDTO getDomain();
}
