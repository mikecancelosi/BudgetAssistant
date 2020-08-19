package com.example.budgetassistant.models;

import com.example.budgetassistant.Bank;

public class BankAccount extends Account {
    public int AccountNumber;
    public int RoutingNumber;
    public Bank Bank;

    public BankAccount(int accountNumber, String displayName, Bank bank, int routingNumber) {
        AccountNumber = accountNumber;
        DisplayName = displayName;
        String numberDisplay = AccountNumber + "";
        DisplayAccountNumber = "x" + numberDisplay.substring(numberDisplay.length()-4);
        RoutingNumber = routingNumber;
        Bank = bank;
    }

}
