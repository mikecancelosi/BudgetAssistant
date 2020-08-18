package com.example.budgetassistant.models;

import com.example.budgetassistant.Bank;
import com.example.budgetassistant.R;

public class BankAccount extends Account {
    public String AccountNumber;
    public String RoutingNumber;
    public Bank Bank;
    public String DisplayName;

    public BankAccount(String accountNumber, String displayName, Bank bank, String routingNumber) {
        AccountNumber = accountNumber;
        DisplayName = displayName;
        RoutingNumber = routingNumber;
        Bank = bank;
    }

    public String getAccountDisplay(){ return "x" + AccountNumber.substring(AccountNumber.length()-4);}
    public static int getBankIcon(String BankName){
        return R.mipmap.ic_visa;
    }
}
