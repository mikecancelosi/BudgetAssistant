package com.example.budgetassistant.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.budgetassistant.TransactionCategories;
import com.example.budgetassistant.models.Income;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.UserSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserSettingsRepository {
    private static UserSettingsRepository instance;
    public static UserSettingsRepository getInstance(){
        if(instance == null){
            instance = new UserSettingsRepository();
        }
        return instance;
    }

    private UserSettings dataSet = new UserSettings();

    public MutableLiveData<UserSettings> getSettings(){
        setSettings();
        MutableLiveData<UserSettings> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }
    private void setSettings(){
        dataSet = new UserSettings();
        dataSet.setName("Mike Cancelosi");
        Income income = new Income(1000f,14,new Date((long)1594915200000f)); // This number represents 07/16/2020 ( ms since 01/01/1970 )
        dataSet.SetIncome(income);
        dataSet.setRecurringTransactions(createRecurringTransactionPayments());
        dataSet.setIdealBreakdown(createIdealBreakdown());
    }
    private static List<Transaction> createRecurringTransactionPayments(){
        List<Transaction> transactions = new ArrayList<Transaction>();
        Calendar calInstance = Calendar.getInstance();
        transactions.add(new Transaction(0f,200f,"Car Payment", TransactionCategories.TRANSPORTATION, new Date((long) 1561953600000f),"Monthly")); // 2019/07/01
        transactions.add(new Transaction(0f,50f,"Spotify", TransactionCategories.SUBSCRIPTION,new Date((long) 1548738000000f),"Monthly")); // 2019/01/29
        transactions.add(new Transaction(0f,10f,"DollarShaveClub", TransactionCategories.SUBSCRIPTION,new Date((long) 1548738000000f),"Quarterly"));
        transactions.add(new Transaction(0f,50f,"Dad's Gift", TransactionCategories.GIFT,new Date((long) 1548738000000f),"Yearly"));
        return transactions;
    }
    private static HashMap<TransactionCategories,Float> createIdealBreakdown(){
        HashMap<TransactionCategories,Float> breakdown = new HashMap<>();
        breakdown.put(TransactionCategories.BILL,.35f);
        breakdown.put(TransactionCategories.TRANSPORTATION,.15f);
        breakdown.put(TransactionCategories.FOOD,.15f);
        breakdown.put(TransactionCategories.INVESTMENT,.10f);
        breakdown.put(TransactionCategories.BEAUTY,.05f);
        breakdown.put(TransactionCategories.PARTYING,.05f);
        breakdown.put(TransactionCategories.SUBSCRIPTION,.05f);
        breakdown.put(TransactionCategories.OTHER,.10f);
        return breakdown;
    }


}
