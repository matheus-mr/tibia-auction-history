package com.matheusmr.tibiaauctionhistory.auctionsearch.service;

import com.matheusmr.tibiaauctionhistory.auctionsearch.exception.AuctionSearchNotFoundException;
import com.matheusmr.tibiaauctionhistory.auctionsearch.model.AuctionSearch;
import com.matheusmr.tibiaauctionhistory.auctionsearch.model.AuctionSearchCriterion;
import com.matheusmr.tibiaauctionhistory.auctionsearch.model.SortableField;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionsResult;
import org.springframework.data.domain.Sort;

import java.util.UUID;

public interface AuctionSearchService {

    AuctionSearch createAuctionSearch(AuctionSearchCriterion criteria);

    AuctionSearch getAuctionSearch(UUID id) throws AuctionSearchNotFoundException;

    AuctionsResult getAuctionSearchResults(
            UUID auctionSearchId,
            int limit,
            int offset,
            SortableField sortBy,
            Sort.Direction orderBy
    ) throws AuctionSearchNotFoundException;
}
