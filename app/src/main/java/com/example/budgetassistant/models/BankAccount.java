package com.example.budgetassistant.models;

public class BankAccount {
    public String AccountNumber;
    public float Balance;
    public String BankName;
    public String DisplayName;

    public BankAccount(String accountNumber, float balance, String bankName, String displayName) {
        AccountNumber = accountNumber;
        Balance = balance;
        BankName = bankName;
        DisplayName = displayName;
    }
}
