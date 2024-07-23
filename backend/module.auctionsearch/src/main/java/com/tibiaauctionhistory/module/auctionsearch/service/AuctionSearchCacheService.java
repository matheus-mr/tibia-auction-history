package com.tibiaauctionhistory.module.auctionsearch.service;

import com.tibiaauctionhistory.module.common.model.Auction;
import com.tibiaauctionhistory.module.common.model.SortableField;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface AuctionSearchCacheService {

    List<Auction> getAuctions(UUID searchId, int limit, int offset, SortableField sortBy, Sort.Direction orderBy);

    void cacheAuctions(UUID searchId, List<Auction> auctions, SortableField sortBy, Sort.Direction orderBy);
}
