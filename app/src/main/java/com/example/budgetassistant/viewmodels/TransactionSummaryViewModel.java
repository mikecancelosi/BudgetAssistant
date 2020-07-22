package com.example.budgetassistant.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.budgetassistant.DateExtensions;
import com.example.budgetassistant.PayPeriodSummaryFragment;
import com.example.budgetassistant.TransactionCategories;
import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.Income;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.TransactionSummary;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.repositories.BankRepository;
import com.example.budgetassistant.repositories.TransactionRepository;
import com.example.budgetassistant.repositories.UserSettingsRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TransactionSummaryViewModel extends ViewModel {
    private MutableLiveData<TransactionSummary> mSummary;
    private TransactionRepository mTransactionRepo;
    private UserSettingsRepository mSettingsRepo;
    private Date mStartDate, mEndDate;

    public void init(){
        if(mSummary != null){
            return;
        }

        mTransactionRepo = TransactionRepository.getInstance();
        mSettingsRepo = UserSettingsRepository.getInstance();
        UserSettings settings = mSettingsRepo.getSettings().getValue();
        Income income = settings.GetIncome();

        mStartDate = income.LastPaycheck;
        mEndDate = income.GetNextPaycheckDate();

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

        setSummary();
    }

    public void init(Date startDate, Date endDate){
        if(mSummary != null){
            return;
        }
        mStartDate = startDate;
        mEndDate = endDate;

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


        setSummary();
    }

    private void setSummary(){
        TransactionSummary summary = new TransactionSummary();
        summary.StartDate = mStartDate;
        summary.EndDate = mEndDate;
        List<Transaction> transactionSourceData = mTransactionRepo.getTransactions().getValue();
        UserSettings settings = mSettingsRepo.getSettings().getValue();
        Income income = settings.GetIncome();
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

    public String GetTimePeriodLabel(){
        int daysInTimePeriod = getSummary().getValue().GetTimePeriodInDays();
        if(daysInTimePeriod == 7){
            return "Weekly";
        } else if(daysInTimePeriod > 25 && daysInTimePeriod < 33){
            return "Monthly";
        } else if(daysInTimePeriod > 80 && daysInTimePeriod < 105){
            return "Quarterly";
        } else if(daysInTimePeriod > 360){
            return "Annually";
        } else{
            return "Custom";
        }
    }

    public HashMap<TransactionCategories,Float> GetCategorizedExpenseValues(){
        float expenseTotal = 0f;
        HashMap<TransactionCategories,Float> categorizedData = new HashMap<TransactionCategories,Float>();
        TransactionSummary summary = getSummary().getValue();
        for(Transaction t : summary.Transactions){
            if(t.Expense > 0f) {
                expenseTotal += t.Expense;
                TransactionCategories category = t.Category;
                if (categorizedData.containsKey(category)) {
                    Float value = categorizedData.get(category);
                    value += t.Expense;
                } else {
                    categorizedData.put(category, t.Expense);
                }
            }
        }

        return categorizedData;
    }
}
