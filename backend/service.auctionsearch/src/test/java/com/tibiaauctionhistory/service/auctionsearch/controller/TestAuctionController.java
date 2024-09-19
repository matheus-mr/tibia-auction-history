package com.tibiaauctionhistory.service.auctionsearch.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tibiaauctionhistory.module.common.model.AuctionDTO;
import com.tibiaauctionhistory.service.auctionsearch.AbstractIntegrationTests;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestAuctionController extends AbstractIntegrationTests {

    @Test
    public void testFetchingAllAuctions() throws Exception {
        final String response = mockMvc.perform(get("/api/v1/auctions").queryParam("sortBy","name"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        final List<AuctionDTO> auctions = objectMapper.readValue(response, new TypeReference<>() {});

        assertEquals(3, auctions.size());
        assertEquals(36, auctions.get(0).id());
        assertEquals(33, auctions.get(1).id());
        assertEquals(23, auctions.get(2).id());
    }
}
