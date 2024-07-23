package com.tibiaauctionhistory.module.auctionsearch.mapper;

import com.tibiaauctionhistory.module.auctionsearch.model.AuctionSearch;
import com.tibiaauctionhistory.module.auctionsearch.model.AuctionSearchDTO;
import org.mapstruct.Mapper;

@Mapper
public interface AuctionSearchMapper {

    AuctionSearchDTO mapToAuctionSearchDTO(AuctionSearch auctionSearch);
}
