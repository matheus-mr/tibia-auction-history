package com.tibiaauctionhistory.module.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static LocalDateTime parseZonedDateTimeToUTC(String date, DateTimeFormatter formatter){
        return ZonedDateTime
                .parse(date, formatter)
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();
    }
}
