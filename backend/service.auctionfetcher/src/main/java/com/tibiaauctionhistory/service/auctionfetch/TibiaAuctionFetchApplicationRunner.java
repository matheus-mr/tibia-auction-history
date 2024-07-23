package com.tibiaauctionhistory.service.auctionfetch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
		"com.tibiaauctionhistory.module.auctionfetch",
		"com.tibiaauctionhistory.module.common",
		"com.tibiaauctionhistory.module.mongodb"
})
public class TibiaAuctionFetchApplication {

	public static void main(String[] args) {
		SpringApplication.run(TibiaAuctionFetchApplication.class, args);
	}

}
