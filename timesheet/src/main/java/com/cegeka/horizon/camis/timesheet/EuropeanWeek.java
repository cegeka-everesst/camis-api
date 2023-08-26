package com.cegeka.horizon.camis.timesheet;

import java.time.LocalDate;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.next;
import static java.time.temporal.TemporalAdjusters.previous;

public class EuropeanWeek {
    static LocalDate startOfWeek(LocalDate localDate) {
        if(localDate.getDayOfWeek() == MONDAY){
            return localDate;
        }else{
            return localDate.with(previous(MONDAY));
        }
    }

    static LocalDate endOfWeek(LocalDate localDate) {
        if(localDate.getDayOfWeek() == SUNDAY){
            return localDate;
        }else{
            return localDate.with(next(SUNDAY));
        }
    }
}
