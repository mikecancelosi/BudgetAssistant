package com.example.budgetassistant.models;

import com.example.budgetassistant.R;

public class BankAccount {
    public String AccountNumber;
    public float Balance;
    public String BankName;
    public String DisplayName;
    public String AccountDisplay;

    public BankAccount(String accountNumber, float balance, String bankName, String displayName) {
        AccountNumber = accountNumber;
        Balance = balance;
        BankName = bankName;
        DisplayName = displayName;
        AccountDisplay = "x" + AccountNumber.substring(AccountNumber.length()-4);
    }

    public static int getBankIcon(String BankName){
        return R.mipmap.ic_visa;
    }
}
