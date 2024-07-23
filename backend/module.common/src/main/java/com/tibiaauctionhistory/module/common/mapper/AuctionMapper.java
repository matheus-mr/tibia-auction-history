package com.tibiaauctionhistory.module.common.mapper;

import com.tibiaauctionhistory.module.common.model.Auction;
import com.tibiaauctionhistory.module.common.model.AuctionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface AuctionMapper {

    @Mapping(
            target = "vocation",
            expression = "java(auction.getVocation() == null ? null : auction.getVocation().getName())"
    )
    AuctionDTO mapToDTO(Auction auction);

    List<AuctionDTO> mapToDTOList(Iterable<Auction> auctions);
}
