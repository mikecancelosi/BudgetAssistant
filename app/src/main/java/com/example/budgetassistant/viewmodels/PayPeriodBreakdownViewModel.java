package com.example.budgetassistant.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.budgetassistant.TransactionCategories;
import com.example.budgetassistant.models.Income;
import com.example.budgetassistant.models.PayPeriodBreakdown;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.repositories.TransactionRepository;
import com.example.budgetassistant.repositories.UserSettingsRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.max;
import static java.time.temporal.ChronoUnit.DAYS;

public class PayPeriodBreakdownViewModel extends ViewModel {
    private MutableLiveData<PayPeriodBreakdown> payPeriodBreakdown;
    private TransactionRepository mRepo;
    private UserSettingsRepository mSettingsRepo;
    private UserSettings mSettings;


    public void init(){
        if(payPeriodBreakdown != null){
            return;
        }
        mSettingsRepo = UserSettingsRepository.getInstance();
        mSettings = mSettingsRepo.getSettings().getValue();

        mRepo = TransactionRepository.getInstance();
        mRepo.getTransactions().observeForever(new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                setPayPeriodBreakdown(mRepo.getTransactions().getValue());

            }
        });
        setPayPeriodBreakdown(mRepo.getTransactions().getValue());

    }

    private void setPayPeriodBreakdown(List<Transaction> sourceData){
        PayPeriodBreakdown breakdown;

       float totalSpent = getTotalSpent(Objects.requireNonNull(mRepo.getTransactions().getValue()));
       float budget = mSettings.getBudget();
       float spentPercentage = budget / totalSpent;

       Income income = mSettings.GetIncome();
       int daysLeftInPayPeriod = income.GetNumberOfDaysToNextPaycheck();
       int payPeriodInDays = income.PayPeriodInDays;
       float idealSpentPercentage = (float)daysLeftInPayPeriod/payPeriodInDays;

        breakdown = new PayPeriodBreakdown(spentPercentage,max(0,1f-spentPercentage),idealSpentPercentage);
        payPeriodBreakdown = new MutableLiveData<>();
        payPeriodBreakdown.setValue(breakdown);
    }

    private static float getTotalSpent(List<Transaction> sourceData){
        float total = 0f;
        // Combine all similarly categorized transactions into one transaction.
        for(Transaction trans : sourceData){
           total += trans.Expense;
        }
        return total;
    }

    public LiveData<PayPeriodBreakdown> getBreakdown(){
        return payPeriodBreakdown;
    }
    public int getDaysLeftInPayPeriod(){return mSettings.GetIncome().GetNumberOfDaysToNextPaycheck();}
}