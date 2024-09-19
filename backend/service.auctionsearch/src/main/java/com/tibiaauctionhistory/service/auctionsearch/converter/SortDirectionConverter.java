package com.tibiaauctionhistory.service.auctionsearch.converter;

import com.tibiaauctionhistory.service.auctionsearch.exception.InvalidSortDirectionException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class SortDirectionConverter implements Converter<String, Sort.Direction> {

    public static final String INVALID_SORT_DIRECTION_MSG = "Invalid sort direction: %s";

    @Override
    public Sort.Direction convert(String direction) {
        try {
            return Sort.Direction.valueOf(direction.toUpperCase());
        } catch (IllegalArgumentException e){
            throw new InvalidSortDirectionException(INVALID_SORT_DIRECTION_MSG.formatted(direction));
        }
    }
}
