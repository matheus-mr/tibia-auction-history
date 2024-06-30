package com.matheusmr.tibiaauctionhistory.common.service;

import com.matheusmr.tibiaauctionhistory.auctionsearch.model.SortableField;
import com.matheusmr.tibiaauctionhistory.common.model.Auction;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface AuctionCacheService {

    List<Auction> getAuctions(int limit, int offset, SortableField sortBy, Sort.Direction orderBy);

    void cacheAuctions(List<Auction> auctions, SortableField sortBy, Sort.Direction orderBy);

    Long countAuctions(SortableField sortBy, Sort.Direction orderBy);

    List<Auction> getAuctions(UUID searchId, int limit, int offset, SortableField sortBy, Sort.Direction orderBy);

    void cacheAuctions(UUID searchId, List<Auction> auctions, SortableField sortBy, Sort.Direction orderBy);

    Long countAuctions(UUID searchId, SortableField sortBy, Sort.Direction orderBy);
}
