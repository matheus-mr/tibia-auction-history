package com.matheusmr.tibiaauctionhistory.auctionsearch.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.matheusmr.tibiaauctionhistory.auctionsearch.model.Operator.*;

@Getter
@EqualsAndHashCode
public class AuctionSearchCriterion {

    String field;

    Operator operator;

    List<Object> values;

    List<AuctionSearchCriterion> criterias;

    @JsonIgnore
    public Object getFirstValue(){
        return values.getFirst();
    }

    public AuctionSearchCriterion(String field, Operator operator, List<Object> values, List<AuctionSearchCriterion> criterias) {
        final boolean isLogicOperator = operator == AND || operator == OR;
        final boolean isElementMatchOperator = operator == ELEMENT_MATCH;
        final boolean operatorShouldHaveCriterias = isLogicOperator || isElementMatchOperator;
        final boolean operatorShouldHaveValues = !operatorShouldHaveCriterias;
        final boolean hasEmptyValues = CollectionUtils.isEmpty(values);
        final boolean hasEmptyCriterias = CollectionUtils.isEmpty(criterias);

        Preconditions.checkArgument(
                !operatorShouldHaveCriterias || hasEmptyValues,
                "Logical/Element Match operators must not have values but instead criterias."
        );
        Preconditions.checkArgument(
                !operatorShouldHaveCriterias || !hasEmptyCriterias,
                "Logical/Element Match operators must have criterias."
        );
        Preconditions.checkArgument(
                !operatorShouldHaveValues || !hasEmptyValues,
                "Simple operators must have values."
        );
        Preconditions.checkArgument(
                !operatorShouldHaveValues || hasEmptyCriterias,
                "Simple operators must not have criterias but instead values."
        );

        this.field = field;
        this.operator = operator;
        this.values = values;
        this.criterias = criterias;
    }
}
