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

    public String GetTimePeriodLabel(){
        int daysInTimePeriod = GetTimePeriodInDays();
        if(daysInTimePeriod == 7){
            return "Weekly";
        } else if(daysInTimePeriod > 25 && daysInTimePeriod < 33){
            return "Monthly";
        } else if(daysInTimePeriod > 80 && daysInTimePeriod < 105){
            return "Quarterly";
        } else if(daysInTimePeriod > 360){
            return "Annually";
        } else{
            return "Custom";
        }
    }

    public HashMap<TransactionCategories,Float> GetCategorizedExpenseValues(){
        float expenseTotal = 0f;
        HashMap<TransactionCategories,Float> categorizedData = new HashMap<TransactionCategories,Float>();
        for(Transaction t : Transactions){
            if(t.Expense > 0f) {
                expenseTotal += t.Expense;
                TransactionCategories category = t.Category;
                if (categorizedData.containsKey(category)) {
                    Float value = categorizedData.get(category);
                    value += t.Expense;
                } else {
                    categorizedData.put(category, t.Expense);
                }
            }
        }

        return categorizedData;
    }


}
