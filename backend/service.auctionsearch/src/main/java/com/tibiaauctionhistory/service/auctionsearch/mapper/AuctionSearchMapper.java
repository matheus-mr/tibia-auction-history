package com.tibiaauctionhistory.service.auctionsearch.mapper;

import com.tibiaauctionhistory.service.auctionsearch.model.AuctionSearch;
import com.tibiaauctionhistory.service.auctionsearch.model.AuctionSearchDTO;
import org.mapstruct.Mapper;

@Mapper
public interface AuctionSearchMapper {

    AuctionSearchDTO mapToAuctionSearchDTO(AuctionSearch auctionSearch);
}
