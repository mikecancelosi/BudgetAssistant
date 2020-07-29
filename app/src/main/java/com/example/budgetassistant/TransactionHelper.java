package com.example.budgetassistant;

import com.example.budgetassistant.models.Income;
import com.example.budgetassistant.models.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionHelper {
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
            if(startCal.getTime() == t.DateOfTransaction || (transCal.after(startCal) && transCal.before(endCal))){
                transactions.add(t);
            }
        }

        return transactions;
    }

    public static Float getExpenseTotal(List<Transaction> transactions){
        Float expenseTotal = 0f;
        for(Transaction t : transactions){
            expenseTotal += t.Expense;
        }
        return expenseTotal;
    }

}
