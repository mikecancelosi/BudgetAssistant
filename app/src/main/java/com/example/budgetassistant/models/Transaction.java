package com.example.budgetassistant.models;

import android.util.Log;

import com.example.budgetassistant.DateExtensions;
import com.example.budgetassistant.TransactionCategories;

import java.util.Calendar;
import java.util.Date;

public class Transaction {
   public Float Amount;
    public String Description;
    public TransactionCategories Category;
    public Date DateOfTransaction;


    public Transaction(Date date){
        Amount = 0f;
        Description = "Not Available";
        Category = TransactionCategories.OTHER;
        DateOfTransaction = date;
    }

    public Transaction(TransactionCategories category){
        Amount = 0f;
        Description = "Not Available";
        Category = category;
        DateOfTransaction = Calendar.getInstance().getTime();
    }

    public Transaction(float amount, String description, TransactionCategories category, Date date) {
        Amount = amount;
        Description = description;
        Category = category;
        DateOfTransaction = date;
    }

    public Transaction(float amount, TransactionCategories category) {
        Amount = amount;
        Category = category;
    }

    public Transaction(Date date, float amount){
        DateOfTransaction = date;
        Amount = amount;
    }
}
