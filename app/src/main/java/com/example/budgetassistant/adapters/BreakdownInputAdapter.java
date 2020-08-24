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

import com.example.budgetassistant.Enums.TransactionCategories;
import com.example.budgetassistant.R;

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
        private int mIndex = -1;
        private boolean mPercentView;
        private Float mIncomeAmount;
        private BreakdownViewHolderListener mListener;
        private List<TransactionCategories> mPrexistingCategories;
        private boolean mHasFocus = false;


        public interface BreakdownViewHolderListener {
            void onItemChanged(int index,
                               AbstractMap.SimpleEntry<TransactionCategories, Float> item);

            void onItemChanging(int index,
                                AbstractMap.SimpleEntry<TransactionCategories, Float> item);

            void onItemRemoved(int index);
        }


        public MyViewHolder(View itemView,
                            List<TransactionCategories> prexistingCategories,
                            final BreakdownViewHolderListener listener) {
            super(itemView);
            categorySpinner = itemView.findViewById(R.id.breakdownCategoryInputSpinner);
            categoryValue = itemView.findViewById(R.id.breakdownCategoryValueInput);
            unitView = itemView.findViewById(R.id.breakdownCategoryUnit);
            mListener = listener;
            mPrexistingCategories = prexistingCategories;


            TransactionCategories[] categories = TransactionCategories.values();
            final List<String> categoryItems = new ArrayList<>();
            for (TransactionCategories category : categories) {
                if (category != TransactionCategories.INCOME) {
                    categoryItems.add(category.toString());
                }
            }

            final ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(categorySpinner.getContext(),
                                                                             R.layout.support_simple_spinner_dropdown_item,
                                                                             categoryItems) {
                @Override
                public View getDropDownView(int position,
                                            @Nullable View convertView,
                                            @NonNull ViewGroup parent) {
                    View mView = super.getDropDownView(position, convertView, parent);
                    TextView mTextView = (TextView) mView;
                    String valueAtPosition = categoryItems.get(position);
                    TransactionCategories category = TransactionCategories.getCategoryFromFriendlyName(
                            valueAtPosition);
                    boolean enabled = !mPrexistingCategories.contains(category);
                    mTextView.setTextColor(enabled ? Color.BLACK : Color.GRAY);
                    return mView;
                }

                @Override
                public boolean isEnabled(int position) {
                    String valueAtPosition = categoryItems.get(position);
                    TransactionCategories category = TransactionCategories.getCategoryFromFriendlyName(
                            valueAtPosition);
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

                    mListener.onItemChanged(mIndex, mItem);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            categoryValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    categoryValue.requestFocus();
                }
            });

            categoryValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if(mHasFocus != hasFocus) {
                        mHasFocus = hasFocus;
                        if (!mHasFocus) {
                            String valueString = categoryValue.getText().toString();

                            Float decimalValue = decodeValue(valueString,
                                                             mPercentView,
                                                             mIncomeAmount);
                            if (decimalValue != null) {
                                if (!mItem.getValue().equals(decimalValue)) {
                                    mItem.setValue(decimalValue);
                                    mListener.onItemChanged(mIndex, mItem);
                                }
                            } else {
                                mListener.onItemRemoved(mIndex);
                            }
                        }
                    }
                }
            });

            categoryValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String valueString = categoryValue.getText().toString();

                    Float decimalValue = decodeValue(valueString,
                                                     mPercentView,
                                                     mIncomeAmount);
                    if (decimalValue != null) {
                        mListener.onItemChanging(mIndex, new AbstractMap.SimpleEntry<>(mItem.getKey(),decimalValue));

                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

        public void setItem(int index, AbstractMap.SimpleEntry<TransactionCategories, Float> item,
                            List<TransactionCategories> prexistingCategories,
                            boolean percentView,
                            Float incomeAmount) {
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
        }

        private static Float decodeValue(String input, boolean percentView, Float incomeAmount) {
            if (!input.isEmpty()) {
                float parsedInput = Float.parseFloat(input);
                if (parsedInput > 0f) {
                    if (percentView) {
                        return parsedInput / 100;

                    } else {
                        return parsedInput / incomeAmount;

                    }
                }
            }

            return null;
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
                updateItem(index, item, true);
            }

            @Override
            public void onItemChanging(int index,
                                       AbstractMap.SimpleEntry<TransactionCategories, Float> item) {
                updateItem(index, item, false);

            }

            @Override
            public void onItemRemoved(int index) {
                removeItem(index);
            }
        };

        return new MyViewHolder(view, mExistingCategories,
                                listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(holder.mIndex == -1 || holder.mIndex != position) {
            AbstractMap.SimpleEntry<TransactionCategories,Float> item = mBreakdown.get(position);
            holder.setItem(position,
                           new AbstractMap.SimpleEntry<>(item.getKey(),item.getValue()),
                           mExistingCategories,
                           mPercentView,
                           mIncomeAmount);
        }
    }

    @Override
    public int getItemCount() {
        return mBreakdown.size();
    }

    private void updateItem(int index,
                            AbstractMap.SimpleEntry<TransactionCategories, Float> item,
                            boolean notify) {

        AbstractMap.SimpleEntry<TransactionCategories, Float> itemBefore = mBreakdown.get(index);
        if (itemBefore.getKey() != item.getKey() ||
            !itemBefore.getValue().equals(item.getValue())) {
            mBreakdown.set(index, item);
            mExistingCategories = getExistingCategories();
            mListener.applyChanges(getBreakdownAsHash());
            if (notify) {
                notifyDataSetChanged();
            }
        }
    }

    private void removeItem(int index){
        notifyItemRemoved(index); //TODO: breaks when you scroll out of focus and remove
        mBreakdown.remove(index);
        mListener.applyChanges(getBreakdownAsHash());
    }

    public void changeView(boolean percent) {
        if (mPercentView != percent) {
            mPercentView = percent;
        }
    }

    private List<TransactionCategories> getExistingCategories() {
        List<TransactionCategories> categories = new ArrayList<>();
        for (AbstractMap.SimpleEntry<TransactionCategories, Float> entry : mBreakdown) {
            categories.add(entry.getKey());
        }
        return categories;
    }

    public boolean addCategory() {
        TransactionCategories[] values = TransactionCategories.values();
        TransactionCategories category = null;
        for (TransactionCategories cat : values) {
            if (cat != TransactionCategories.INCOME &&
                !mExistingCategories.contains(cat)) {
                category = cat;
                break;
            }
        }
        if (category != null) {
            mBreakdown.add(new AbstractMap.SimpleEntry<>(category, .01f));
            mExistingCategories = getExistingCategories();
            mListener.applyChanges(getBreakdownAsHash()); //TODO: Make more efficient then updating whole breakdown
            notifyDataSetChanged();
        }

        return (mExistingCategories.size() == values.length - 1);
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
