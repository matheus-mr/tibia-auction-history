package com.matheusmr.tibiaauctionhistory.auctionfetch.service;

import com.matheusmr.tibiaauctionhistory.common.entity.Auction;

import java.io.IOException;

public interface AuctionFetchService {

    Auction fetchAuction(int id) throws IOException;
}
