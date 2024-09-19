package com.tibiaauctionhistory.service.auctionsearch.service;

import com.tibiaauctionhistory.service.auctionsearch.exception.AuctionSearchNotFoundException;
import com.tibiaauctionhistory.service.auctionsearch.model.AuctionSearch;
import com.tibiaauctionhistory.service.auctionsearch.model.AuctionSearchCriterion;
import com.tibiaauctionhistory.module.common.model.AuctionDTO;
import com.tibiaauctionhistory.module.common.model.SortableField;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AuctionSearchService {

    AuctionSearch createAuctionSearch(AuctionSearchCriterion criteria);

    AuctionSearch getAuctionSearch(String id) throws AuctionSearchNotFoundException;

    List<AuctionDTO> getAuctionSearchResults(
            String auctionSearchId,
            int limit,
            int offset,
            SortableField sortBy,
            Sort.Direction orderBy
    ) throws AuctionSearchNotFoundException;
}
