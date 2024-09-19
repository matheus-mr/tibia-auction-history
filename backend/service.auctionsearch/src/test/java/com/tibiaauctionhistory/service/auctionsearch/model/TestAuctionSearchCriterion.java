package com.tibiaauctionhistory.service.auctionsearch.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestAuctionSearchCriterion {

    @Test
    public void testOperatorShouldBeNonNull(){
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AuctionSearchCriterion("name", null, null, null)
        );

        assertEquals("Operator must not be null.", exception.getMessage());
    }

    @Test
    public void testNonLogicalOperatorsFieldShouldBeNonNull(){
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AuctionSearchCriterion(null, Operator.EQUALS, null, null)
        );

        assertEquals("Non logical operators must have a field.", exception.getMessage());
    }

    @Test
    public void testNonLogicalOperatorsFieldShouldBeNonEmpty(){
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AuctionSearchCriterion("   ", Operator.EQUALS, null, null)
        );

        assertEquals("Non logical operators must have a field.", exception.getMessage());
    }

    @Test
    public void testLogicalOperatorsShouldNotHaveField(){
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AuctionSearchCriterion("name", Operator.AND, null, null)
        );

        assertEquals("Logical operators must not have a field.", exception.getMessage());
    }

    @Test
    public void testLogicalOperatorShouldNotHaveValues(){
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AuctionSearchCriterion(null, Operator.AND, List.of("foo"), null)
        );

        assertEquals("Logical/Element Match operators must not have values but instead criterias.", exception.getMessage());
    }

    @Test
    public void testLogicalOperatorCriterasShouldBeNonNull(){
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AuctionSearchCriterion(null, Operator.OR, null, null)
        );

        assertEquals("Logical/Element Match operators must have criterias.", exception.getMessage());
    }

    @Test
    public void testLogicalOperatorCriterasShouldBeNonEmpty(){
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AuctionSearchCriterion(null, Operator.OR, null, List.of())
        );

        assertEquals("Logical/Element Match operators must have criterias.", exception.getMessage());
    }

    @Test
    public void testNonLogicalOperatorValuesShouldBeNonNull(){
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AuctionSearchCriterion("name", Operator.EQUALS, null, null)
        );

        assertEquals("Simple operators must have values.", exception.getMessage());
    }

    @Test
    public void testNonLogicalOperatorValuesShouldBeNonEmpty(){
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AuctionSearchCriterion("name", Operator.EQUALS, List.of(), null)
        );

        assertEquals("Simple operators must have values.", exception.getMessage());
    }

    @Test
    public void testNonLogicalOperatorShouldNotHaveNonNullCriteria(){
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AuctionSearchCriterion("name", Operator.EQUALS, List.of("foo"), List.of())
        );

        assertEquals("Simple operators must not have criterias but instead values.", exception.getMessage());
    }

    @Test
    public void testOperatorShouldHaveExactlyAmountOfValues(){
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AuctionSearchCriterion("auctionEnd", Operator.BETWEEN, List.of("2024-01-01"), null)
        );

        assertEquals(
                "%s operator must have exactly %s values."
                        .formatted(Operator.BETWEEN.getKey(), Operator.BETWEEN.getExpectedValues()),
                exception.getMessage()
        );
    }

    @Test
    public void testOperatorShouldNotHaveMoreThanMaximumAmountOfValues(){
        final List<Object> values = IntStream.range(0, Operator.IN.getMaxValues() + 2)
                .boxed()
                .collect(Collectors.toList());

        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AuctionSearchCriterion("level", Operator.IN, values, null)
        );

        assertEquals(
                "%s operator must have at most %s values."
                        .formatted(Operator.IN.getKey(), Operator.IN.getMaxValues()),
                exception.getMessage()
        );
    }

    @Test
    public void testOperatorShouldNotHaveMoreThanMaximumAmountOfCriterias(){
        final AuctionSearchCriterion nameCriterion = new AuctionSearchCriterion("name", Operator.EQUALS, List.of("foo"), null);

        final List<AuctionSearchCriterion> criterias = IntStream.range(0, Operator.AND.getMaxCriterias() + 2)
                .mapToObj(i -> nameCriterion)
                .collect(Collectors.toList());

        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AuctionSearchCriterion(null, Operator.AND, null, criterias)
        );

        assertEquals(
                "%s operator must have at most %s criterias."
                        .formatted(Operator.AND.getKey(), Operator.AND.getMaxCriterias()),
                exception.getMessage()
        );
    }
}
