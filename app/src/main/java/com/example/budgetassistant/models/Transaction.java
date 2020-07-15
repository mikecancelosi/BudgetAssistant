package com.example.budgetassistant.models;

import com.example.budgetassistant.TransactionCategories;

import java.util.Date;

public class Transaction {
    public float Amount;
    public boolean Expense;
    public String Description;
    public TransactionCategories Category;
    public String DateOfTransaction;
    public String Frequency;

    public Transaction(float amount, boolean expense, String description, TransactionCategories category, String date) {
        Amount = amount;
        Expense = expense;
        Description = description;
        Category = category;
        DateOfTransaction = date;
    }
}
