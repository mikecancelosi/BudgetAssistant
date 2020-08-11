package com.example.budgetassistant.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.budgetassistant.CalendarHelper;
import com.example.budgetassistant.DateExtensions;
import com.example.budgetassistant.TransactionCategories;
import com.example.budgetassistant.TransactionHelper;
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
                map.put(category, existingValue + t.Amount);
            }else{
                map.put(category, t.Amount);
            }
       }

       return map;
    }

    public Float getUnspentBudgetForPeriod(Date startDate, Date endDate){
        List<Transaction> sourceData = getTransactions().getValue();
        List<Transaction> transactionsInPayPeriod = TransactionHelper.getTransactionsInTimeFrame(sourceData,startDate,endDate);
        Float expenses = TransactionHelper.getExpenseTotal(transactionsInPayPeriod);
        Float income = TransactionHelper.getIncomeTotal(transactionsInPayPeriod);
        return income - expenses;
    }

    public Float getIdealValueForCategory(TransactionCategories category,
                                          Date startDate, Date endDate,
                                          AbstractMap.SimpleEntry<Integer,Integer> period){

        HashMap<TransactionCategories,Float> breakdown = getSettings().getValue().idealBreakdown;
        if(breakdown.containsKey(category)){
            Float idealPercentage = breakdown.get(category);
            List<Transaction> transactions = TransactionHelper.getTransactionsInTimeFrame(mTransactions.getValue(),startDate,endDate);
            Float incomeTotal = TransactionHelper.getIncomeTotal(transactions);
            int periodIterations = CalendarHelper.countTimePeriodsBetweenDates(startDate,endDate,period);
            return (idealPercentage * incomeTotal) / periodIterations;
        }else{
            return 0f;
        }
    }

    public Float getCurrentValueForCategory(TransactionCategories category, Date startDate, Date endDate){
        HashMap<TransactionCategories,Float> map = getCategorizedExpensesForTimeLine(startDate,endDate);
        if(map.containsKey(category)){
            return map.get(category);
        }else{
            return 0f;
        }
    }

    public Float getLifetimeAverageValueForCategory(TransactionCategories category, int daysInComparison){
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
                catExpense += t.Amount;
            }
        }
        int daysOfData = DateExtensions.GetDaysBetween(startCal.getTime(),endCal.getTime());

        return (catExpense / daysOfData) * daysInComparison;
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



    public Date getPayPeriodStartDate(){
        return getSettings().getValue().income.GetLastPaycheckDate();
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
