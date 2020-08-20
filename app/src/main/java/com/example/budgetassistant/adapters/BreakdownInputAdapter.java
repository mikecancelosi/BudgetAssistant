package com.example.budgetassistant.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetassistant.R;
import com.example.budgetassistant.TransactionCategories;

import java.util.AbstractMap;
import java.util.List;

public class BreakdownInputAdapter extends RecyclerView.Adapter<BreakdownInputAdapter.MyViewHolder> {

    //Value is in percentages.
    private List<AbstractMap.SimpleEntry<TransactionCategories,Float>> breakdown;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public Spinner categorySpinner;
        public EditText valueInput;

        public MyViewHolder(View itemView, Spinner spinner, EditText valueText) {
            super(itemView);
            categorySpinner = spinner;
            valueInput = valueText;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Create parent layout
        LinearLayout parentLayout = new LinearLayout(parent.getContext());
        LinearLayout.LayoutParams parentLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                                                LinearLayout.LayoutParams.WRAP_CONTENT);
        parentLayout.setLayoutParams(parentLayoutParams);
        parent.addView(parentLayout);

        //Create spinner
        Spinner catSpinner = new Spinner(parent.getContext());
        LinearLayout.LayoutParams spinnerLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                                                     LinearLayout.LayoutParams.WRAP_CONTENT);
        catSpinner.setLayoutParams(spinnerLayoutParams);
        TransactionCategories[] categories = TransactionCategories.values();
        String[] categoryItems = new String[categories.length];
        for(int i = 0; i<categories.length; i++){
            categoryItems[i] = categories[i].name();
        }
        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(parent.getContext(), R.layout.support_simple_spinner_dropdown_item, categoryItems);
        catAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        catSpinner.setAdapter(catAdapter);
        catSpinner.setSelection(0);
        parentLayout.addView(catSpinner);

        //Create Value input
        EditText valueText = new EditText(parent.getContext());
        LinearLayout.LayoutParams valueLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                                                     LinearLayout.LayoutParams.WRAP_CONTENT);
        valueText.setLayoutParams(valueLayoutParams);

        parentLayout.addView(valueText);


        BreakdownInputAdapter.MyViewHolder viewHolder = new BreakdownInputAdapter.MyViewHolder(parentLayout,catSpinner,valueText);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return breakdown.size();
    }


}
