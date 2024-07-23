package com.tibiaauctionhistory.service.auctionfetch.service;

import com.tibiaauctionhistory.module.common.model.Auction;

import java.io.IOException;

public interface AuctionFetchService {

    Auction fetchAuction(int id) throws IOException;
}
