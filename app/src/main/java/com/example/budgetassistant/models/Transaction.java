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
}
