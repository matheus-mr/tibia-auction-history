package com.tibiaauctionhistory.module.auctionsearch.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.tibiaauctionhistory.module.auctionsearch.model.AuctionSearchCriterion;
import com.tibiaauctionhistory.module.auctionsearch.model.Operator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class AuctionSearchCriterionDeserializer extends JsonDeserializer<AuctionSearchCriterion> {

    public static final int MAX_RECURSION_DEPTH = 10;
    public static final String MAX_RECURSION_DEPTH_EXCEEDED_MSG = "Maximum recursion depth exceeded.";

    @Override
    public AuctionSearchCriterion deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return deserialize(jp.getCodec().readTree(jp), 0);
    }

    private AuctionSearchCriterion deserialize(JsonNode node, int depth) throws IOException {
        if (depth > MAX_RECURSION_DEPTH) {
            throw new IllegalArgumentException(MAX_RECURSION_DEPTH_EXCEEDED_MSG);
        }

        ObjectMapper mapper = new ObjectMapper();

        final String field = Optional
                .ofNullable(node.get("field"))
                .filter(Predicate.not(JsonNode::isNull))
                .map(JsonNode::asText)
                .orElse(null);
        final Operator operator = Optional
                .ofNullable(node.get("operator"))
                .filter(Predicate.not(JsonNode::isNull))
                .flatMap(n -> Operator.getByKey(n.asText()))
                .orElse(null);

        final JsonNode valuesNode = node.get("values");
        List<Object> values = null;
        if (valuesNode != null && !valuesNode.isNull()){
            values = new ArrayList<>();
            ArrayNode valuesArrayNode = (ArrayNode) valuesNode;
            for (JsonNode valueNode : valuesArrayNode) {
                values.add(mapper.treeToValue(valueNode, Object.class));
            }
        }

        final JsonNode criteriasNode = node.get("criterias");
        List<AuctionSearchCriterion> criterias = null;
        if (criteriasNode != null && !criteriasNode.isNull()){
            criterias = new ArrayList<>();
            ArrayNode criteriasArrayNode = (ArrayNode) criteriasNode;
            for (JsonNode criteriaNode : criteriasArrayNode) {
                criterias.add(deserialize(criteriaNode, depth + 1));
            }
        }

        return new AuctionSearchCriterion(field, operator, values, criterias);
    }
}
