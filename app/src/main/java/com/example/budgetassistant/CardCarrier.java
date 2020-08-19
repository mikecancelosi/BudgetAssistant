package com.example.budgetassistant;

public enum CardCarrier {
    VISA("Visa"),
    MASTERCARD("Mastercard"),
    AMEX("American Express"),
    DISCOVER("Discover");

    private String friendlyName;

    CardCarrier(String friendly){
        friendlyName = friendly;
    }

    @Override
    public String toString() {
        return friendlyName;
    }
}
