package com.matheusmr.tibiaauctionhistory.common.utils;

public class NumberUtils {

    public static Integer parseIntegerCommaSeparatedThousands(String number){
        return Integer.parseInt(number.replaceAll(",", ""));
    }
}
