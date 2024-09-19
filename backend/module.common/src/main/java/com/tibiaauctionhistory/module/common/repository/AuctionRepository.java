package com.tibiaauctionhistory.module.common.repository;

import com.tibiaauctionhistory.module.common.model.Auction;
import org.bson.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public interface AuctionRepository extends MongoRepository<Auction, Integer> {

    @Query(value = "{}", fields = "{ '_id': 1 }")
    List<Document> findAllIdsDocuments();

    default Set<Integer> findAllIds(){
        return findAllIdsDocuments().stream().map(doc -> (int) doc.get("_id")).collect(Collectors.toSet());
    }
}
