package com.tibiaauctionhistory.module.auctionsearch.service;

import com.tibiaauctionhistory.module.common.model.Auction;
import com.tibiaauctionhistory.module.common.model.SortableField;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AuctionService {

    List<Auction> getAuctions(
            int limit,
            int offset,
            SortableField sortBy,
            Sort.Direction orderBy
    );
}
