package com.example.budgetassistant.models;

import com.example.budgetassistant.DateExtensions;
import com.example.budgetassistant.TransactionCategories;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TransactionSummary {
    public List<Transaction> Transactions;
    public float Budget;
    public Date StartDate;
    public Date EndDate;

    public int GetTimePeriodInDays(){
        return DateExtensions.GetDaysBetween(StartDate,EndDate);
    }

    public int GetDaysLeftInTimePeriod(){
        Calendar c = Calendar.getInstance();
        return DateExtensions.GetDaysBetween(c.getTime(),EndDate);
    }

    public float GetExpenseTotal(){
        float expense = 0f;

        for(Transaction t : Transactions){
            expense += t.Expense;
        }

        return expense;
    }
    public float GetIncomeTotal(){
        float income = 0f;
        for(Transaction t : Transactions){
            income += t.Income;
        }
        return income;
    }
}
