package com.example.budgetassistant.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.budgetassistant.Enums.Bank;
import com.example.budgetassistant.R;
import com.example.budgetassistant.Enums.TransactionCategories;
import com.example.budgetassistant.models.Account;
import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.Income;
import com.example.budgetassistant.models.RecurringTransaction;
import com.example.budgetassistant.models.UserSettings;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
                                    new AbstractMap.SimpleEntry<>(Calendar.WEEK_OF_YEAR, 2),
                                    new Date((long) 1561953600000f));
        dataSet.recurringTransactions = createRecurringTransactionPayments();
        dataSet.idealBreakdown = createIdealBreakdown();
        dataSet.accounts.add(new BankAccount((long)15202593282f,
                                             "Main",
                                             Bank.CHASE,
                                             (long)15223f));
        dataSet.accounts.add(new BankAccount((long)84641112479f,
                                             "Savings",
                                             Bank.USBANK,
                                             (long)151082f));
        dataSet.joinDate = Calendar.getInstance().getTime();
    }

    private static List<RecurringTransaction> createRecurringTransactionPayments() {
        List<RecurringTransaction> transactions = new ArrayList<RecurringTransaction>();

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

    public void postAccount(Account account){
        dataSet.accounts.add(account);
    }
    public void updateIncome(Income income) {dataSet.income = income;}

    public void postBreakdown( HashMap<TransactionCategories,Float> breakdown){dataSet.idealBreakdown = breakdown;}


}
