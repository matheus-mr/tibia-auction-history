package com.matheusmr.tibiaauctionhistory.auctionfetch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan({
		"com.matheusmr.tibiaauctionhistory.auctionfetch",
		"com.matheusmr.tibiaauctionhistory.common",
		"com.matheusmr.tibiaauctionhistory.mongodb"
})
public class TibiaAuctionFetchApplication {

	public static void main(String[] args) {
		SpringApplication.run(TibiaAuctionFetchApplication.class, args);
	}

}
