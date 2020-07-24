package com.example.budgetassistant.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.repositories.BankRepository;

public class BankAccountViewModel extends ViewModel {
    private MutableLiveData<BankAccount> mAccount;
    private BankRepository mRepo;

    public void init(){
        if(mAccount != null){
            return;
        }
        mRepo = BankRepository.getInstance();
        mRepo.getAccount().observeForever(new Observer<BankAccount>() {
            @Override
            public void onChanged(BankAccount newAccount) {
                mAccount = mRepo.getAccount();
            }
        });
        mAccount = mRepo.getAccount();
    }

    public LiveData<BankAccount> getAccount(){
        return mAccount;
    }
}
