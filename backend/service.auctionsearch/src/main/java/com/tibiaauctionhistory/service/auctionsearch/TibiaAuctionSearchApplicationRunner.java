package com.tibiaauctionhistory.service.auctionsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
        "com.tibiaauctionhistory.service.auctionsearch",
        "com.tibiaauctionhistory.module.auctiondomain",
        "com.tibiaauctionhistory.module.common",
        "com.tibiaauctionhistory.module.mongodb",
        "com.tibiaauctionhistory.module.redis"
})
public class TibiaAuctionSearchApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(TibiaAuctionSearchApplicationRunner.class, args);
    }
}
