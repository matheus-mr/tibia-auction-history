package com.tibiaauctionhistory.service.auctionsearch.controller;

import com.tibiaauctionhistory.module.auctiondomain.model.AuctionDomainDTO;
import com.tibiaauctionhistory.module.auctiondomain.service.AuctionDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/auctions/domain")
public class AuctionDomainController {

    private final AuctionDomainService auctionDomainService;

    @Autowired
    public AuctionDomainController(AuctionDomainService auctionDomainService) {
        this.auctionDomainService = auctionDomainService;
    }

    @GetMapping
    public AuctionDomainDTO getAuctionDomain(){
        return auctionDomainService.getAuctionDomain();
    }
}
