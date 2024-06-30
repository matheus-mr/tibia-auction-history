package com.matheusmr.tibiaauctionhistory.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListUtils {

    public static <T extends Comparable<? super T>> boolean equalsIgnoringOrder(List<T> list1, List<T> list2) {
        if (list1 == null || list2 == null) {
            return list1 == list2;
        }

        if (list1.size() != list2.size()) {
            return false;
        }

        List<T> sortedList1 = new ArrayList<>(list1);
        List<T> sortedList2 = new ArrayList<>(list2);

        Collections.sort(sortedList1);
        Collections.sort(sortedList2);

        return sortedList1.equals(sortedList2);
    }
}
