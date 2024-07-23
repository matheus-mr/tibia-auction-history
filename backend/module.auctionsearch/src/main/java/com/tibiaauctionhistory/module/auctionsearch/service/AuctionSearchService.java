package com.tibiaauctionhistory.module.auctionsearch.service;

import com.tibiaauctionhistory.module.auctionsearch.exception.AuctionSearchNotFoundException;
import com.tibiaauctionhistory.module.auctionsearch.model.AuctionSearch;
import com.tibiaauctionhistory.module.auctionsearch.model.AuctionSearchCriterion;
import com.tibiaauctionhistory.module.common.model.Auction;
import com.tibiaauctionhistory.module.common.model.SortableField;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface AuctionSearchService {

    AuctionSearch createAuctionSearch(AuctionSearchCriterion criteria);

    AuctionSearch getAuctionSearch(UUID id) throws AuctionSearchNotFoundException;

    List<Auction> getAuctionSearchResults(
            UUID auctionSearchId,
            int limit,
            int offset,
            SortableField sortBy,
            Sort.Direction orderBy
    ) throws AuctionSearchNotFoundException;
}
