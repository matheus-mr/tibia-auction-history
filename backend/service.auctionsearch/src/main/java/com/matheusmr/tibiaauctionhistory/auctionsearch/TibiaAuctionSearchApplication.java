package com.matheusmr.tibiaauctionhistory.auctionsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan({
		"com.matheusmr.tibiaauctionhistory.auctionsearch",
		"com.matheusmr.tibiaauctionhistory.common",
		"com.matheusmr.tibiaauctionhistory.mongodb",
		"com.matheusmr.tibiaauctionhistory.redis"
})
public class TibiaAuctionSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(TibiaAuctionSearchApplication.class, args);
	}

}
