package com.matheusmr.tibiaauctionhistory.auctionsearch.service;

import com.matheusmr.tibiaauctionhistory.common.model.Auction;
import com.matheusmr.tibiaauctionhistory.common.model.SortableField;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionDomainDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AuctionService {

    List<Auction> getAuctions(
            int limit,
            int offset,
            SortableField sortBy,
            Sort.Direction orderBy
    );

    AuctionDomainDTO getDomain();
}
