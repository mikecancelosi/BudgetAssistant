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

        public interface BreakdownViewHolderListener{
            void onCategorySelected(int index, TransactionCategories category);
            void onValueChanged(int index, Float rawValue);
        }

        private BreakdownViewHolderListener mListener;

        public MyViewHolder(View itemView,final BreakdownViewHolderListener listener) {
            super(itemView);
            categorySpinner = itemView.findViewById(R.id.breakdownCategoryInputSpinner);
            categoryValue = itemView.findViewById(R.id.breakdownCategoryValueInput);
            unitView = itemView.findViewById(R.id.breakdownCategoryUnit);
            mListener = listener;

            TransactionCategories[] categories = TransactionCategories.values();
            String[] categoryItems = new String[categories.length];
            for (int i = 0; i < categories.length; i++) {
                if (categories[i] != TransactionCategories.INCOME) {
                    categoryItems[i] = categories[i].name();
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
                    listener.onCategorySelected(mIndex,TransactionCategories.valueOf(selection));
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
                    Float rawValue = Float.parseFloat(charSequence.toString());
                    listener.onValueChanged(mIndex,rawValue);

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
            categorySpinner.setSelection(item.getKey().getValue());

            if (percentView) {
                String convertedValue = String.format("%.2f", (item.getValue() * 100));
                categoryValue.setText(convertedValue);
                unitView.setText("%");

            } else {
                Float dollarValue = incomeAmount * item.getValue();
                String dollarText = String.format("%.2f", dollarValue);
                categoryValue.setText(dollarText);
                unitView.setText("$");
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
            public void onCategorySelected(int index, TransactionCategories category) {
                updateItem(index,category);
            }

            @Override
            public void onValueChanged(int index,Float rawValue) {

            }
        };

        BreakdownInputAdapter.MyViewHolder viewHolder = new BreakdownInputAdapter.MyViewHolder(view,listener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       holder.setItem(position, mBreakdown.get(position),mPercentView,mIncomeAmount);
    }

    private void updateItem(int index, TransactionCategories newCategory) {
        AbstractMap.SimpleEntry<TransactionCategories, Float> oldEntry = getItem(index);
        mBreakdown.set(index, new AbstractMap.SimpleEntry<>(newCategory, oldEntry.getValue()));
        mListener.applyChanges(getBreakdownAsHash());

    }

    private void updateItem(int index, Float newValue) {
        Float decimalValue;

        if (mPercentView) {
            decimalValue = (newValue / 100f);
        } else {
            decimalValue = newValue / mIncomeAmount;
        }

        Log.d("!!", mBreakdown.get(index).getKey() + " | " + newValue);
        mBreakdown.get(index).setValue(decimalValue);
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

    public AbstractMap.SimpleEntry<TransactionCategories, Float> getItem(int index) {
        return mBreakdown.get(index);
    }

    private boolean doesCategoryExistInBreakdown(TransactionCategories category) {
        for (AbstractMap.SimpleEntry<TransactionCategories, Float> entry : mBreakdown) {
            if (entry.getKey() == category) {
                return true;
            }
        }
        return false;
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
