package com.example.budgetassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.budgetassistant.R;
import com.example.budgetassistant.models.Transaction;

import java.util.ArrayList;

public class AlertAdapter extends  android.widget.BaseAdapter {

    LayoutInflater aInflater;
    ArrayList<Transaction> mData = new ArrayList<Transaction>();


    public AlertAdapter(Context c) {
        aInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Transaction getItem(int i) {return mData.get(i);}

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = aInflater.inflate(R.layout.alert,null);
        TextView descriptionTextView = (TextView) v.findViewById(R.id.Description);
        TextView amountTextView = (TextView) v.findViewById(R.id.AmountTextView);
        TextView occurrenceTextView = (TextView) v.findViewById(R.id.Occurrence);
        TextView countdownNumTextView = (TextView) v.findViewById(R.id.CountdownNum);
        TextView countdownUnitTextView = (TextView) v.findViewById(R.id.CountdownUnit);

        Transaction transaction = getItem(i);

        String desc = transaction.Description;
        String cost = Float.toString(transaction.Expense);
        String occur = transaction.Frequency;

        //TODO: Calculate countdown
        String countNum = Double.toString(Math.random() * 5f);
        String countUnit = "Days";

        descriptionTextView.setText(desc);
        amountTextView.setText(cost);
        occurrenceTextView.setText(occur);
        countdownNumTextView.setText(countNum);
        countdownUnitTextView.setText(countUnit);

        return v;
    }

    public void addItem(Transaction transaction){
        mData.add(transaction);
    }
}
