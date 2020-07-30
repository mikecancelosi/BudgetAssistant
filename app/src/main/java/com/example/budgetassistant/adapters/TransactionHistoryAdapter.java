package com.example.budgetassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.budgetassistant.R;
import com.example.budgetassistant.models.Transaction;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

public class TransactionHistoryAdapter  extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_HEADER = 1;

    private ArrayList<Transaction> mData = new ArrayList<Transaction>();
    private TreeSet<Integer> mHeaderIndices = new TreeSet<Integer>();
    private LayoutInflater mInflater;

    private static DecimalFormat df = new DecimalFormat("0.00");

    public TransactionHistoryAdapter(Context context){
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position){
        return mHeaderIndices.contains(position) ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Transaction getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int rowType = getItemViewType(i);

        switch (rowType) {
            case TYPE_ITEM:
                view = mInflater.inflate(R.layout.fragment_transaction, null);
                initItem(view,i);
                break;
            case TYPE_HEADER:
                view = mInflater.inflate(R.layout.fragment_history_date_header, null);
                initHeader(view,i);
                break;
        }
        return view;
    }

    private void initItem(View view, int index){
        TextView descriptionText = view.findViewById(R.id.DescriptionTextView);
        TextView costText = view.findViewById(R.id.AmountTextView);

        Transaction transaction = getItem(index);
        descriptionText.setText(transaction.Description);
        float amount = transaction.Income != 0f ? transaction.Income : transaction.Expense;
        String displayAmount = "$" + df.format(amount);
        costText.setText(displayAmount);
    }

    private void initHeader(View view,int index){
        TextView dateText = view.findViewById(R.id.DateTextView);

        Transaction transaction = getItem(index);
        dateText.setText(transaction.DateOfTransaction.toString());
    }

    public void addItem(final Transaction transaction){
        mData.add(transaction);
        notifyDataSetChanged();
    }

    public void addHeader(Date date){
        mData.add(new Transaction(date));
        mHeaderIndices.add(mData.size()-1);
        notifyDataSetChanged();
    }
}
