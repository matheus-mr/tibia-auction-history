package com.tibiaauctionhistory.service.auctionsearch.service;

import com.tibiaauctionhistory.module.common.model.SortableField;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AuctionSearchCacheService {

    List<Integer> getAuctionsIds(String searchId, int limit, int offset, SortableField sortBy, Sort.Direction orderBy);

    void cacheAuctionsIds(String searchId, List<Integer> auctionsIds, SortableField sortBy, Sort.Direction orderBy);
}
