package com.example.budgetassistant.models;

import android.media.Image;

import com.example.budgetassistant.R;
import com.example.budgetassistant.TransactionCategories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserSettings {

    public String name;
    public int profilePicture;
    public List<Transaction> recurringTransactions;
    public HashMap<TransactionCategories, Float> idealBreakdown;
    public float budget;
    public Income income;
    public List<BankAccount> accounts;
    public Date joinDate;

    public UserSettings() {
        name = "Not Available";
        profilePicture = R.mipmap.ic_cancelosi; //TODO: Replace with empty user pic
        recurringTransactions = new ArrayList<>();
        idealBreakdown = new HashMap<>();
        budget = 0f;
        income = new Income();
        accounts = new ArrayList<>();


    }
}
