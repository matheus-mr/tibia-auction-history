package com.matheusmr.tibiaauctionhistory.auctionfetch.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("AUCTION_FETCHING")
public class AuctionFetchValidationServiceImpl implements AuctionFetchValidationService {

    @Override
    public void validateAuctionFetchingCorrectness() {

    }
}
