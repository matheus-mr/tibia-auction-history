package com.matheusmr.tibiaauctionhistory.auctionsearch.mapper;

import com.matheusmr.tibiaauctionhistory.auctionsearch.model.AuctionSearch;
import com.matheusmr.tibiaauctionhistory.auctionsearch.model.AuctionSearchDTO;
import org.mapstruct.Mapper;

@Mapper
public interface AuctionSearchMapper {

    AuctionSearchDTO mapToAuctionSearchDTO(AuctionSearch auctionSearch);
}
