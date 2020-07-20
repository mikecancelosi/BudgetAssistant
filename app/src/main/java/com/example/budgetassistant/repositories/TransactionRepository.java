package com.example.budgetassistant.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.budgetassistant.TransactionCategories;
import com.example.budgetassistant.models.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        dataSet.add(new Transaction(0f,500.35f,"Steam Summer Sale", TransactionCategories.OTHER,  new Date((long) 1548738000000f)));
        dataSet.add(new Transaction(0f,13.25f,"Venmo", TransactionCategories.FOOD,  new Date((long) 1548738000000f)));
        dataSet.add(new Transaction(0f,800.31f,"Matt Peterson", TransactionCategories.BILL,  new Date((long) 1548738000000f)));
        dataSet.add(new Transaction(0f,60.34f,"Shoprite", TransactionCategories.FOOD,  new Date((long) 1561780800000f)));
        dataSet.add(new Transaction(10.30f,0f,"Venmo", TransactionCategories.OTHER,  new Date((long)1556510400000f)));
        dataSet.add(new Transaction(0f,12.55f,"Shoprite", TransactionCategories.FOOD,  new Date((long) 1548738000000f)));

    }
}
