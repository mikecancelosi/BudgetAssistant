package com.example.budgetassistant.models;

import android.media.Image;

import com.example.budgetassistant.TransactionCategories;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UserSettings {

    public String name;
    public Image profilePicture;
    public List<Transaction> recurringTransactions;
    public HashMap<TransactionCategories, Float> idealBreakdown;
    public float budget;
    public Income income;
}
