package com.example.budgetassistant.repositories;

import android.content.res.Resources;
import android.graphics.Picture;
import android.media.Image;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.MutableLiveData;

import com.example.budgetassistant.Bank;
import com.example.budgetassistant.R;
import com.example.budgetassistant.TransactionCategories;
import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.Income;
import com.example.budgetassistant.models.RecurringTransaction;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.UserSettings;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserSettingsRepository {
    private static UserSettingsRepository instance;

    public static UserSettingsRepository getInstance() {
        if (instance == null) {
            instance = new UserSettingsRepository();
        }
        return instance;
    }

    private UserSettings dataSet;

    public MutableLiveData<UserSettings> getSettings() {
        if (dataSet == null) {
            initSettings();
        }
        MutableLiveData<UserSettings> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    public void postRecurringTransaction(RecurringTransaction transaction) {
        boolean found = false;
        for (RecurringTransaction trans : dataSet.recurringTransactions) {
            if (trans.Id == transaction.Id) {
                trans = transaction;
                found = true;
                break;
            }
        }
        if (!found) {
            dataSet.recurringTransactions.add(transaction);
        }
    }


    private void initSettings() {
        dataSet = new UserSettings();
        dataSet.name = "Mike Cancelosi";
        dataSet.profilePicture = R.mipmap.ic_cancelosi;
        dataSet.income = new Income(1000f,
                                    new AbstractMap.SimpleEntry<Integer, Integer>(Calendar.DATE, 14),
                                    new Date((long) 1561953600000f));
        dataSet.recurringTransactions = createRecurringTransactionPayments();
        dataSet.idealBreakdown = createIdealBreakdown();
        dataSet.accounts.add(new BankAccount("15202593282",
                                             "Main",
                                             Bank.CHASE,
                                             "Main Account"));
        dataSet.accounts.add(new BankAccount("84641112479",
                                             "Savings",
                                             Bank.USBANK,
                                             "Savings Account"));
        dataSet.joinDate = Calendar.getInstance().getTime();
    }

    private static List<RecurringTransaction> createRecurringTransactionPayments() {
        List<RecurringTransaction> transactions = new ArrayList<RecurringTransaction>();
        Calendar calInstance = Calendar.getInstance();

        transactions.add(new RecurringTransaction(new Date((long) 1561953600000f),
                                                  new Date(Long.MAX_VALUE),
                                                  150f,
                                                  false,
                                                  TransactionCategories.TRANSPORTATION,
                                                  new AbstractMap.SimpleEntry<Integer, Integer>(
                                                          Calendar.MONTH, 1),
                                                  "CarPayment"));

        transactions.add(
                new RecurringTransaction(new Date((long) 1561953600000f), new Date(Long.MAX_VALUE),
                                         50f, false, TransactionCategories.SUBSCRIPTION,
                                         new AbstractMap.SimpleEntry<Integer, Integer>(
                                                 Calendar.MONTH, 1), "Spotify"));
        transactions.add(
                new RecurringTransaction(new Date((long) 1561953600000f), new Date(Long.MAX_VALUE),
                                         10f, false, TransactionCategories.SUBSCRIPTION,
                                         new AbstractMap.SimpleEntry<Integer, Integer>(
                                                 Calendar.MONTH, 3), "DollarShaveClub"));
        transactions.add(
                new RecurringTransaction(new Date((long) 1564632000000f), new Date(Long.MAX_VALUE),
                                         50f, true, TransactionCategories.GIFT,
                                         new AbstractMap.SimpleEntry<Integer, Integer>(
                                                 Calendar.YEAR, 1), "Dad's Gift"));
        transactions.add(
                new RecurringTransaction(new Date((long) 1561953600000f), new Date(Long.MAX_VALUE),
                                         1f, false, TransactionCategories.GIFT,
                                         new AbstractMap.SimpleEntry<Integer, Integer>(
                                                 Calendar.MONTH, 1), "Wikipedia Donation"));
        return transactions;
    }

    private static HashMap<TransactionCategories, Float> createIdealBreakdown() {
        HashMap<TransactionCategories, Float> breakdown = new HashMap<>();
        breakdown.put(TransactionCategories.RENT, .35f);
        breakdown.put(TransactionCategories.TRANSPORTATION, .15f);
        breakdown.put(TransactionCategories.FOOD, .15f);
        breakdown.put(TransactionCategories.INVESTMENT, .10f);
        breakdown.put(TransactionCategories.BEAUTY, .05f);
        breakdown.put(TransactionCategories.PARTYING, .05f);
        breakdown.put(TransactionCategories.SUBSCRIPTION, .05f);
        breakdown.put(TransactionCategories.OTHER, .10f);
        return breakdown;
    }


}
