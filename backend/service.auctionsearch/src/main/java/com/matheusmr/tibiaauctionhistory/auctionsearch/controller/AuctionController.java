package com.matheusmr.tibiaauctionhistory.auctionsearch.controller;

import com.matheusmr.tibiaauctionhistory.auctionsearch.service.AuctionService;
import com.matheusmr.tibiaauctionhistory.common.mapper.AuctionMapper;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionDTO;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionDomainDTO;
import com.matheusmr.tibiaauctionhistory.common.model.SortableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = { "X-Total-Count" })
@RequestMapping("/api/v1/auctions")
public class AuctionController {

    private final AuctionService auctionService;
    private final AuctionMapper auctionMapper;

    @Autowired
    public AuctionController(AuctionService auctionService, AuctionMapper auctionMapper) {
        this.auctionService = auctionService;
        this.auctionMapper = auctionMapper;
    }

    @GetMapping
    public List<AuctionDTO> getAuctions(
            @RequestParam(required = false, defaultValue = "100") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "AUCTION_END") SortableField sortBy,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction orderBy
    ){
        return auctionMapper.mapToDTOList(auctionService.getAuctions(limit, offset, sortBy, orderBy));
    }

    @GetMapping(value = "/domain")
    public AuctionDomainDTO getDomain(){
        return auctionService.getDomain();
    }
}
