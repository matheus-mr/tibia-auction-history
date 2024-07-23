package com.tibiaauctionhistory.service.auctiondomainupdater;

import com.tibiaauctionhistory.module.auctiondomain.service.AuctionDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuctionDomainUpdaterCommandLineRunner implements CommandLineRunner {

    private final AuctionDomainService auctionDomainService;

    @Autowired
    public AuctionDomainUpdaterCommandLineRunner(AuctionDomainService auctionDomainService) {
        this.auctionDomainService = auctionDomainService;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("Creating/updating auction domain view...");
            auctionDomainService.createOrRefreshAuctionDomainView();
            log.info("Auction domain view successfully created/updated.");
        } catch (Exception e){
            log.error("Error while creating/updating auction domain view", e);
            throw new RuntimeException(e);
        }
    }
}
