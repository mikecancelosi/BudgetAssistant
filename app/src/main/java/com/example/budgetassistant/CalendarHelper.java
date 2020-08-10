package com.example.budgetassistant;

import java.util.AbstractMap;
import java.util.Calendar;
import java.util.Date;

public class CalendarHelper {

    public static int countTimePeriodsBetweenDates(Date StartDate, Date EndDate,
                                                   AbstractMap.SimpleEntry<Integer,Integer> period){

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(StartDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(EndDate);
        Calendar iterCal = Calendar.getInstance();
        iterCal.setTime(StartDate);
        int count = 0;
        while(iterCal.before(endCal) && iterCal.after(startCal)){
            count++;
            iterCal.add(period.getKey(),period.getValue());
        }
        return count--;

    }

    public static int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}
