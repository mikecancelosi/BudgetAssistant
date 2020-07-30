package com.example.budgetassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.budgetassistant.R;
import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.Transaction;

import java.util.ArrayList;
import java.util.TreeSet;

public class BankAccountAdapter extends BaseAdapter {

    private ArrayList<BankAccount> mData = new ArrayList<BankAccount>();
    private LayoutInflater mInflater;
    private Context mContext;

    public BankAccountAdapter(Context c) {
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = c;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = mInflater.inflate(R.layout.bank_account,null);
        BankAccount account = (BankAccount)getItem(i);

        ImageView accountIconView = view.findViewById(R.id.AccountTypePic);
        TextView accountLabelView = view.findViewById(R.id.AccountNameLabel);
        TextView accountNumLabelView = view.findViewById(R.id.AccountNumberLabel);

        accountIconView.setImageResource(BankAccount.getBankIcon(account.BankName));
        accountLabelView.setText(account.DisplayName);
        accountNumLabelView.setText(account.AccountDisplay);

        return view;
    }

    public void addItem(BankAccount account){mData.add(account);}
}
