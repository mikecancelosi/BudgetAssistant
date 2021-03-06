package com.example.budgetassistant.models;

import com.example.budgetassistant.Enums.TransactionCategories;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Transaction {
    public Float Amount;
    public String Description;
    public TransactionCategories Category;
    public Date DateOfTransaction;
    public UUID Id;

    public Transaction(){
        Amount = 0f;
        Description = "Not Available";
        Category = TransactionCategories.OTHER;
        DateOfTransaction = Calendar.getInstance().getTime();
        Id = UUID.randomUUID();
    }


    public Transaction(Date date) {
        Amount = 0f;
        Description = "Not Available";
        Category = TransactionCategories.OTHER;
        DateOfTransaction = date;
        Id = UUID.randomUUID();
    }

    public Transaction(TransactionCategories category) {
        Amount = 0f;
        Description = "Not Available";
        Category = category;
        DateOfTransaction = Calendar.getInstance().getTime();
        Id = UUID.randomUUID();
    }

    public Transaction(float amount,
                       String description,
                       TransactionCategories category,
                       Date date) {
        Amount = amount;
        Description = description;
        Category = category;
        DateOfTransaction = date;
        Id = UUID.randomUUID();
    }

    public Transaction(float amount, TransactionCategories category) {
        Amount = amount;
        Category = category;
        Id = UUID.randomUUID();
    }

    public Transaction(Date date, float amount) {
        DateOfTransaction = date;
        Amount = amount;
        Id = UUID.randomUUID();
    }
}
