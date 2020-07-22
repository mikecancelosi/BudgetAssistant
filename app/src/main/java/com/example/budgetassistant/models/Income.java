package com.example.budgetassistant.models;

import com.example.budgetassistant.DateExtensions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import static java.time.temporal.ChronoUnit.DAYS;

public class Income {
    public float Amount;
    public int PayPeriodInDays;
    public Date LastPaycheck;

    public Income(float amount, int payPeriodInDays, Date lastPaycheck) {
        Amount = amount;
        PayPeriodInDays = payPeriodInDays;
        LastPaycheck = lastPaycheck;
    }

    public Date GetNextPaycheckDate(){
        Calendar c = Calendar.getInstance();
        c.setTime(LastPaycheck);
        c.add(Calendar.DAY_OF_MONTH, PayPeriodInDays);
        return c.getTime();
    }

    public int GetNumberOfDaysToNextPaycheck(){
        Date nextPaycheckDate = GetNextPaycheckDate();
        Calendar c = Calendar.getInstance();
        return DateExtensions.GetDaysBetween(c.getTime(),nextPaycheckDate);
    }
}
