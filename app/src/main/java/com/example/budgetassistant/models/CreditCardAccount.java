package com.example.budgetassistant.models;

import com.example.budgetassistant.Enums.CardCarrier;

import java.util.AbstractMap;

public class CreditCardAccount extends Account {
    public CardCarrier Card;
    public int CardNumber;
    public int CSV;
    public AbstractMap.SimpleEntry<Integer,Integer> Expiration;

    public CreditCardAccount(int cardNumber,int csv,CardCarrier card, AbstractMap.SimpleEntry<Integer,Integer> expiration, String displayName){
        Card = card;
        CardNumber = cardNumber;
        CSV = csv;
        Expiration = expiration;
        DisplayName = displayName;
        String numberDisplay = cardNumber + "";
        DisplayAccountNumber = "x" + numberDisplay.substring(numberDisplay.length()-4);
    }

}
