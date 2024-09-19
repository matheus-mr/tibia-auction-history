package com.tibiaauctionhistory.service.auctiondomain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
        "com.tibiaauctionhistory.module.auctiondomain",
        "com.tibiaauctionhistory.service.auctiondomain",
})
public class AuctionDomainUpdaterApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(AuctionDomainUpdaterApplicationRunner.class, args);
    }
}