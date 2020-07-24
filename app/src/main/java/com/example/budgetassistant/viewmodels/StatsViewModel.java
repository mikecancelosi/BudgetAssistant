package com.example.budgetassistant.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.budgetassistant.DateExtensions;
import com.example.budgetassistant.TransactionCategories;
import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.Income;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.TransactionSummary;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.repositories.BankRepository;
import com.example.budgetassistant.repositories.TransactionRepository;
import com.example.budgetassistant.repositories.UserSettingsRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class StatsViewModel extends ViewModel {
    private MutableLiveData<TransactionSummary> mSummary;
    private TransactionRepository mTransactionRepo;
    private UserSettingsRepository mSettingsRepo;

    public void init(){
        if(mSummary != null){
            return;
        }
        mTransactionRepo = TransactionRepository.getInstance();
        mSettingsRepo = UserSettingsRepository.getInstance();
        mTransactionRepo.getTransactions().observeForever(new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                setSummary();
            }
        });
        mSettingsRepo.getSettings().observeForever(new Observer<UserSettings>() {
            @Override
            public void onChanged(UserSettings settings) {
                setSummary();
            }
        });

    }

    public void setSummary(){
        TransactionSummary summary = new TransactionSummary();
        summary.StartDate = new Date(0L);
        summary.EndDate = new Date(Long.MAX_VALUE);
        List<Transaction> transactionSourceData = mTransactionRepo.getTransactions().getValue();
        UserSettings settings = mSettingsRepo.getSettings().getValue();
        Income income = settings.income;
        // Get transactions in the correct date range.
        List<Transaction> transactionsInRange = new ArrayList<>();
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(summary.StartDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(summary.EndDate);
        Calendar transCal = Calendar.getInstance();
        for(Transaction trans : transactionSourceData){
            transCal.setTime(trans.DateOfTransaction);
            if((transCal.after(startCal) || transCal.getTime() == startCal.getTime()) && transCal.before(endCal)){
                transactionsInRange.add(trans);
            }
        }
        summary.Transactions = transactionsInRange;
        //Set budget
        int periodInDays = DateExtensions.GetDaysBetween(startCal.getTime(),endCal.getTime());
        int paycheckCount = periodInDays / income.PayPeriodInDays;
        summary.Budget = income.Amount * paycheckCount;

        mSummary = new MutableLiveData<>();
        mSummary.setValue(summary);
    }
    public LiveData<TransactionSummary> getSummary(){
        return mSummary;
    }

    public HashMap<TransactionCategories,Float> getCategorizedExpenses(){
        return null;
    }

    public Float getUnspentBudget(){
        return 0f;
    }

    public Float getIdealValueForCategory(TransactionCategories category){
        return 0f;
    }

    public Float getLifetimeAverageValueForCategory(TransactionCategories category){
        return 0f;
    }

    public Float getCurrentValueForCategory(TransactionCategories category){
        return 0f;
    }

    public int getTimePeriodInDays(){
        return mSettingsRepo.getSettings().getValue().income.PayPeriodInDays;
    }
}
