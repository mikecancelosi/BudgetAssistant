package com.example.budgetassistant.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.budgetassistant.models.BankAccount;

public class BankRepository {

    private static BankRepository instance;
    public static BankRepository getInstance(){
        if(instance == null){
            instance = new BankRepository();
        }
        return instance;
    }

    private BankAccount mAccount;
    public MutableLiveData<BankAccount> getAccount(){
        setAccount();
        MutableLiveData<BankAccount> data = new MutableLiveData<>();
        data.setValue(mAccount);
        return data;

    }
    public void setAccount(){
        mAccount = new BankAccount("x5554",13850,"TD Bank","Spending Account");
    }
}
