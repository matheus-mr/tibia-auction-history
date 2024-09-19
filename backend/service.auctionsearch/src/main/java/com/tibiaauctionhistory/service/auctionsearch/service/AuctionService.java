package com.tibiaauctionhistory.service.auctionsearch.service;

import com.tibiaauctionhistory.module.common.model.AuctionDTO;
import com.tibiaauctionhistory.module.common.model.SortableField;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AuctionService {

    List<AuctionDTO> getAuctions(
            int limit,
            int offset,
            SortableField sortBy,
            Sort.Direction orderBy
    );
}
