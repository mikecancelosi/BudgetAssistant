package com.example.budgetassistant;

import androidx.annotation.NonNull;

public enum TransactionCategories {
    RENT("Rent",0),
    UTILITIES("Utilities",1),
    TRANSPORTATION("Transportation",2),
    HEALTH("Health",3),
    PARTYING("Partying",4),
    INCOME("Income",5),
    LOVE("Love",6),
    FOOD("Food",7),
    BEAUTY("Beauty",8),
    GIFT("Gift",9),
    UPKEEP("Upkeep",10),
    VACATION("Vacation",11),
    INVESTMENT("Investment",12),
    SAVINGS("Savings",13),
    SUBSCRIPTION("Subscription",14),
    OTHER("Other",15);

    private String friendlyName;
    private int value;

    TransactionCategories(String friendly,int val){
        friendlyName = friendly;
        value = val;
    }

    public int getValue(){return value;}

    @Override
    public String toString() {
        return friendlyName;
    }
}

