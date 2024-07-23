package com.tibiaauctionhistory.service.web.controller;

import com.tibiaauctionhistory.module.auctionsearch.mapper.AuctionSearchMapper;
import com.tibiaauctionhistory.module.auctionsearch.model.AuctionSearchCriterion;
import com.tibiaauctionhistory.module.auctionsearch.model.AuctionSearchDTO;
import com.tibiaauctionhistory.module.auctionsearch.service.AuctionSearchService;
import com.tibiaauctionhistory.module.common.mapper.AuctionMapper;
import com.tibiaauctionhistory.module.common.model.AuctionDTO;
import com.tibiaauctionhistory.module.common.model.SortableField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
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
    public List<AuctionDTO> getAuctionSearchResults(
            @PathVariable UUID id,
            @RequestParam(required = false, defaultValue = "100") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "AUCTION_END") SortableField sortBy,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction orderBy
    ){
        return auctionMapper.mapToDTOList(
                auctionSearchService.getAuctionSearchResults(
                        id, limit, offset, sortBy, orderBy
                )
        );
    }
}
