package com.matheusmr.tibiaauctionhistory.common.service;

import com.matheusmr.tibiaauctionhistory.auctionsearch.model.SortableField;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionDomainDTO;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionsResult;
import org.springframework.data.domain.Sort;

public interface AuctionService {

    AuctionsResult getAuctions(
            int limit,
            int offset,
            SortableField sortBy,
            Sort.Direction orderBy
    );

    AuctionDomainDTO getDomain();
}
