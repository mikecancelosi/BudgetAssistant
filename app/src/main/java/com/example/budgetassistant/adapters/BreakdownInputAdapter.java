package com.example.budgetassistant.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetassistant.R;
import com.example.budgetassistant.Enums.TransactionCategories;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BreakdownInputAdapter extends RecyclerView.Adapter<BreakdownInputAdapter.MyViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public Spinner categorySpinner;
        public EditText categoryValue;
        public TextView unitView;

        private AbstractMap.SimpleEntry<TransactionCategories, Float> mItem;
        private int mIndex;
        private boolean mPercentView;
        private Float mIncomeAmount;
        private BreakdownViewHolderListener mListener;

        public interface BreakdownViewHolderListener {
            void onItemChanged(int index,
                               AbstractMap.SimpleEntry<TransactionCategories, Float> item);
        }


        public MyViewHolder(View itemView, final BreakdownViewHolderListener listener) {
            super(itemView);
            categorySpinner = itemView.findViewById(R.id.breakdownCategoryInputSpinner);
            categoryValue = itemView.findViewById(R.id.breakdownCategoryValueInput);
            unitView = itemView.findViewById(R.id.breakdownCategoryUnit);
            mListener = listener;


            TransactionCategories[] categories = TransactionCategories.values();
            List<String> categoryItems = new ArrayList<>();
            for (int i = 0; i < categories.length; i++) {
                if (categories[i] != TransactionCategories.INCOME) {
                    categoryItems.add(categories[i].toString());
                }
            }

            ArrayAdapter<String> catAdapter = new ArrayAdapter<>(categorySpinner.getContext(),
                                                                 R.layout.support_simple_spinner_dropdown_item,
                                                                 categoryItems);
            catAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            categorySpinner.setAdapter(catAdapter);

            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String selection = categorySpinner.getItemAtPosition(i).toString();
                    TransactionCategories newCategory = TransactionCategories.getCategoryFromFriendlyName(selection);
                    mItem = new AbstractMap.SimpleEntry<>(newCategory, mItem.getValue());
                    listener.onItemChanged(mIndex, mItem);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            categoryValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Float decimalValue  = decodeValue(charSequence.toString(),mPercentView,mIncomeAmount);
                    mItem.setValue(decimalValue);
                    listener.onItemChanged(mIndex, mItem);

                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

        }

        public void setItem(int index, AbstractMap.SimpleEntry<TransactionCategories, Float> item,
                            boolean percentView,
                            Float incomeAmount) {
            mIndex = index;
            mItem = item;
            mPercentView = percentView;
            mIncomeAmount = incomeAmount;
            int selectionValue = 0;

           SpinnerAdapter adapter = categorySpinner.getAdapter();
            int adapterValueCount = adapter.getCount();
            for(int i = 0; i< adapterValueCount; i++){
                String adapterStringAtIndex = adapter.getItem(i).toString();
                if(adapterStringAtIndex == item.getKey().toString()){
                    selectionValue = i;
                }
            }
            categorySpinner.setSelection(selectionValue);

            categoryValue.setText(encodeValue(item.getValue(), mPercentView, mIncomeAmount));
            unitView.setText(percentView ? "%" : "$");
        }

        private static Float decodeValue(String input, boolean percentView, Float incomeAmount) {
            if (percentView) {
                Float decimalValue = Float.parseFloat(input) / 100;
                return decimalValue;
            } else {
                Float decimalValue = Float.parseFloat(input) / incomeAmount;
                return decimalValue;
            }
        }

        private static String encodeValue(Float input, boolean percentView, Float incomeAmount) {
            if (percentView) {
                String convertedValue = String.format("%.2f", (input * 100));
                return convertedValue;
            } else {
                Float dollarValue = incomeAmount * input;
                String dollarText = String.format("%.2f", dollarValue);
                return dollarText;
            }
        }
    }

    //Value is in percentages.
    private Float mIncomeAmount;
    private List<AbstractMap.SimpleEntry<TransactionCategories, Float>> mBreakdown;
    private boolean mPercentView;

    public BreakdownInputAdapter(HashMap<TransactionCategories, Float> breakdown,
                                 Float incomeAmount) {
        mBreakdown = new ArrayList<>();
        for (TransactionCategories category : breakdown.keySet()) {
            mBreakdown.add(new AbstractMap.SimpleEntry<>(category, breakdown.get(category)));
        }
        mIncomeAmount = incomeAmount;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_breakdown_category_input,
                                                                     parent,
                                                                     false);
        MyViewHolder.BreakdownViewHolderListener listener = new MyViewHolder.BreakdownViewHolderListener() {

            @Override
            public void onItemChanged(int index,
                                      AbstractMap.SimpleEntry<TransactionCategories, Float> item) {
                updateItem(index,item);
            }
        };

        return new MyViewHolder(view,
                                listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setItem(position, mBreakdown.get(position), mPercentView, mIncomeAmount);
    }

    private void updateItem(int index, AbstractMap.SimpleEntry<TransactionCategories, Float> item){
        mBreakdown.set(index,item);
        mListener.applyChanges(getBreakdownAsHash());
    }

    public void changeView(boolean percent) {
        if (mPercentView != percent) {
            mPercentView = percent;
        }
    }

    @Override
    public int getItemCount() {
        return mBreakdown.size();
    }

    private HashMap<TransactionCategories, Float> getBreakdownAsHash() {
        HashMap<TransactionCategories, Float> map = new HashMap<>();

        for (AbstractMap.SimpleEntry<TransactionCategories, Float> entry : mBreakdown) {
            map.put(entry.getKey(), entry.getValue());
        }

        return map;
    }


    private BreakdownInputAdapterListener mListener;

    public void setListener(BreakdownInputAdapterListener listener) {
        mListener = listener;
    }

    public interface BreakdownInputAdapterListener {
        void applyChanges(HashMap<TransactionCategories, Float> breakdown);
    }

}
