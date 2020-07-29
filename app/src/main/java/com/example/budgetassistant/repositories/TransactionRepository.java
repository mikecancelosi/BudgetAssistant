package com.example.budgetassistant.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.budgetassistant.TransactionCategories;
import com.example.budgetassistant.models.Transaction;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TransactionRepository {

    private static TransactionRepository instance;
    public static TransactionRepository getInstance(){
        if(instance == null){
            instance = new TransactionRepository();
        }
        return instance;
    }

    private ArrayList<Transaction> dataSet = new ArrayList<>();
    public MutableLiveData<List<Transaction>> getTransactions(){
        setTransactions();
        MutableLiveData<List<Transaction>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }
    private void setTransactions(){
        dataSet.clear();

        Calendar transCal = Calendar.getInstance();
        for(int i = 0; i < 14;i++){
            //create 14 months of data; i is months before current
            transCal.add(Calendar.MONTH, -1);

            YearMonth yearMonthObject = YearMonth.of(transCal.get(Calendar.YEAR),transCal.get(Calendar.MONTH));
            int daysInMonth = yearMonthObject.lengthOfMonth();
            for(int j = daysInMonth; j > 0; j--){
                //Create data for each day in the month
                transCal.set(Calendar.DATE,j);
                Transaction newTrans = new Transaction(transCal.getTime());
                int randInt = ThreadLocalRandom.current().nextInt(0,TransactionCategories.values().length);
                TransactionCategories category = TransactionCategories.values()[randInt];
                if(category != TransactionCategories.INCOME){
                    //We only want to add expenses here.
                    newTrans.Expense += (ThreadLocalRandom.current().nextFloat() * 125f);
                    newTrans.Category = category;
                    newTrans.Description = "testicles";
                    dataSet.add(newTrans);
                }
            }

        }

    }
}
