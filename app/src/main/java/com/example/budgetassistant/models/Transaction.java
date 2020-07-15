package com.example.budgetassistant.models;

import com.example.budgetassistant.TransactionCategories;

import java.util.Date;

public class Transaction {
    public float Income;
    public float Expense;
    public String Description;
    public TransactionCategories Category;
    public String DateOfTransaction;
    public String Frequency;

    public Transaction(String date){
        Income = 0f;
        Expense = 0f;
        Description = "Not Available";
        Category = TransactionCategories.OTHER;
        DateOfTransaction = date;    }

    public Transaction(float income, float expense, String description, TransactionCategories category, String date) {
        Income = income;
        Expense = expense;
        Description = description;
        Category = category;
        DateOfTransaction = date;
    }
}
