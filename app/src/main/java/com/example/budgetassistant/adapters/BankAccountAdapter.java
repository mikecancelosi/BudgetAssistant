package com.example.budgetassistant.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetassistant.R;
import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.Transaction;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class BankAccountAdapter extends RecyclerView.Adapter<BankAccountAdapter.MyViewHolder> {

    private List<BankAccount> mData = new ArrayList<BankAccount>();
    private LayoutInflater mInflater;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView accountIconView;
        public TextView accountLabelView;
        public TextView accountNumLabelView;
        public MyViewHolder(View itemView){
            super(itemView);
            accountIconView = itemView.findViewById(R.id.AccountTypePic);;
            accountLabelView = itemView.findViewById(R.id.AccountNameLabel);
            accountNumLabelView = itemView.findViewById(R.id.AccountNumberLabel);
        }
    }

    public BankAccountAdapter(List<BankAccount> accounts) {
        mData = accounts;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public BankAccountAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bank_account, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BankAccount account = mData.get(position);

        holder.accountIconView.setImageResource(BankAccount.getBankIcon(account.Bank.name()));
        holder.accountLabelView.setText(account.DisplayName);
        holder.accountNumLabelView.setText(account.getAccountDisplay());
    }

    public void addItem(BankAccount account){mData.add(account);}
}
