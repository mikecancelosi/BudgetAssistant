package com.example.budgetassistant.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.budgetassistant.DateExtensions;
import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.Income;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.repositories.BankRepository;
import com.example.budgetassistant.repositories.TransactionRepository;
import com.example.budgetassistant.repositories.UserSettingsRepository;

import java.util.ArrayList;
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

    private MutableLiveData<BankAccount> mAccount;
    public LiveData<BankAccount> getAccount(){return mAccount;}
    private BankRepository mBankRepo;


    public void init(){
        if(mSettings != null){
            return;
        }
        mTransactionRepo = TransactionRepository.getInstance();
        mSettingsRepo = UserSettingsRepository.getInstance();
        mBankRepo = BankRepository.getInstance();

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
        mBankRepo.getAccount().observeForever(new Observer<BankAccount>() {
            @Override
            public void onChanged(BankAccount account) {
                mAccount.setValue(account);
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
        List<Transaction> transactions = new ArrayList<>();
        Calendar startCal = Calendar.getInstance();
        Income inc = getSettings().getValue().income;
        startCal.setTime(inc.LastPaycheck);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(inc.GetNextPaycheckDate());
        Calendar transCal = Calendar.getInstance();
        for(Transaction t : getTransactions().getValue()){
           transCal.setTime(t.DateOfTransaction);
            if(startCal.getTime() == t.DateOfTransaction || (transCal.after(startCal) && transCal.before(endCal))){
                transactions.add(t);
            }
        }

        return transactions;
    }

    public Float getExpenseAsPercentage(){
        float budget = getSettings().getValue().budget;
        float expenses = 0f;
        List<Transaction> transactionsInPayPeriod = getTransactionsForPayPeriod();
        for(Transaction t : transactionsInPayPeriod){
            expenses += t.Expense;
        }

        return expenses/budget;


    }



}
