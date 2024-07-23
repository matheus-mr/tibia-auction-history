package com.matheusmr.tibiaauctionhistory.auctiondomain.service;

import com.matheusmr.tibiaauctionhistory.auctiondomain.model.AuctionDomainDTO;

public interface AuctionDomainService {

    AuctionDomainDTO getAuctionDomain();

    void createOrRefreshAuctionDomainView();
}
