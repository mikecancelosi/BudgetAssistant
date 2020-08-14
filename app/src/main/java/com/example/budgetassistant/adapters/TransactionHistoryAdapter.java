package com.example.budgetassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.budgetassistant.HistoryFragment;
import com.example.budgetassistant.R;
import com.example.budgetassistant.TransactionCategories;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.repositories.TransactionRepository;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

public class TransactionHistoryAdapter  extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_HEADER = 1;

    private ArrayList<Transaction> mData = new ArrayList<>();
    private List<Integer> mHeaderIndices = new ArrayList<>();
    private LayoutInflater mInflater;

    private static DecimalFormat df = new DecimalFormat("0.00");
    private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
    private Context mContext;
    private HistoryFragment mFragment;

    public TransactionHistoryAdapter(Context context, HistoryFragment fragment){
        mContext = context;
        mFragment = fragment;
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

    private void initItem(View view, final int index){
        TextView descriptionText = view.findViewById(R.id.DescriptionTextView);
        TextView costText = view.findViewById(R.id.AmountTextView);
        Spinner catSpinner = view.findViewById(R.id.CategorySpinner);

        Transaction transaction = getItem(index);
        descriptionText.setText(transaction.Description);
        float amount = transaction.Amount;
        String displayAmount = "$" + df.format(amount);
        costText.setText(displayAmount);

        catSpinner.setAdapter(new ArrayAdapter<>(mContext,android.R.layout.simple_spinner_item,TransactionCategories.values()));
        int catIndex = transaction.Category.getValue();
        catSpinner.setSelection(catIndex);
        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mData.get(index).Category = TransactionCategories.values()[i];
                TransactionRepository.getInstance().postTransaction(mData.get(index));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragment.openEditDialog(index);
            }
        });
    }

    private void initHeader(View view,int index){
        TextView dateText = view.findViewById(R.id.DateTextView);
        TextView incText = view.findViewById(R.id.DateIncomeTotal);
        TextView expText = view.findViewById(R.id.DateExpenseTotal);

        //Find next header index
        int transEndIndex = mData.size();
        int indexInArray = mHeaderIndices.indexOf(index);
        if(mHeaderIndices.size() > indexInArray +1) {
            transEndIndex = mHeaderIndices.get(indexInArray + 1);
        }
        //get list of transactions under this header.
        List<Transaction> dateList = mData.subList(index, transEndIndex);
        //add income/expenses.
        float income = 0f;
        float expense =0f;
        for(Transaction t :dateList){
            if(t.Amount > 0f){
                income += t.Amount;
            }else{
                expense += t.Amount;
            }
        }
        //Display
        Transaction transaction = getItem(index);
        dateText.setText(sdf.format(transaction.DateOfTransaction));

        if(income > 0) {
            incText.setText("$" + df.format(income));
        }else{
            incText.setVisibility(View.INVISIBLE);
        }

        if(expense < 0){
            expText.setText("$" + df.format(expense));
        }else{
            expText.setVisibility(View.INVISIBLE);
        }
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
