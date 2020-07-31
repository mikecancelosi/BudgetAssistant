package com.example.budgetassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetassistant.R;
import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.UserSettings;

import java.util.ArrayList;
import java.util.List;

public class AlertAdapter extends  RecyclerView.Adapter<AlertAdapter.MyViewHolder> {

    LayoutInflater mInflater;
    List<Transaction> mData = new ArrayList<>();
    private Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public  TextView descriptionTextView;
        public TextView amountTextView;
        public TextView occurrenceTextView;
        public TextView countdownNumTextView;
        public TextView countdownUnitTextView;

        public MyViewHolder(View itemView){
            super(itemView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.Description);
            amountTextView = (TextView) itemView.findViewById(R.id.AmountTextView);
            occurrenceTextView = (TextView) itemView.findViewById(R.id.Occurrence);
            countdownNumTextView = (TextView) itemView.findViewById(R.id.CountdownNum);
            countdownUnitTextView = (TextView) itemView.findViewById(R.id.CountdownUnit);
        }
    }


    public AlertAdapter(List<Transaction> transactions) {
       mData = transactions;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public AlertAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert, parent, false);
        AlertAdapter.MyViewHolder vh = new AlertAdapter.MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(AlertAdapter.MyViewHolder holder, int position) {
        Transaction transaction = mData.get(position);

        String desc = transaction.Description;
        String cost = Float.toString(transaction.Expense);
        String occur = transaction.Frequency;

        holder.descriptionTextView.setText(desc);
        holder.amountTextView.setText(cost);
        holder.occurrenceTextView.setText(occur);
        holder.countdownNumTextView.setText("" + transaction.GetDaysLeftUntilNextRecurrentCharge());
        holder.countdownUnitTextView.setText("Days");
    }


    public void addItem(Transaction transaction){
        mData.add(transaction);
    }
}
