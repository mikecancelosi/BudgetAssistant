package com.example.budgetassistant.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.budgetassistant.DateExtensions;
import com.example.budgetassistant.Utils.TransactionUtil;
import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.Income;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.repositories.TransactionRepository;
import com.example.budgetassistant.repositories.UserSettingsRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<UserSettings> mSettings;
    public LiveData<UserSettings> getSettings(){return mSettings;}
    private UserSettingsRepository mSettingsRepo;

    private MutableLiveData<List<Transaction>> mTransactions;
    public LiveData<List<Transaction>> getTransactions(){return mTransactions;}
    private TransactionRepository mTransactionRepo;

    private BankAccount mAccount;



    public void init(){
        if(mSettings != null){
            return;
        }
        mTransactionRepo = TransactionRepository.getInstance();
        mSettingsRepo = UserSettingsRepository.getInstance();
        mTransactions = mTransactionRepo.getTransactions();

        mSettings = mSettingsRepo.getSettings();
        mAccount = (BankAccount) mSettings.getValue().accounts.get(0);


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

    public int getDaysUntilNextPaycheck()
    {
        Date nextPaycheckDate = getSettings().getValue().income.GetNextPaycheckDate();
        Calendar nowCal = Calendar.getInstance();
        return  DateExtensions.GetDaysBetween(nowCal.getTime(),nextPaycheckDate);
    }

    private List<Transaction> getTransactionsForPayPeriod(){
        List<Transaction> source = getTransactions().getValue();
        Income income = getSettings().getValue().income;
        Date startDate = income.GetLastPaycheckDate();
        Date endDate = income.GetNextPaycheckDate();

        return TransactionUtil.getTransactionsInTimeFrame(source, startDate, endDate);
    }

    public Float getExpenseAsPercentage(){
        float budget = getSettings().getValue().income.Amount;
        float expenses = 0f;
        List<Transaction> transactionsInPayPeriod = getTransactionsForPayPeriod();
        for(Transaction t : transactionsInPayPeriod){
            if(t.Amount < 0f) {
                expenses += t.Amount;
            }
        }

        return Math.abs(expenses/budget);
    }





}
