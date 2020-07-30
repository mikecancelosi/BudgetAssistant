package com.example.budgetassistant.models;

import android.util.Log;

import com.example.budgetassistant.DateExtensions;
import com.example.budgetassistant.TransactionCategories;

import java.util.Calendar;
import java.util.Date;

public class Transaction {
    public float Income;
    public float Expense;
    public String Description;
    public TransactionCategories Category;
    public Date DateOfTransaction;
    public String Frequency;

    public Transaction(Date date){
        Income = 0f;
        Expense = 0f;
        Description = "Not Available";
        Category = TransactionCategories.OTHER;
        DateOfTransaction = date;
    }

    public Transaction(TransactionCategories category){
        Income = 0f;
        Expense = 0f;
        Description = "Not Available";
        Category = category;
        DateOfTransaction = Calendar.getInstance().getTime();
    }

    public Transaction(float income, float expense, String description, TransactionCategories category, Date date) {
        Income = income;
        Expense = expense;
        Description = description;
        Category = category;
        DateOfTransaction = date;
    }

    public Transaction(float income, float expense, String description, TransactionCategories category, Date date, String frequency) {
        Income = income;
        Expense = expense;
        Description = description;
        Category = category;
        DateOfTransaction = date;
        Frequency = frequency;
    }

    public Transaction(float income, float expense, TransactionCategories category) {
        Income = income;
        Expense = expense;
        Category = category;
    }

    public Transaction(Date date, float income, float expense){
        DateOfTransaction = date;
        Income = income;
        Expense = expense;
    }

    public int GetDaysLeftUntilNextRecurrentCharge(){
        if(Frequency != "OneTime") {
            Calendar c = Calendar.getInstance();
            Calendar calToday = Calendar.getInstance();
            c.setTime(DateOfTransaction);
            int Days = DateExtensions.GetDaysBetween(c.getTime(), calToday.getTime());
            switch (Frequency) {
                case "Monthly":
                    while (c.before(calToday)) {
                        c.add(Calendar.MONTH, 1);
                    }
                    break;
                case "Quarterly":
                    while (c.before(calToday)) {
                        c.add(Calendar.MONTH, 3);
                    }
                    break;
                case "Yearly":
                    while (c.before(calToday)) {
                        c.add(Calendar.YEAR, 1);
                    }
                    break;
                default:
                    break;
            }

            long startTime = c.getTime().getTime();
            long endTime = calToday.getTime().getTime();
            long diffTime = Math.abs(endTime - startTime);
            long diffDays = diffTime / (1000 * 60 * 60 * 24);

            return (int) diffDays;
        }else{
            return -1;
        }

    }
}
