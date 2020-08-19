package com.example.budgetassistant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetassistant.R;
import com.example.budgetassistant.models.Account;
import com.example.budgetassistant.models.BankAccount;

import java.util.ArrayList;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder> {

    private List<Account> mData;

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

    public AccountAdapter(List<Account> accounts) {
        mData = accounts;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public AccountAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bank_account, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Account account = mData.get(position);

        holder.accountIconView.setImageResource(account.getIcon());
        holder.accountLabelView.setText(account.DisplayName);
        holder.accountNumLabelView.setText(account.DisplayAccountNumber);
    }
}
