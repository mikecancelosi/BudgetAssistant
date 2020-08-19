package com.example.budgetassistant.models;

import android.media.Image;

import com.example.budgetassistant.Bank;
import com.example.budgetassistant.R;

public abstract class Account {
    public String DisplayAccountNumber;
    public String DisplayName;

    public int getIcon(){
        return R.mipmap.ic_visa;
    }

}
