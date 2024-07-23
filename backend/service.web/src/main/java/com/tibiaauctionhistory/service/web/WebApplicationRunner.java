package com.tibiaauctionhistory.service.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
        "com.tibiaauctionhistory.module.auctionsearch",
        "com.tibiaauctionhistory.module.common",
        "com.tibiaauctionhistory.module.mongodb",
        "com.tibiaauctionhistory.module.redis"
})
public class WebApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(WebApplicationRunner.class, args);
    }
}