package com.example.budgetassistant.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.budgetassistant.Utils.CalendarUtil;
import com.example.budgetassistant.DateExtensions;
import com.example.budgetassistant.Enums.TransactionCategories;
import com.example.budgetassistant.Utils.TransactionUtil;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.repositories.TransactionRepository;
import com.example.budgetassistant.repositories.UserSettingsRepository;

import java.util.AbstractMap;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class StatsViewModel extends ViewModel {
    private UserSettingsRepository mSettingsRepo;
    private MutableLiveData<UserSettings> mSettings;

    public LiveData<UserSettings> getSettings() {
        return mSettings;
    }

    private TransactionRepository mTransactionRepo;
    private MutableLiveData<List<Transaction>> mTransactions;

    public LiveData<List<Transaction>> getTransactions() {
        return mTransactions;
    }


    public void init() {
        if (mSettings != null) {
            return;
        }
        mTransactionRepo = TransactionRepository.getInstance();
        mSettingsRepo = UserSettingsRepository.getInstance();
        mTransactions = mTransactionRepo.getTransactions();
        mSettings = mSettingsRepo.getSettings();

        mTransactionRepo.getTransactions().observeForever(new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                mTransactions.setValue(transactions);
            }
        });
        mSettingsRepo.getSettings().observeForever(new Observer<UserSettings>() {
            @Override
            public void onChanged(UserSettings settings) {
                mSettings.setValue(settings);
            }
        });
    }


    public HashMap<TransactionCategories, Float> getCategorizedExpenses(List<Transaction> transactions) {
        HashMap<TransactionCategories, Float> map = new HashMap<>();

        for (Transaction t : transactions) {
            TransactionCategories category = t.Category;
            if (category != TransactionCategories.INCOME) {
                if (map.containsKey(category)) {
                    float existingValue = map.get(category);
                    map.put(category, existingValue + Math.abs(t.Amount));
                } else {
                    map.put(category, Math.abs(t.Amount));
                }
            }
        }

        return map;
    }

    public Float getUnspentBudgetForPeriod(List<Transaction> transactions) {
        Float expenses = TransactionUtil.getExpenseTotal(transactions);
        Float income = TransactionUtil.getIncomeTotal(transactions);
        return income - expenses;
    }

    public Float getIdealValueForCategory(TransactionCategories category,
                                          Date startDate, Date endDate,
                                          List<Transaction> transactions,
                                          AbstractMap.SimpleEntry<Integer, Integer> period) {


        HashMap<TransactionCategories, Float> breakdown = getSettings().getValue().idealBreakdown;
        if (breakdown.containsKey(category)) {
            Float idealPercentage = breakdown.get(category);
            Float incomeTotal = TransactionUtil.getIncomeTotal(transactions);
            int periodIterations = CalendarUtil.countTimePeriodsBetweenDates(startDate,
                                                                             endDate,
                                                                             period);
            return (idealPercentage * incomeTotal) / periodIterations;
        } else {
            return 0f;
        }
    }

    public Float getCurrentValueForCategory(TransactionCategories category,
                                            List<Transaction> transactions) {
        HashMap<TransactionCategories, Float> map = getCategorizedExpenses(transactions);
        if (map.containsKey(category)) {
            return Math.abs(map.get(category));
        } else {
            return 0f;
        }
    }

    public Float getLifetimeAverageValueForCategory(TransactionCategories category,
                                                    int daysInComparison) {
        //Get start and end dates of transactional history to accurately measure average
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(new Date(Long.MAX_VALUE));
        Calendar endCal = Calendar.getInstance();
        Calendar transCal = Calendar.getInstance();
        List<Transaction> transactions = getTransactions().getValue();
        float catExpense = 0f;
        for (Transaction t : transactions) {
            transCal.setTime(t.DateOfTransaction);
            if (transCal.before(startCal)) {
                startCal.setTime(t.DateOfTransaction);
            }
            if (t.Category == category) {
                catExpense += Math.abs(t.Amount);
            }
        }
        int daysOfData = DateExtensions.GetDaysBetween(startCal.getTime(), endCal.getTime());

        return (catExpense / daysOfData) * daysInComparison;
    }

    public Float getExpensesInMonth(int monthsFromCurrent) {
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        startCal.add(Calendar.MONTH, -1 * monthsFromCurrent);
        endCal.add(Calendar.MONTH, -1 * monthsFromCurrent);
        //Set startCal to the first of the month
        startCal.set(Calendar.DAY_OF_MONTH, 1);
        //Set endCal to the end of the month
        endCal.add(Calendar.MONTH, 1);
        endCal.set(Calendar.DAY_OF_MONTH, 1);
        endCal.add(Calendar.DATE, -1); // TODO: Set to start/end of day.

        List<Transaction> transactionSourceData = getTransactions().getValue();
        List<Transaction> transactionInMonth = TransactionUtil.getTransactionsInTimeFrame(
                transactionSourceData,
                startCal.getTime(),
                endCal.getTime());
        return TransactionUtil.getExpenseTotal(transactionInMonth);
    }

    public List<Transaction> getTransactionsInTimePeriod(Date startDate, Date endDate) {
        return TransactionUtil.getTransactionsInTimeFrame(getTransactions().getValue(),
                                                          startDate,
                                                          endDate);
    }


    public Date getPayPeriodStartDate() {
        return getSettings().getValue().income.GetLastPaycheckDate();
    }

    public Date getPayPeriodEndDate() {
        return getSettings().getValue().income.GetNextPaycheckDate();
    }

    public Date getMonthStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
        return cal.getTime();
    }

    public Date getMonthEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.getActualMaximum(Calendar.DATE));
        return cal.getTime();
    }

    public Date getYearStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), 1, 1);
        return cal.getTime();
    }

    public Date getYearEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), 12, 31);
        return cal.getTime();
    }
}
