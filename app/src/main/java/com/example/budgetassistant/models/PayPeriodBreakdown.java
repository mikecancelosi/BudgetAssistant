package com.example.budgetassistant.models;

import java.util.ArrayList;

public class PayPeriodBreakdown {
    public float spentPercentage;
    public float unspentPercentage;
    public float idealPercentageSpent;

    public PayPeriodBreakdown(float spentPercentage, float unspentPercentage, float idealPercentageSpent) {
        this.spentPercentage = spentPercentage;
        this.unspentPercentage = unspentPercentage;
        this.idealPercentageSpent = idealPercentageSpent;
    }
}
