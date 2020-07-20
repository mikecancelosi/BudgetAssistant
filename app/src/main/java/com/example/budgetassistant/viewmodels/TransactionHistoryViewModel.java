package com.example.budgetassistant.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.repositories.TransactionRepository;

import java.util.List;

public class TransactionHistoryViewModel extends ViewModel {

    private MutableLiveData<List<Transaction>> mTransactions;
    private TransactionRepository mRepo;

    public void init(){
        if(mTransactions != null){
            return;
        }
        mRepo = TransactionRepository.getInstance();
        mRepo.getTransactions().observeForever(new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
               mTransactions = mRepo.getTransactions();

            }
        });
        mTransactions = mRepo.getTransactions();
    }

    public LiveData<List<Transaction>> getTransactions(){
        return mTransactions;
    }
}
