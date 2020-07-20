package com.example.budgetassistant.models;

public class BankAccount {
    public String AccountNumber;
    public int Balance;
    public String BankName;
    public String DisplayName;

    public BankAccount(String accountNumber, int balance, String bankName, String displayName) {
        AccountNumber = accountNumber;
        Balance = balance;
        BankName = bankName;
        DisplayName = displayName;
    }
}
