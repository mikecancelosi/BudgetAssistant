package com.example.budgetassistant.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.repositories.TransactionRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class TransactionHistoryViewModel extends ViewModel {

    private MutableLiveData<List<Transaction>> mTransactions;
    public LiveData<List<Transaction>> getTransactions(){return mTransactions;}
    private TransactionRepository mRepo;

    public void init(){
        if(mTransactions != null){
            return;
        }

        mRepo = TransactionRepository.getInstance();
        mTransactions = mRepo.getTransactions();
        mRepo.getTransactions().observeForever(new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                mTransactions.setValue(transactions);
            }
        });
    }

    public TreeMap<Date,List<Transaction>> getDatedTransactions(){
        TreeMap<Date,List<Transaction>> map = new TreeMap<>(Collections.<Date>reverseOrder());
        List<Transaction> sourceData = mRepo.getTransactions().getValue();
        for(Transaction trans : sourceData){
            Date transDate = trans.DateOfTransaction;
            if(map.containsKey(transDate)){
                map.get(transDate).add(trans);
            }else{
                map.put(transDate, Arrays.asList(trans));
            }
        }
        return map;
    }

}
