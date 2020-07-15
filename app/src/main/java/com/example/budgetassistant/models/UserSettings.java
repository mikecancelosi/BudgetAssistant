package com.example.budgetassistant.models;

import android.media.Image;

import com.example.budgetassistant.models.Transaction;

import java.util.HashMap;
import java.util.List;

public class UserSettings {

    private String name;
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    private Image profilePicture;
    public Image getProfilePicture(){
        return profilePicture;
    }
    public void setProfilePicture(Image newPicture){
        profilePicture = newPicture;
    }

    private List<Transaction> recurringTransactions;
    public List<Transaction> getRecurringTransactions(){
        return  recurringTransactions;
    }
    public void setRecurringTransactions(List<Transaction> transactions){
        recurringTransactions = transactions;
    }
    public void addRecurringTransaction(Transaction transaction){
        recurringTransactions.add(transaction);
    }
    public void removeRecurringTransaction(int i){
        recurringTransactions.remove(i);
    }
    public void editRecurringTransaction(int i, Transaction transaction){
        recurringTransactions.set(i, transaction);
    }
    public Transaction getRecurringTransaction(int i){
        return recurringTransactions.get(i);
    }

    private HashMap<String, Float> idealBreakdown;
    public HashMap<String,Float> getIdealBreakdown(){
        return idealBreakdown;
    }
    public Boolean setIdealBreakdown(HashMap<String,Float> breakdown){
        idealBreakdown = breakdown;
        return true;
    }

    private List<Transaction> transactionHistory;
    public List<Transaction> getTransactionHistory(){
        return transactionHistory;
    }
    public void setTransactionHistory(List<Transaction> transactions){
        transactionHistory = transactions;
    }
    public void addHistoricalTransaction(Transaction transaction){
        transactionHistory.add(transaction);
    }
    public void removeHistoricalTransaction(int i){
        transactionHistory.remove(i);
    }
    public void editHistoricalTransaction(int i, Transaction transaction){
        transactionHistory.set(i, transaction);
    }
    public Transaction getHistoricalTransaction(int i){
        return transactionHistory.get(i);
    }
}
