package com.example.budgetassistant.models;

import com.example.budgetassistant.DateExtensions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.Calendar;
import java.util.Date;

import static java.time.temporal.ChronoUnit.DAYS;

public class Income {
    public float Amount;
    public AbstractMap.SimpleEntry<Integer, Integer> Period;
    public Date FirstPaycheck;

    public Income() {
        Amount = 0f;
        Period = new AbstractMap.SimpleEntry<>(Calendar.WEEK_OF_YEAR, 2);
        FirstPaycheck = new Date();
    }

    public Income(float amount,
                  AbstractMap.SimpleEntry<Integer, Integer> period,
                  Date firstPaycheck) {
        Amount = amount;
        Period = period;
        FirstPaycheck = firstPaycheck;
    }

    public Date GetLastPaycheckDate() {
        Calendar calToday = Calendar.getInstance();
        Calendar calChecker = Calendar.getInstance();
        calChecker.setTime(FirstPaycheck);
        while (calChecker.before(calToday)) {
            calChecker.add(Period.getKey(), Period.getValue());
        }
        calChecker.add(Period.getKey(), -1 * Period.getValue());
        return calChecker.getTime();
    }

    public Date GetNextPaycheckDate() {
        Calendar calToday = Calendar.getInstance();
        Calendar calChecker = Calendar.getInstance();
        calChecker.setTime(FirstPaycheck);
        while (calChecker.before(calToday)) {
            calChecker.add(Period.getKey(), Period.getValue());
        }
        return calChecker.getTime();
    }
}
