package com.example.budgetassistant.models;

import com.example.budgetassistant.DateExtensions;
import com.example.budgetassistant.TransactionCategories;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TransactionSummary {
    public List<Transaction> Transactions;
    public float Budget;
    public Date StartDate;
    public Date EndDate;
}
