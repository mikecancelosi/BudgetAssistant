package com.example.budgetassistant.models;

import com.example.budgetassistant.Bank;

public class BankAccount extends Account {
    public long AccountNumber;
    public long RoutingNumber;
    public Bank Bank;

    public BankAccount(long accountNumber, String displayName, Bank bank, long routingNumber) {
        AccountNumber = accountNumber;
        DisplayName = displayName;
        String numberDisplay = AccountNumber + "";
        DisplayAccountNumber = "x" + numberDisplay.substring(numberDisplay.length()-4);
        RoutingNumber = routingNumber;
        Bank = bank;
    }

}
