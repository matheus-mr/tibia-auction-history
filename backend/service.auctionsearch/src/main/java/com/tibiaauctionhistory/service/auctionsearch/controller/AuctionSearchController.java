package com.tibiaauctionhistory.service.auctionsearch.controller;

import com.tibiaauctionhistory.service.auctionsearch.mapper.AuctionSearchMapper;
import com.tibiaauctionhistory.service.auctionsearch.model.AuctionSearchCriterion;
import com.tibiaauctionhistory.service.auctionsearch.model.AuctionSearchDTO;
import com.tibiaauctionhistory.service.auctionsearch.service.AuctionSearchService;
import com.tibiaauctionhistory.module.common.model.AuctionDTO;
import com.tibiaauctionhistory.module.common.model.SortableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/auctions/search")
public class AuctionSearchController {

    private final AuctionSearchService auctionSearchService;
    private final AuctionSearchMapper auctionSearchMapper;

    @Autowired
    public AuctionSearchController(AuctionSearchService auctionSearchService,
                                   AuctionSearchMapper auctionSearchMapper) {
        this.auctionSearchService = auctionSearchService;
        this.auctionSearchMapper = auctionSearchMapper;
    }

    @PostMapping
    public ResponseEntity<String> createAuctionSearch(@RequestBody AuctionSearchCriterion criteria){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(auctionSearchService.createAuctionSearch(criteria).getId());
    }

    @GetMapping("/{id}")
    public AuctionSearchDTO getAuctionSearch(@PathVariable("id") String id){
        return auctionSearchMapper.mapToAuctionSearchDTO(auctionSearchService.getAuctionSearch(id));
    }

    @GetMapping("/{id}/results")
    public List<AuctionDTO> getAuctionSearchResults(
            @PathVariable String id,
            @RequestParam(required = false, defaultValue = "100") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "AUCTION_END") SortableField sortBy,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction orderBy
    ){
        return auctionSearchService.getAuctionSearchResults(
                id, limit, offset, sortBy, orderBy
        );
    }
}
