package com.example.budgetassistant.adapters;

import android.graphics.Color;
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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetassistant.R;
import com.example.budgetassistant.Enums.TransactionCategories;
import com.example.budgetassistant.models.Transaction;

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
        private List<TransactionCategories> mPrexistingCategories;
        private boolean mBinding;

        public interface BreakdownViewHolderListener {
            void onItemChanged(int index,
                               AbstractMap.SimpleEntry<TransactionCategories, Float> item);
        }


        public MyViewHolder(View itemView,
                            List<TransactionCategories> prexistingCategories,
                            final BreakdownViewHolderListener listener) {
            super(itemView);
            mBinding = true;
            categorySpinner = itemView.findViewById(R.id.breakdownCategoryInputSpinner);
            categoryValue = itemView.findViewById(R.id.breakdownCategoryValueInput);
            unitView = itemView.findViewById(R.id.breakdownCategoryUnit);
            mListener = listener;
            mPrexistingCategories = prexistingCategories;


            TransactionCategories[] categories = TransactionCategories.values();
            final List<String> categoryItems = new ArrayList<>();
            for (int i = 0; i < categories.length; i++) {
                if (categories[i] != TransactionCategories.INCOME) {
                    categoryItems.add(categories[i].toString());
                }
            }

            final ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(categorySpinner.getContext(),
                                                                       R.layout.support_simple_spinner_dropdown_item,
                                                                       categoryItems){
                @Override
                public View getDropDownView(int position,
                                            @Nullable View convertView,
                                            @NonNull ViewGroup parent) {
                    View mView = super.getDropDownView(position,convertView,parent);
                    TextView mTextView = (TextView)mView;
                    String valueAtPosition = categoryItems.get(position);
                    TransactionCategories category = TransactionCategories.getCategoryFromFriendlyName(valueAtPosition);
                    boolean enabled = !mPrexistingCategories.contains(category);
                    mTextView.setTextColor(enabled ? Color.BLACK : Color.GRAY);
                    return mView;
                }

                @Override
                public boolean isEnabled(int position) {
                    String valueAtPosition = categoryItems.get(position);
                    TransactionCategories category = TransactionCategories.getCategoryFromFriendlyName(valueAtPosition);
                    return !mPrexistingCategories.contains(category);
                }
            };
            catAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            categorySpinner.setAdapter(catAdapter);

            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String selection = categorySpinner.getItemAtPosition(i).toString();
                    TransactionCategories newCategory = TransactionCategories.getCategoryFromFriendlyName(
                            selection);
                    mItem = new AbstractMap.SimpleEntry<>(newCategory, mItem.getValue());
                    if(!mBinding) {
                        listener.onItemChanged(mIndex, mItem);
                    }
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
                    Float decimalValue = decodeValue(charSequence.toString(),
                                                     mPercentView,
                                                     mIncomeAmount);
                    mItem.setValue(decimalValue);
                    if(!mBinding) {
                        listener.onItemChanged(mIndex, mItem);
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
         mBinding = false;
        }

        public void setItem(int index, AbstractMap.SimpleEntry<TransactionCategories, Float> item,
                            List<TransactionCategories> prexistingCategories,
                            boolean percentView,
                            Float incomeAmount) {
            mBinding = true;
            mIndex = index;
            mItem = item;
            mPercentView = percentView;
            mIncomeAmount = incomeAmount;
            mPrexistingCategories = prexistingCategories;
            int selectionValue = 0;

            SpinnerAdapter adapter = categorySpinner.getAdapter();

            int adapterValueCount = adapter.getCount();
            for (int i = 0; i < adapterValueCount; i++) {
                String adapterStringAtIndex = adapter.getItem(i).toString();
                if (adapterStringAtIndex.equals(item.getKey().toString())) {
                    selectionValue = i;
                }
            }
            categorySpinner.setSelection(selectionValue);

            categoryValue.setText(encodeValue(item.getValue(), mPercentView, mIncomeAmount));
            unitView.setText(percentView ? "%" : "$");
            mBinding = false;
        }

        private static Float decodeValue(String input, boolean percentView, Float incomeAmount) {
            if (percentView) {
                return Float.parseFloat(input) / 100;
            } else {
                Float decimalValue = Float.parseFloat(input) / incomeAmount;
                return decimalValue;
            }
        }

        private static String encodeValue(Float input, boolean percentView, Float incomeAmount) {
            if (percentView) {
                return String.format("%.2f", (input * 100));
            } else {
                Float dollarValue = incomeAmount * input;
                return String.format("%.2f", dollarValue);
            }
        }
    }

    //Value is in percentage decimal (.10)
    private Float mIncomeAmount;
    private List<AbstractMap.SimpleEntry<TransactionCategories, Float>> mBreakdown;
    private boolean mPercentView;
    private BreakdownInputAdapterListener mListener;
    private boolean mBinding = false;
    private List<TransactionCategories> mExistingCategories;

    public BreakdownInputAdapter(HashMap<TransactionCategories, Float> breakdown,
                                 Float incomeAmount) {
        mBreakdown = new ArrayList<>();
        for (TransactionCategories category : breakdown.keySet()) {
            mBreakdown.add(new AbstractMap.SimpleEntry<>(category, breakdown.get(category)));
        }
        mIncomeAmount = incomeAmount;
        mExistingCategories = getExistingCategories();
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
                updateItem(index, item);
            }
        };

        return new MyViewHolder(view, mExistingCategories,
                                listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        mBinding = true;
        holder.setItem(position, mBreakdown.get(position),mExistingCategories, mPercentView, mIncomeAmount);
        mBinding = false;
    }

    @Override
    public int getItemCount() {
        return mBreakdown.size();
    }

    private void updateItem(int index, AbstractMap.SimpleEntry<TransactionCategories, Float> item) {
        AbstractMap.SimpleEntry<TransactionCategories, Float> itemBefore = mBreakdown.get(index);
        if(itemBefore.getKey() != item.getKey() ||
           !itemBefore.getValue().equals(item.getValue())) {
            mBreakdown.set(index, item);
            mExistingCategories = getExistingCategories();
            mListener.applyChanges(getBreakdownAsHash());
            if (!mBinding) {
                notifyDataSetChanged();
            }
        }
    }

    public void changeView(boolean percent) {
        if (mPercentView != percent) {
            mPercentView = percent;
            notifyDataSetChanged();
        }
    }

    private List<TransactionCategories> getExistingCategories() {
        List<TransactionCategories> categories = new ArrayList<>();
        for (AbstractMap.SimpleEntry<TransactionCategories, Float> entry : mBreakdown) {
            categories.add(entry.getKey());
        }
        return categories;
    }


    public void setListener(BreakdownInputAdapterListener listener) {
        mListener = listener;
    }

    public interface BreakdownInputAdapterListener {
        void applyChanges(HashMap<TransactionCategories, Float> breakdown);
    }

    private HashMap<TransactionCategories, Float> getBreakdownAsHash() {
        HashMap<TransactionCategories, Float> map = new HashMap<>();

        for (AbstractMap.SimpleEntry<TransactionCategories, Float> entry : mBreakdown) {
            map.put(entry.getKey(), entry.getValue());
        }

        return map;
    }

}
