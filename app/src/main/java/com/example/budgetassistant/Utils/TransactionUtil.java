package com.example.budgetassistant.Utils;

import android.util.Log;

import com.example.budgetassistant.models.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionUtil {
    public static List<Transaction> getTransactionsInTimeFrame(List<Transaction> sourceTransactions,
                                                               Date start,
                                                               Date end)
    {
        List<Transaction> transactions = new ArrayList<>();
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(start);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);
        Calendar transCal = Calendar.getInstance();
        for(Transaction t : sourceTransactions){
            transCal.setTime(t.DateOfTransaction);
            if(CalendarUtil.isSameDay(startCal.getTime(),t.DateOfTransaction) || (transCal.after(startCal) && transCal.before(endCal))){
                transactions.add(t);
            }
        }
        return transactions;
    }

    public static Float getExpenseTotal(List<Transaction> transactions){
        float expenseTotal = 0f;
        for(Transaction t : transactions){
            if(t.Amount < 0) {
                expenseTotal += t.Amount;
            }
        }
        return Math.abs(expenseTotal);
    }

    public static Float getIncomeTotal(List<Transaction> transactions){
        float incomeTotal = 0f;
        for(Transaction t : transactions){
            if(t.Amount > 0) {
                incomeTotal += t.Amount;
            }
        }
        return incomeTotal;
    }

}
