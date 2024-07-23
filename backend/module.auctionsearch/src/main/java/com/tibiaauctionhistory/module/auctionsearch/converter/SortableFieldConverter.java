package com.tibiaauctionhistory.module.auctionsearch.converter;

import com.tibiaauctionhistory.module.auctionsearch.exception.InvalidSortableFieldException;
import com.tibiaauctionhistory.module.common.model.SortableField;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SortableFieldConverter implements Converter<String, SortableField> {

    public static final String INVALID_SORTABLE_FIELD_MSG = "Invalid sortable field: %s";

    @Override
    public SortableField convert(String sortableField) {
        try {
            return SortableField.valueOf(sortableField);
        } catch (IllegalArgumentException e){
            return SortableField
                    .getByFieldName(sortableField)
                    .orElseThrow(() -> new InvalidSortableFieldException(INVALID_SORTABLE_FIELD_MSG.formatted(sortableField)));
        }
    }
}
