package com.example.budgetassistant.models;

import java.util.AbstractMap;

public class CreditCardAccount extends Account {
    public int CardNumber;
    public int CSV;
    public AbstractMap.SimpleEntry<Integer,Integer> Expiration;
}
