package com.example.budgetassistant.models;

import android.media.Image;

import com.example.budgetassistant.TransactionCategories;
import com.example.budgetassistant.models.Transaction;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

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

    private HashMap<TransactionCategories, Float> idealBreakdown;
    public HashMap<TransactionCategories,Float> getIdealBreakdown(){
        return idealBreakdown;
    }
    public void setIdealBreakdown(HashMap<TransactionCategories,Float> breakdown){
        idealBreakdown = breakdown;
    }

    private float budget;
    public float getBudget() {
        return budget;
    }

    private Income income;
    public Income GetIncome(){
        return income;
    }
    public void SetIncome(Income newIncome){
        income = newIncome;
    }
    private Date nextPaycheckDate;
    public Date GetNextPaycheckDate(){
        return nextPaycheckDate;
    }
    public int GetNumberOfDaysToNextPaycheck(){
      return (int) DAYS.between(LocalDate.now(),LocalDate.parse(GetNextPaycheckDate().toString()));
    }
}
