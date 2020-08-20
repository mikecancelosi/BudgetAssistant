package com.example.budgetassistant.Helpers;

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

    public static String calendarValueDisplay(Integer input){
        switch(input){
            case Calendar.DATE:
                return "Days";
            case Calendar.YEAR:
                return "Years";
            case Calendar.MONTH:
                return "Months";
            case Calendar.WEEK_OF_YEAR:
                return "Weeks";
            default:
                return "";
        }
    }

    public static int calendarValueFromDisplay(String input){
        switch(input){
            case "Days":
            case "Day":
                return Calendar.DATE;
            case "Years":
            case "Year":
                return Calendar.YEAR;
            case "Months":
            case "Month":
                return Calendar.MONTH;
            case "Weeks":
            case "Week":
                return Calendar.WEEK_OF_YEAR;
            default:
                return -1;
        }
    }
}
