package com.example.budgetassistant.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.repositories.TransactionRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

    //TODO: Limit to short time period, load more on scroll
    public TreeMap<Date,List<Transaction>> getDatedTransactions(){
        TreeMap<Date,List<Transaction>> map = new TreeMap<>(Collections.<Date>reverseOrder());
        List<Transaction> sourceData = mRepo.getTransactions().getValue();
        Calendar transCal = Calendar.getInstance();
        for(Transaction trans : sourceData){
            transCal.setTime(trans.DateOfTransaction);
            transCal.set(Calendar.HOUR_OF_DAY,0);
            transCal.set(Calendar.MINUTE,0);
            transCal.set(Calendar.SECOND,0);
            transCal.set(Calendar.MILLISECOND,0);

            if(map.containsKey(transCal.getTime())){
                Date d = transCal.getTime();
                List<Transaction> transList = new ArrayList<>(map.get(d));
                transList.add(trans);
                map.put(transCal.getTime(),transList);
            }else{
                map.put(transCal.getTime(), Arrays.asList(trans));
            }
        }
        return map;
    }

}
