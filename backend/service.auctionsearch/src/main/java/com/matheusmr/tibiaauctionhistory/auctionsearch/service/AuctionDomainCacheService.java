package com.matheusmr.tibiaauctionhistory.auctionsearch.service;

import com.matheusmr.tibiaauctionhistory.common.model.AuctionDomainDTO;

public interface AuctionDomainCacheService {

    AuctionDomainDTO getAuctionDomainDTO();

    void cacheAuctionDomainDTO(AuctionDomainDTO auctionDomainDTO);
}
