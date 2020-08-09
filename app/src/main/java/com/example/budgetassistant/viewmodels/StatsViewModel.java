package com.example.budgetassistant.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.budgetassistant.DateExtensions;
import com.example.budgetassistant.TransactionCategories;
import com.example.budgetassistant.TransactionHelper;
import com.example.budgetassistant.models.Income;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.repositories.TransactionRepository;
import com.example.budgetassistant.repositories.UserSettingsRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class StatsViewModel extends ViewModel {
    private UserSettingsRepository mSettingsRepo;
    private MutableLiveData<UserSettings> mSettings;
    public LiveData<UserSettings> getSettings(){
        return mSettings;
    }
    private TransactionRepository mTransactionRepo;
    private MutableLiveData<List<Transaction>> mTransactions;
    public LiveData<List<Transaction>> getTransactions(){
        return mTransactions;
    }

    public void init(){
        if(mSettings != null){
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


    public HashMap<TransactionCategories,Float> getCategorizedExpensesForTimeLine(Date startDate, Date endDate){
       HashMap<TransactionCategories,Float> map = new HashMap<>();
       List<Transaction> source = getTransactions().getValue();

       for(Transaction t : TransactionHelper.getTransactionsInTimeFrame(source,startDate,endDate)){
            TransactionCategories category = t.Category;
            if(map.containsKey(category)){
                float existingValue = map.get(category);
                map.put(category, existingValue + t.Expense);
            }else{
                map.put(category, t.Expense);
            }
       }

       return map;
    }

    public Float getUnspentBudgetForPayPeriod(){
        Income income = getSettings().getValue().income;
        List<Transaction> sourceData = getTransactions().getValue();
        Date startDate = income.LastPaycheck;
        Date endDate = income.GetNextPaycheckDate();
        List<Transaction> transactionsInPayPeriod = TransactionHelper.getTransactionsInTimeFrame(sourceData,startDate,endDate);
        Float expenses = TransactionHelper.getExpenseTotal(transactionsInPayPeriod);
        return income.Amount - expenses;
    }

    public Float getIdealValueForCategory(TransactionCategories category){
        HashMap<TransactionCategories,Float> breakdown = getSettings().getValue().idealBreakdown;
        if(breakdown.containsKey(category)){
            return breakdown.get(category);
        }else{
            return 0f;
        }
    }

    public Float getLifetimeAverageValueForCategory(TransactionCategories category){
        //Get start and end dates of transactional history to accurately measure average
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(new Date(Long.MAX_VALUE));
        Calendar endCal = Calendar.getInstance();
        Calendar transCal = Calendar.getInstance();
        List<Transaction> transactions = getTransactions().getValue();
        float catExpense = 0f;
        for(Transaction t :transactions){
            transCal.setTime(t.DateOfTransaction);
            if(transCal.before(startCal)){
                startCal.setTime(t.DateOfTransaction);
            }
            if(t.Category == category)
            {
                catExpense += t.Expense;
            }
        }
        int daysOfData = DateExtensions.GetDaysBetween(startCal.getTime(),endCal.getTime());

        return catExpense / daysOfData;
    }

    public Float getExpensesInMonth(int monthsFromCurrent){
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        startCal.add(Calendar.MONTH,-1 * monthsFromCurrent);
        endCal.add(Calendar.MONTH,-1 * monthsFromCurrent);
        //Set startCal to the first of the month
        startCal.set(Calendar.DAY_OF_MONTH, 1);
        //Set endCal to the end of the month
        endCal.add(Calendar.MONTH,1);
        endCal.set(Calendar.DAY_OF_MONTH, 1);
        endCal.add(Calendar.DATE,-1); // TODO: Set to start/end of day.

       List<Transaction> transactionSourceData = getTransactions().getValue();
       List<Transaction> transactionInMonth = TransactionHelper.getTransactionsInTimeFrame(transactionSourceData,startCal.getTime(),endCal.getTime());
       return TransactionHelper.getExpenseTotal(transactionInMonth);
    }


    public Float getCurrentValueForCategory(TransactionCategories category){
        HashMap<TransactionCategories,Float> map = getCategorizedExpensesForTimeLine(getPayPeriodStartDate(),getPayPeriodEndDate());
        if(map.containsKey(category)){
            return map.get(category);
        }else{
            return 0f;
        }
    }

    public int getTimePeriodInDays(){
        return mSettingsRepo.getSettings().getValue().income.PayPeriodInDays;
    }

    public Date getPayPeriodStartDate(){
        return getSettings().getValue().income.LastPaycheck;
    }

    public Date getPayPeriodEndDate(){
        return getSettings().getValue().income.GetNextPaycheckDate();
    }

    public Date getMonthStartDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),1);
        return cal.getTime();
    }

    public Date getMonthEndDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.getActualMaximum(Calendar.DATE));
        return cal.getTime();
    }

    public Date getYearStartDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),1,1);
        return cal.getTime();
    }

    public Date getYearEndDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),12,31);
        return cal.getTime();
    }
}
