package com.example.budgetassistant.models;

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
    private SimpleDateFormat mSdf;

    public Income(float amount, int payPeriodInDays, Date lastPaycheck) {
        Amount = amount;
        PayPeriodInDays = payPeriodInDays;
        LastPaycheck = lastPaycheck;
        mSdf = new SimpleDateFormat(getDateFormat());
    }

    private String getDateFormat(){
        return "yyyy--MM-dd";
    }

    public Date GetNextPaycheckDate(){
        Calendar c = Calendar.getInstance();


        try{
            //Setting the date to the given date
            c.setTime(LastPaycheck);
        }catch(Exception e){
            e.printStackTrace();
        }
        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH, PayPeriodInDays);
        Date date = c.getTime();
        c.setTime(new Date());
        return date;
    }
    public int GetNumberOfDaysToNextPaycheck(){
        Date nextPaycheckDate = GetNextPaycheckDate();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(getDateFormat());
        String formattedNextPaycheckDate = mSdf.format(nextPaycheckDate);
        return (int) DAYS.between(LocalDate.now(),LocalDate.parse(formattedNextPaycheckDate,dtf));
    }
}
