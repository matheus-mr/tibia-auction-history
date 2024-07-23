package com.tibiaauctionhistory.module.auctionsearch.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.tibiaauctionhistory.module.auctionsearch.deserializer.AuctionSearchCriterionDeserializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.With;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.tibiaauctionhistory.module.auctionsearch.model.Operator.*;

@Getter
@EqualsAndHashCode
@JsonDeserialize(using = AuctionSearchCriterionDeserializer.class)
public class AuctionSearchCriterion {

    String field;

    Operator operator;

    List<Object> values;

    @With List<AuctionSearchCriterion> criterias;

    @JsonIgnore
    public Object getFirstValue(){
        return values.getFirst();
    }

    public AuctionSearchCriterion(String field, Operator operator, List<Object> values, List<AuctionSearchCriterion> criterias) {
        final boolean isLogicOperator = operator == AND || operator == OR;
        final boolean isElementMatchOperator = operator == ELEMENT_MATCH;
        final boolean operatorShouldHaveCriterias = isLogicOperator || isElementMatchOperator;
        final boolean operatorShouldHaveValues = !operatorShouldHaveCriterias;
        final boolean operatorShouldHaveField = !isLogicOperator;
        final boolean hasEmptyValues = CollectionUtils.isEmpty(values);
        final boolean hasEmptyCriterias = CollectionUtils.isEmpty(criterias);

        Preconditions.checkArgument(
                operator != null,
                "Operator must not be null."
        );

        if (operatorShouldHaveField){
            Preconditions.checkArgument(
                    StringUtils.isNotBlank(field),
                    "Non logical operators must have a field."
            );
        } else {
            Preconditions.checkArgument(
                    field == null,
                    "Logical operators must not have a field."
            );
        }

        if (operatorShouldHaveCriterias){
            Preconditions.checkArgument(
                    values == null,
                    "Logical/Element Match operators must not have values but instead criterias."
            );
            Preconditions.checkArgument(
                    !hasEmptyCriterias,
                    "Logical/Element Match operators must have criterias."
            );
        }

        if (operatorShouldHaveValues){
            Preconditions.checkArgument(
                    criterias == null,
                    "Simple operators must not have criterias but instead values."
            );
            Preconditions.checkArgument(
                    !hasEmptyValues,
                    "Simple operators must have values."
            );
        }

        if (operator.hasExpectedValuesAmount()){
            Preconditions.checkArgument(
                    values.size() == operator.getExpectedValues(),
                    "%s operator must have exactly %s values.",
                    operator.getKey(),
                    operator.getExpectedValues()
            );
        }

        if (operator.hasMaxValuesAmount()){
            Preconditions.checkArgument(
                    values.size() <= operator.getMaxValues(),
                    "%s operator must have at most %s values.",
                    operator.getKey(),
                    operator.getMaxValues()
            );
        }

        if (operator.hasMaxCriteriasAmount()){
            Preconditions.checkArgument(
                    criterias.size() <= operator.getMaxCriterias(),
                    "%s operator must have at most %s criterias.",
                    operator.getKey(),
                    operator.getMaxCriterias()
            );
        }

        this.field = field;
        this.operator = operator;
        this.values = values;
        this.criterias = criterias;
    }
}
