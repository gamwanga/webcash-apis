package com.micropay.webcash.utils;
import com.micropay.webcash.model.Picklist;

import java.util.ArrayList;
import java.util.List;

public class StaticPickList {
    public static List<Picklist> findCyclePeriods() {
        List<Picklist> results = new ArrayList<>();
        results.add(Picklist.builder().code("D").
                description("Day(s)").build());
        results.add(Picklist.builder().code("W").
                description("Week(s)").build());
        results.add(Picklist.builder().code("M").
                description("Month(s)").build());
        results.add(Picklist.builder().code("Q").
                description("Quarter(s)").build());
        results.add(Picklist.builder().code("HY").
                description("Half Year(s)").build());
        results.add(Picklist.builder().code("Y").
                description("Year(s)").build());
        return results;
    }

}
