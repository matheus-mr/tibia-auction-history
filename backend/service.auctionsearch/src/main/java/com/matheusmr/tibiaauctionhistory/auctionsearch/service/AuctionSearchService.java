package com.matheusmr.tibiaauctionhistory.auctionsearch.service;

import com.matheusmr.tibiaauctionhistory.auctionsearch.exception.AuctionSearchNotFoundException;
import com.matheusmr.tibiaauctionhistory.auctionsearch.model.AuctionSearch;
import com.matheusmr.tibiaauctionhistory.auctionsearch.model.AuctionSearchCriterion;
import com.matheusmr.tibiaauctionhistory.common.model.Auction;
import com.matheusmr.tibiaauctionhistory.common.model.SortableField;
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
