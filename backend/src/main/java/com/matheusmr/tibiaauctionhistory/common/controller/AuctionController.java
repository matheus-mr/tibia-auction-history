package com.matheusmr.tibiaauctionhistory.common.controller;

import com.matheusmr.tibiaauctionhistory.auctionsearch.model.SortableField;
import com.matheusmr.tibiaauctionhistory.common.mapper.AuctionMapper;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionDTO;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionDomainDTO;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionsResult;
import com.matheusmr.tibiaauctionhistory.common.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<AuctionDTO>> getAuctions(
            @RequestParam(required = false, defaultValue = "100") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "AUCTION_END") SortableField sortBy,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction orderBy
    ){
        final AuctionsResult auctionsResult = auctionService.getAuctions(limit, offset, sortBy, orderBy);

        return ResponseEntity
                .ok()
                .header("X-Total-Count", String.valueOf(auctionsResult.totalCount()))
                .body(auctionMapper.mapToDTOList(auctionsResult.auctions()));
    }

    @GetMapping(value = "/domain")
    public AuctionDomainDTO getDomain(){
        return auctionService.getDomain();
    }
}
