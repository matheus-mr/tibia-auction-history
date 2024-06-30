package com.matheusmr.tibiaauctionhistory.auctionsearch.controller;

import com.matheusmr.tibiaauctionhistory.auctionsearch.mapper.AuctionSearchMapper;
import com.matheusmr.tibiaauctionhistory.auctionsearch.model.AuctionSearchCriterion;
import com.matheusmr.tibiaauctionhistory.auctionsearch.model.AuctionSearchDTO;
import com.matheusmr.tibiaauctionhistory.auctionsearch.model.SortableField;
import com.matheusmr.tibiaauctionhistory.auctionsearch.service.AuctionSearchService;
import com.matheusmr.tibiaauctionhistory.common.mapper.AuctionMapper;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionDTO;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000", exposedHeaders = { "X-Total-Count" })
@RequestMapping("/api/v1/auctions/search")
public class AuctionSearchController {

    private final AuctionSearchService auctionSearchService;
    private final AuctionMapper auctionMapper;
    private final AuctionSearchMapper auctionSearchMapper;

    @Autowired
    public AuctionSearchController(AuctionSearchService auctionSearchService,
                                   AuctionMapper auctionMapper,
                                   AuctionSearchMapper auctionSearchMapper) {
        this.auctionSearchService = auctionSearchService;
        this.auctionMapper = auctionMapper;
        this.auctionSearchMapper = auctionSearchMapper;
    }

    @PostMapping
    public ResponseEntity<UUID> createAuctionSearch(@RequestBody AuctionSearchCriterion criteria){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(auctionSearchService.createAuctionSearch(criteria).getId());
    }

    @GetMapping("/{id}")
    public AuctionSearchDTO getAuctionSearch(@PathVariable("id") UUID id){
        return auctionSearchMapper.mapToAuctionSearchDTO(auctionSearchService.getAuctionSearch(id));
    }

    @GetMapping("/{id}/results")
    public ResponseEntity<List<AuctionDTO>> getAuctionSearchResults(
            @PathVariable UUID id,
            @RequestParam(required = false, defaultValue = "100") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "AUCTION_END") SortableField sortBy,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction orderBy
    ){
        final AuctionsResult auctionSearchResults = auctionSearchService.getAuctionSearchResults(
                id, limit, offset, sortBy, orderBy
        );

        return ResponseEntity
                .ok()
                .header("X-Total-Count", String.valueOf(auctionSearchResults.totalCount()))
                .body(auctionMapper.mapToDTOList(auctionSearchResults.auctions()));
    }
}
