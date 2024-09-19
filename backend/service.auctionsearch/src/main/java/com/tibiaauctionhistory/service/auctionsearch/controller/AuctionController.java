package com.tibiaauctionhistory.service.auctionsearch.controller;

import com.tibiaauctionhistory.service.auctionsearch.service.AuctionService;
import com.tibiaauctionhistory.module.common.model.AuctionDTO;
import com.tibiaauctionhistory.module.common.model.SortableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/auctions")
public class AuctionController {

    private final AuctionService auctionService;

    @Autowired
    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @GetMapping
    public List<AuctionDTO> getAuctions(
            @RequestParam(required = false, defaultValue = "100") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "AUCTION_END") SortableField sortBy,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction orderBy
    ){
        return auctionService.getAuctions(limit, offset, sortBy, orderBy);
    }
}
