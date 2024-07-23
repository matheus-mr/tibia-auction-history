package com.tibiaauctionhistory.service.auctiondomainupdater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
        "com.tibiaauctionhistory.module.auctiondomain",
        "com.tibiaauctionhistory.service.auctiondomainupdater",
})
public class AuctionDomainUpdaterApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(AuctionDomainUpdaterApplicationRunner.class, args);
    }
}