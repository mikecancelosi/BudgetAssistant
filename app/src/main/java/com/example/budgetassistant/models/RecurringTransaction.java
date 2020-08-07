package com.example.budgetassistant.models;

import com.example.budgetassistant.DateExtensions;
import com.example.budgetassistant.TransactionCategories;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class RecurringTransaction implements Serializable {
    public Date StartDate;
    public Date EndDate;
    public Float Amount;
    public boolean Varies;
    public TransactionCategories Category;
    public AbstractMap.SimpleEntry<Integer,Integer> Frequency;
    public String Description;
    public UUID Id;

    public RecurringTransaction(Date startDate, Date endDate, Float amount, boolean varies, TransactionCategories category, AbstractMap.SimpleEntry<Integer,Integer> frequency, String description) {
        StartDate = startDate;
        EndDate = endDate;
        Amount = amount;
        Varies = varies;
        Category = category;
        Frequency = frequency;
        Description = description;
        Id = UUID.randomUUID();
    }


    public String getFrequencyDisplayText(){
        int key = Frequency.getKey();
        int value = Frequency.getValue();
        //TODO: Convert to switch statement
        if(key == Calendar.DATE){
            if(value ==1){
                return "Every day";
            } else if(value ==2){
                return "Every other day";
            } else{
                return "Every " + value + " Days";
            }
        }
        if(key == Calendar.WEEK_OF_YEAR){
            if(value == 1){
                return "Weekly";
            } else if(value ==2){
                return "Biweekly";
            } else{
                return "Every " + value + " Weeks";
            }
        }
        if(key == Calendar.MONTH){
            if(value == 1){
                return "Monthly";
            } else if(value ==3){
                return "Quarterly";
            } else if(value == 6){
                return "Bianually";
            } else{
                return "Every " + value + " Months";
            }
        }

        if(key == Calendar.YEAR){
            if(value == 1){
                return "Yearly";
            }else{
                return "Every " + value + " Years";
            }
        }

        return "";
    }

    public int GetDaysLeftUntilNextRecurrentCharge(){
        Calendar c = Calendar.getInstance();
        Calendar calToday = Calendar.getInstance();
        c.setTime(StartDate);
        while (c.before(calToday)) {
            c.add(Frequency.getKey(),Frequency.getValue());
        }

        long startTime = c.getTime().getTime();
        long endTime = calToday.getTime().getTime();
        long diffTime = Math.abs(endTime - startTime);
        long diffDays = diffTime / (1000 * 60 * 60 * 24);

        return (int) diffDays;
    }
}
