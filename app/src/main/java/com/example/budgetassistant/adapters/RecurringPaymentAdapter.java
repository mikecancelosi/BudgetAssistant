package com.example.budgetassistant.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetassistant.R;
import com.example.budgetassistant.dialogs.RecurringPaymentDialog;
import com.example.budgetassistant.models.RecurringTransaction;
import com.example.budgetassistant.models.Transaction;

import java.util.List;

public class RecurringPaymentAdapter extends  RecyclerView.Adapter<RecurringPaymentAdapter.MyViewHolder> {

    List<RecurringTransaction> mData;
    View view;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public  TextView descriptionTextView;
        public TextView amountTextView;
        public TextView occurrenceTextView;
        public TextView countdownNumTextView;
        public TextView countdownUnitTextView;
        public LinearLayout layout;

        public MyViewHolder(View itemView){
            super(itemView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.Description);
            amountTextView = (TextView) itemView.findViewById(R.id.AmountTextView);
            occurrenceTextView = (TextView) itemView.findViewById(R.id.Occurrence);
            countdownNumTextView = (TextView) itemView.findViewById(R.id.CountdownNum);
            countdownUnitTextView = (TextView) itemView.findViewById(R.id.CountdownUnit);
            layout = (LinearLayout) itemView.findViewById(R.id.Alert);
        }
    }


    public RecurringPaymentAdapter(List<RecurringTransaction> transactions) {
       mData = transactions;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public RecurringPaymentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recurring_payment, parent, false);
        RecurringPaymentAdapter.MyViewHolder vh = new RecurringPaymentAdapter.MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecurringPaymentAdapter.MyViewHolder holder, final int position) {
        RecurringTransaction transaction = mData.get(position);

        String desc = transaction.Description;
        String cost = Float.toString(transaction.Amount);
        String occur = transaction.getFrequency();

        holder.descriptionTextView.setText(desc);
        holder.amountTextView.setText(transaction.Varies ? "~$" + cost : "$"+cost);
        holder.occurrenceTextView.setText(occur);
        holder.countdownNumTextView.setText("" + transaction.GetDaysLeftUntilNextRecurrentCharge());
        holder.countdownUnitTextView.setText("Days");
        holder.layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openDialog(position);
            }
        });

    }

    public void openDialog(int position){
        RecurringPaymentDialog dialog = new RecurringPaymentDialog();
        RecurringTransaction transaction = mData.get(position);
        dialog.setTransaction(transaction);
        dialog.show(((FragmentActivity)view.getContext()).getSupportFragmentManager(),"Example");
    }


    public void addItem(RecurringTransaction transaction){
        mData.add(transaction);
    }
}
