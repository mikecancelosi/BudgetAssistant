package com.example.budgetassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.budgetassistant.R;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.UserSettings;

import java.util.ArrayList;

public class AlertAdapter extends  android.widget.BaseAdapter {

    LayoutInflater aInflater;
    ArrayList<Transaction> mData = new ArrayList<>();
    private Context mContext;


    public AlertAdapter(Context c) {
        aInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = c;
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

         view = aInflater.inflate(R.layout.alert,null);

        TextView descriptionTextView = (TextView) view.findViewById(R.id.Description);
        TextView amountTextView = (TextView) view.findViewById(R.id.AmountTextView);
        TextView occurrenceTextView = (TextView) view.findViewById(R.id.Occurrence);
        TextView countdownNumTextView = (TextView) view.findViewById(R.id.CountdownNum);
        TextView countdownUnitTextView = (TextView) view.findViewById(R.id.CountdownUnit);

        Transaction transaction = getItem(i);

        String desc = transaction.Description;
        String cost = Float.toString(transaction.Expense);
        String occur = transaction.Frequency;



        descriptionTextView.setText(desc);
        amountTextView.setText(cost);
        occurrenceTextView.setText(occur);
        countdownNumTextView.setText("" + transaction.GetDaysLeftUntilNextRecurrentCharge());
        countdownUnitTextView.setText("Days");

        return view;
    }



    public void addItem(Transaction transaction){
        mData.add(transaction);
    }
}
