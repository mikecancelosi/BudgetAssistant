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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetassistant.R;
import com.example.budgetassistant.Enums.TransactionCategories;
import com.example.budgetassistant.models.Transaction;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BreakdownInputAdapter extends RecyclerView.Adapter<BreakdownInputAdapter.MyViewHolder> {

    //Value is in percentages.
    private Float mIncomeAmount;
    private List<AbstractMap.SimpleEntry<TransactionCategories,Float>> mBreakdown;
    private boolean mPercentView;

    public BreakdownInputAdapter(HashMap<TransactionCategories, Float> breakdown, Float incomeAmount) {
        mBreakdown = new ArrayList<>();
        for(TransactionCategories category : breakdown.keySet()){
          mBreakdown.add(new AbstractMap.SimpleEntry<>(category,breakdown.get(category)));
        }
        mIncomeAmount = incomeAmount;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public Spinner categorySpinner;
        public EditText valueInput;
        public TextView unitView;

        private AbstractMap.SimpleEntry<TransactionCategories,Float> mItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            categorySpinner = itemView.findViewById(R.id.breakdownCategoryInputSpinner);
            valueInput = itemView.findViewById(R.id.breakdownCategoryValueInput);
            unitView = itemView.findViewById(R.id.breakdownCategoryUnit);
        }

        public void setItem(AbstractMap.SimpleEntry<TransactionCategories,Float> item){
            mItem = item;


        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_breakdown_category_input,
                                                                     parent,
                                                                     false);
        BreakdownInputAdapter.MyViewHolder viewHolder = new BreakdownInputAdapter.MyViewHolder(view);

        Spinner catSpinner = viewHolder.categorySpinner;
        EditText catValue = viewHolder.valueInput;


        TransactionCategories[] categories = TransactionCategories.values();
        String[] categoryItems = new String[categories.length];
        for (int i = 0; i < categories.length; i++) {
            if (categories[i] != TransactionCategories.INCOME) {
                categoryItems[i] = categories[i].name();
            }
        }
        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(catSpinner.getContext(),
                                                                   R.layout.support_simple_spinner_dropdown_item,
                                                                   categoryItems);
        catAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        catSpinner.setAdapter(catAdapter);

        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TransactionCategories category = TransactionCategories.values()[i];
                if(!doesCategoryExistInBreakdown(category)) {
                    updateItem(i, TransactionCategories.values()[i]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        catValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Float rawValue = Float.parseFloat(charSequence.toString());
                updateItem(i,rawValue);

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //Create spinner
        Spinner catSpinner = holder.categorySpinner;
        EditText catValue = holder.valueInput;
        TextView unitView = holder.unitView;

        AbstractMap.SimpleEntry<TransactionCategories,Float> item = getItem(position);

        catSpinner.setSelection(item.getKey().getValue());

        if(mPercentView){
            String convertedValue = String.format("%.2f", (item.getValue() * 100));
            Log.d(item.getKey() + " | ", convertedValue);
            catValue.setText(convertedValue);
            unitView.setText("%");

        } else{
            Float dollarValue = mIncomeAmount * item.getValue();
            String dollarText = String.format("%.2f", dollarValue);
            Log.d(item.getKey() + " | ", dollarText);
            catValue.setText(dollarText);
            unitView.setText("$");
        }
    }

    private void updateItem(int index, TransactionCategories newCategory){
        AbstractMap.SimpleEntry<TransactionCategories,Float> oldEntry = getItem(index);
        mBreakdown.set(index, new AbstractMap.SimpleEntry<>(newCategory,oldEntry.getValue()));
        mListener.applyChanges(getBreakdownAsHash());

    }

    private void updateItem(int index, Float newValue){
        Float decimalValue;

        if(mPercentView) {
            decimalValue = (newValue / 100f);
        } else{
            decimalValue = newValue / mIncomeAmount;
        }

        Log.d("!!", mBreakdown.get(index).getKey() + " | " + newValue);
        mBreakdown.get(index).setValue(decimalValue);
        mListener.applyChanges(getBreakdownAsHash());
    }

    public void changeView(boolean percent){
        if(mPercentView != percent) {
            mPercentView = percent;
        }
    }

    @Override
    public int getItemCount() {
        return mBreakdown.size();
    }

    public AbstractMap.SimpleEntry<TransactionCategories,Float> getItem(int index){
        return mBreakdown.get(index);
    }

    private boolean doesCategoryExistInBreakdown(TransactionCategories category){
        for(AbstractMap.SimpleEntry<TransactionCategories,Float> entry : mBreakdown){
            if(entry.getKey() == category){
                return true;
            }
        }
        return false;
    }

    private HashMap<TransactionCategories,Float> getBreakdownAsHash(){
        HashMap<TransactionCategories,Float> map = new HashMap<>();

        for(AbstractMap.SimpleEntry<TransactionCategories,Float> entry : mBreakdown){
            map.put(entry.getKey(),entry.getValue());
        }

        return map;
    }


    private BreakdownInputAdapterListener mListener;
    public void setListener(BreakdownInputAdapterListener listener){
        mListener = listener;
    }

    public interface BreakdownInputAdapterListener{
        void applyChanges(HashMap<TransactionCategories, Float> breakdown);
    }

}
