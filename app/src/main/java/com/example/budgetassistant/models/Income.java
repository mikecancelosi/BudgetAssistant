package com.example.budgetassistant.models;

import java.util.Date;

public class Income {
    public float Amount;
    public int PayPeriodInDays;
    public Date LastPaycheck;

    public Income(float amount, int payPeriodInDays, Date lastPaycheck) {
        Amount = amount;
        PayPeriodInDays = payPeriodInDays;
        LastPaycheck = lastPaycheck;
    }
}
