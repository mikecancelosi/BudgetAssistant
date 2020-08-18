package com.example.budgetassistant;

public enum Bank {
    BANKOFAMERICA("Bank Of America"),
    CAPITALONE("Capital One"),
    WELLSFARGO("Wells Fargo"),
    CITI("Citi"),
    CHASE("Chase Bank"),
    USBANK("US Bank");

    private String friendlyName;

    Bank(String friendly){
        friendlyName = friendly;
    }

    @Override
    public String toString() {
        return friendlyName;
    }
}
