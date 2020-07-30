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

    public Income(){
        Amount = 0f;
        PayPeriodInDays = -1;
        LastPaycheck = new Date();
    }

    public Income(float amount, int payPeriodInDays, Date lastPaycheck) {
        Amount = amount;
        PayPeriodInDays = payPeriodInDays;
        LastPaycheck = lastPaycheck;
    }

    public Date GetNextPaycheckDate(){
        Calendar c = Calendar.getInstance();
        c.setTime(LastPaycheck);
        c.add(Calendar.DATE, PayPeriodInDays);
        return c.getTime();
    }
}
