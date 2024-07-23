package com.tibiaauctionhistory.module.auctiondomain.service;

import com.tibiaauctionhistory.module.auctiondomain.model.AuctionDomainDTO;

public interface AuctionDomainService {

    AuctionDomainDTO getAuctionDomain();

    void createOrRefreshAuctionDomainView();
}
