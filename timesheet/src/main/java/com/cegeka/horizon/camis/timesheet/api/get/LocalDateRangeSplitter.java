package com.cegeka.horizon.camis.timesheet.api.get;

import org.threeten.extra.LocalDateRange;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.next;

public class LocalDateRangeSplitter {
    public static List<LocalDateRange> splitByWeek(LocalDateRange dateRange){
        List<LocalDateRange> result = new ArrayList<>();
        LocalDate start = dateRange.getStart();
        while(! start.isAfter(dateRange.getEnd())){
            if(start.getDayOfWeek() == SUNDAY){
                result.add(LocalDateRange.of(start, start));
            }else{
                result.add(LocalDateRange.of(start, min(dateRange.getEnd(), start.with(next(SUNDAY)))));
            }
            start = start.with(next(MONDAY));
        }
        return result;
    }

    private static LocalDate min(LocalDate localDate1, LocalDate localDate2) {
        return Stream.of(localDate1, localDate2).sorted().findFirst().get();
    }
}
