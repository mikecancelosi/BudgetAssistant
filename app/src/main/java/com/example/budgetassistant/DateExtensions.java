package com.example.budgetassistant;

import java.util.Calendar;
import java.util.Date;

public class DateExtensions {
    public static int GetDaysBetween(Date d1, Date d2){
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.setTime(d1);
        endDate.setTime(d2);
        long startTime = startDate.getTime().getTime();
        long endTime = endDate.getTime().getTime();
        long diffTime = Math.abs(endTime - startTime);
        long diffDays = diffTime / (1000 * 60 * 60 * 24);

        return (int) diffDays;
    }
}
