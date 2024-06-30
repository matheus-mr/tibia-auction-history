package com.matheusmr.tibiaauctionhistory.auctionsearch.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.matheusmr.tibiaauctionhistory.AbstractIntegrationTests;
import com.matheusmr.tibiaauctionhistory.auctionsearch.deserializer.AuctionSearchCriterionDeserializer;
import com.matheusmr.tibiaauctionhistory.auctionsearch.model.AuctionSearch;
import com.matheusmr.tibiaauctionhistory.auctionsearch.model.AuctionSearchCriterion;
import com.matheusmr.tibiaauctionhistory.auctionsearch.model.Operator;
import com.matheusmr.tibiaauctionhistory.common.model.AuctionDTO;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.matheusmr.tibiaauctionhistory.auctionsearch.converter.SortDirectionConverter.INVALID_SORT_DIRECTION_MSG;
import static com.matheusmr.tibiaauctionhistory.auctionsearch.converter.SortableFieldConverter.INVALID_SORTABLE_FIELD_MSG;
import static com.matheusmr.tibiaauctionhistory.auctionsearch.service.AuctionSearchServiceImpl.AUCTION_SEARCH_NOT_FOUND_MSG;
import static com.matheusmr.tibiaauctionhistory.auctionsearch.service.AuctionSearchServiceImpl.INVALID_AMOUNT_OF_AUCTIONS_FETCHED_MSG;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestAuctionSearchController extends AbstractIntegrationTests {

    @Test
    public void testCreateAndFetchAuctionSearchSuccess() throws Exception {
        final AuctionSearchCriterion auctionSearchCriterion = new AuctionSearchCriterion(
                "name", Operator.EQUALS, List.of("Some name"), null
        );

        final UUID auctionSearchIdResult = createAuctionSearch(auctionSearchCriterion);

        final String getAuctionSearchResult = mockMvc.perform(
                    get("/api/v1/auctions/search/" + auctionSearchIdResult)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        final AuctionSearch auctionSearchResult = objectMapper.readValue(
                getAuctionSearchResult, AuctionSearch.class
        );

        assertEquals(auctionSearchIdResult, auctionSearchResult.getId());
        assertEquals(auctionSearchCriterion, auctionSearchResult.getCriteria());
        assertNull(auctionSearchResult.getLastUsageAt());
    }

    @Test
    public void testCreateAuctionSearchExceedingMaxRecursionDepth() throws Exception {
        final AuctionSearchCriterion nameCriterion = new AuctionSearchCriterion(
                "name", Operator.EQUALS, List.of("foo"), null
        );
        final AuctionSearchCriterion baseAndCriterion = new AuctionSearchCriterion(
                null, Operator.AND, null, List.of(nameCriterion)
        );
        final AuctionSearchCriterion criterionExceedingMaxRecursionDepth = createRecursiveCriterion(
                baseAndCriterion,
                AuctionSearchCriterionDeserializer.MAX_RECURSION_DEPTH + 1
        );

        final String response = mockMvc.perform(
                        post("/api/v1/auctions/search")
                                .content(objectMapper.writeValueAsString(criterionExceedingMaxRecursionDepth))
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(AuctionSearchCriterionDeserializer.MAX_RECURSION_DEPTH_EXCEEDED_MSG, response);
    }

    @Test
    public void testFetchAuctionSearchResultsWithNegativeAmountOfAuctions() throws Exception {
        final String response = mockMvc.perform(
                        get("/api/v1/auctions/search/" + KNIGHT_AUCTIONS_SEARCH_ID + "/results")
                                .queryParam("limit", "-1")
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(INVALID_AMOUNT_OF_AUCTIONS_FETCHED_MSG, response);
    }

    @Test
    public void testFetchAuctionSearchResultsWithZeroAmountOfAuctions() throws Exception {
        final String response = mockMvc.perform(
                        get("/api/v1/auctions/search/" + KNIGHT_AUCTIONS_SEARCH_ID + "/results")
                                .queryParam("limit", "0")
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(INVALID_AMOUNT_OF_AUCTIONS_FETCHED_MSG, response);
    }

    @Test
    public void testFetchAuctionSearchResultsWithMoreThanLimitAmountOfAuctions() throws Exception {
        final String response = mockMvc.perform(
                        get("/api/v1/auctions/search/" + KNIGHT_AUCTIONS_SEARCH_ID + "/results")
                                .queryParam("limit", "999999")
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(INVALID_AMOUNT_OF_AUCTIONS_FETCHED_MSG, response);
    }

    @Test
    public void testFetchAuctionSearchResultsWithInexistentAuctionSearch() throws Exception {
        final String response = mockMvc.perform(
                        get("/api/v1/auctions/search/" + UUID.randomUUID() + "/results")
                )
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(AUCTION_SEARCH_NOT_FOUND_MSG, response);
    }

    @Test
    public void testFetchAuctionSearchResultsWithInvalidSortableField() throws Exception {
        final String field = "id";
        final String response = mockMvc.perform(
                        get("/api/v1/auctions/search/" + KNIGHT_AUCTIONS_SEARCH_ID + "/results")
                                .queryParam("sortBy", field)
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(INVALID_SORTABLE_FIELD_MSG.formatted(field), response);
    }

    @Test
    public void testFetchAuctionSearchResultsWithInvalidSortDirection() throws Exception {
        final String orderDirection = "foo";
        final String response = mockMvc.perform(
                        get("/api/v1/auctions/search/" + KNIGHT_AUCTIONS_SEARCH_ID + "/results")
                                .queryParam("orderBy", orderDirection)
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(INVALID_SORT_DIRECTION_MSG.formatted(orderDirection), response);
    }

    @Test
    public void testFetchAuctionSearchResultsWithOffsetGreaterThanResults() throws Exception {
        final List<AuctionDTO> auctions = searchAuctions(
                KNIGHT_AUCTIONS_SEARCH_ID, "999999", null, null, null
        );

        assertTrue(auctions.isEmpty());
    }

    @Test
    public void testFetchAuctionSearchResultsWithOffset() throws Exception {
        final List<AuctionDTO> auctions = searchAuctions(
                KNIGHT_AUCTIONS_SEARCH_ID, "1", null, "name", null
        );

        assertEquals(1, auctions.size());
        assertEquals(33, auctions.getFirst().id());
    }

    @Test
    public void testFetchAuctionSearchResultsWithLimit() throws Exception {
        final List<AuctionDTO> auctions = searchAuctions(
                KNIGHT_AUCTIONS_SEARCH_ID, null, "1", "name", null
        );

        assertEquals(1, auctions.size());
        assertEquals(36, auctions.getFirst().id());
    }

    @Test
    public void testFetchAuctionSearchResultsWithAndOperator() throws Exception {
        final AuctionSearchCriterion auctionSearchCriterion = new AuctionSearchCriterion(
                null,
                Operator.AND,
                null,
                List.of(
                        new AuctionSearchCriterion(
                                "world",
                                Operator.EQUALS,
                                List.of("Damora"),
                                null
                        ),
                        new AuctionSearchCriterion(
                                "vocation",
                                Operator.EQUALS,
                                List.of("SORCERER"),
                                null
                        )
                )
        );

        final UUID auctionSearchIdResult = createAuctionSearch(auctionSearchCriterion);

        final List<AuctionDTO> auctions = searchAuctions(
                auctionSearchIdResult, null, null, null, null
        );

        assertEquals(1, auctions.size());
        assertEquals(23, auctions.getFirst().id());
    }

    @Test
    public void testFetchAuctionSearchResultsWithOrOperator() throws Exception {
        final AuctionSearchCriterion auctionSearchCriterion = new AuctionSearchCriterion(
                null,
                Operator.OR,
                null,
                List.of(
                        new AuctionSearchCriterion(
                                "world",
                                Operator.EQUALS,
                                List.of("Damora"),
                                null
                        ),
                        new AuctionSearchCriterion(
                                "vocation",
                                Operator.EQUALS,
                                List.of("KNIGHT"),
                                null
                        )
                )
        );

        final UUID auctionSearchIdResult = createAuctionSearch(auctionSearchCriterion);

        final List<AuctionDTO> auctions = searchAuctions(
                auctionSearchIdResult, null, null, "name", null
        );

        assertEquals(3, auctions.size());
        assertEquals(36, auctions.get(0).id());
        assertEquals(33, auctions.get(1).id());
        assertEquals(23, auctions.get(2).id());
    }

    @Test
    public void testFetchAuctionSearchResultsWithElementMatchOperator() throws Exception {
        final AuctionSearchCriterion auctionSearchCriterion = new AuctionSearchCriterion(
                "items",
                Operator.ELEMENT_MATCH,
                null,
                List.of(
                        new AuctionSearchCriterion(
                                "name",
                                Operator.EQUALS,
                                List.of("giant shimmering pearl"),
                                null
                        )
                )
        );

        final UUID auctionSearchIdResult = createAuctionSearch(auctionSearchCriterion);

        final List<AuctionDTO> auctions = searchAuctions(
                auctionSearchIdResult, null, null, "name", null
        );

        assertEquals(1, auctions.size());
        assertEquals(36, auctions.getFirst().id());
    }

    @Test
    public void testFetchAuctionSearchResultsWithEqualsOperator() throws Exception {
        final AuctionSearchCriterion auctionSearchCriterion = new AuctionSearchCriterion(
                "name",
                Operator.EQUALS,
                List.of("Aj Dzi"),
                null
        );

        final UUID auctionSearchIdResult = createAuctionSearch(auctionSearchCriterion);

        final List<AuctionDTO> auctions = searchAuctions(
                auctionSearchIdResult, null, null, "name", null
        );

        assertEquals(1, auctions.size());
        assertEquals(23, auctions.getFirst().id());
    }

    @Test
    public void testFetchAuctionSearchResultsWithNotEqualsOperator() throws Exception {
        final AuctionSearchCriterion auctionSearchCriterion = new AuctionSearchCriterion(
                "name",
                Operator.NOT_EQUALS,
                List.of("Aj Dzi"),
                null
        );

        final UUID auctionSearchIdResult = createAuctionSearch(auctionSearchCriterion);

        final List<AuctionDTO> auctions = searchAuctions(
                auctionSearchIdResult, null, null, "name", null
        );

        assertEquals(2, auctions.size());
        assertEquals(36, auctions.get(0).id());
        assertEquals(33, auctions.get(1).id());
    }

    @Test
    public void testFetchAuctionSearchResultsWithGreaterThanOperator() throws Exception {
        final AuctionSearchCriterion auctionSearchCriterion = new AuctionSearchCriterion(
                "level",
                Operator.GREATER_THAN,
                List.of(110),
                null
        );

        final UUID auctionSearchIdResult = createAuctionSearch(auctionSearchCriterion);

        final List<AuctionDTO> auctions = searchAuctions(
                auctionSearchIdResult, null, null, "name", null
        );

        assertEquals(2, auctions.size());
        assertEquals(36, auctions.get(0).id());
        assertEquals(33, auctions.get(1).id());
    }

    @Test
    public void testFetchAuctionSearchResultsWithGreaterOrEqualsThanOperator() throws Exception {
        final AuctionSearchCriterion auctionSearchCriterion = new AuctionSearchCriterion(
                "level",
                Operator.GREATER_OR_EQUALS_THAN,
                List.of(309),
                null
        );

        final UUID auctionSearchIdResult = createAuctionSearch(auctionSearchCriterion);

        final List<AuctionDTO> auctions = searchAuctions(
                auctionSearchIdResult, null, null, "name", null
        );

        assertEquals(2, auctions.size());
        assertEquals(36, auctions.get(0).id());
        assertEquals(33, auctions.get(1).id());
    }

    @Test
    public void testFetchAuctionSearchResultsWithLessThanOperator() throws Exception {
        final AuctionSearchCriterion auctionSearchCriterion = new AuctionSearchCriterion(
                "level",
                Operator.LESS_THAN,
                List.of(309),
                null
        );

        final UUID auctionSearchIdResult = createAuctionSearch(auctionSearchCriterion);

        final List<AuctionDTO> auctions = searchAuctions(
                auctionSearchIdResult, null, null, "name", null
        );

        assertEquals(1, auctions.size());
        assertEquals(23, auctions.getFirst().id());
    }

    @Test
    public void testFetchAuctionSearchResultsWithLessOrEqualsThanOperator() throws Exception {
        final AuctionSearchCriterion auctionSearchCriterion = new AuctionSearchCriterion(
                "level",
                Operator.LESS_OR_EQUALS_THAN,
                List.of(309),
                null
        );

        final UUID auctionSearchIdResult = createAuctionSearch(auctionSearchCriterion);

        final List<AuctionDTO> auctions = searchAuctions(
                auctionSearchIdResult, null, null, "name", null
        );

        assertEquals(2, auctions.size());
        assertEquals(36, auctions.get(0).id());
        assertEquals(23, auctions.get(1).id());
    }

    @Test
    public void testFetchAuctionSearchResultsWithInOperator() throws Exception {
        final AuctionSearchCriterion auctionSearchCriterion = new AuctionSearchCriterion(
                "world",
                Operator.IN,
                List.of("Gladera", "Damora"),
                null
        );

        final UUID auctionSearchIdResult = createAuctionSearch(auctionSearchCriterion);

        final List<AuctionDTO> auctions = searchAuctions(
                auctionSearchIdResult, null, null, "name", null
        );

        assertEquals(3, auctions.size());
        assertEquals(36, auctions.get(0).id());
        assertEquals(33, auctions.get(1).id());
        assertEquals(23, auctions.get(2).id());
    }

    @Test
    public void testFetchAuctionSearchResultsWithNotInOperator() throws Exception {
        final AuctionSearchCriterion auctionSearchCriterion = new AuctionSearchCriterion(
                "world",
                Operator.NOT_IN,
                List.of("Gladera"),
                null
        );

        final UUID auctionSearchIdResult = createAuctionSearch(auctionSearchCriterion);

        final List<AuctionDTO> auctions = searchAuctions(
                auctionSearchIdResult, null, null, "name", null
        );

        assertEquals(2, auctions.size());
        assertEquals(36, auctions.get(0).id());
        assertEquals(23, auctions.get(1).id());
    }

    @Test
    public void testFetchAuctionSearchResultsWithBetweenOperator() throws Exception {
        final AuctionSearchCriterion auctionSearchCriterion = new AuctionSearchCriterion(
                "level",
                Operator.BETWEEN,
                List.of(110, 309),
                null
        );

        final UUID auctionSearchIdResult = createAuctionSearch(auctionSearchCriterion);

        final List<AuctionDTO> auctions = searchAuctions(
                auctionSearchIdResult, null, null, "name", null
        );

        assertEquals(2, auctions.size());
        assertEquals(36, auctions.get(0).id());
        assertEquals(23, auctions.get(1).id());
    }

    private AuctionSearchCriterion createRecursiveCriterion(AuctionSearchCriterion criterion, int depth){
        if (depth == 0){
            return new AuctionSearchCriterion(
                    "name", Operator.EQUALS, List.of("foo"), null
            );
        }

        return criterion.withCriterias(List.of(createRecursiveCriterion(criterion, depth - 1)));
    }

    private UUID createAuctionSearch(AuctionSearchCriterion auctionSearchCriterion) throws Exception {
        final String response = mockMvc.perform(
                        post("/api/v1/auctions/search")
                                .content(objectMapper.writeValueAsString(auctionSearchCriterion))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(response, UUID.class);
    }

    private List<AuctionDTO> searchAuctions(
            UUID auctionSearchId,
            String offset,
            String limit,
            String sortBy,
            String orderBy
    ) throws Exception {
        final String response = mockMvc.perform(
                        get("/api/v1/auctions/search/" + auctionSearchId + "/results")
                                .queryParam("offset", offset)
                                .queryParam("limit", limit)
                                .queryParam("sortBy", sortBy)
                                .queryParam("orderBy", orderBy)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(response, new TypeReference<>() {});
    }
}
