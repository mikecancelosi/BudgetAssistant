package com.example.budgetassistant.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.repositories.BankRepository;
import com.example.budgetassistant.repositories.TransactionRepository;
import com.example.budgetassistant.repositories.UserSettingsRepository;

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
        return -1;
    }

    public Float getExpenseAsPercentage(){
        return .1f;
    }



}
