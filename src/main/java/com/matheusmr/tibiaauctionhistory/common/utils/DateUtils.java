package com.matheusmr.tibiaauctionhistory.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static LocalDateTime parseDateToUTC(String date, DateTimeFormatter formatter){
        return ZonedDateTime
                .parse(date, formatter)
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();
    }
}
