package com.example.budgetassistant.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.budgetassistant.CalendarHelper;
import com.example.budgetassistant.R;
import com.example.budgetassistant.models.Income;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class IncomeDialog extends AppCompatDialogFragment {

    private View mView;
    private IncomeDialogListener mListener;
    private List<AbstractMap.SimpleEntry<String,Integer>> frequencyKeys;
    private Income mIncome;
    private Calendar selectedStartDateCalendar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mView = View.inflate(getContext(), R.layout.dialog_income, null);
        final AlertDialog dialog = builder.setView(mView)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button posBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                posBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText incomeAmountInput = mView.findViewById(R.id.IncomeAmountInput);
                        EditText incomefreqValue = mView.findViewById(R.id.IncomeFreqValue);
                        Spinner incomeFreqKey = mView.findViewById(R.id.IncomeFreqKey);



                        float amount = Float.parseFloat(incomeAmountInput.getText().toString());
                        int freqValue = Integer.parseInt(incomefreqValue.getText().toString());
                        String selectedString = incomeFreqKey.getSelectedItem().toString();
                        int freqKey = CalendarHelper.calendarValueFromDisplay(selectedString);
                        Income income = new Income(amount,
                                                   new AbstractMap.SimpleEntry<>(freqKey,
                                                                                 freqValue),
                                                   selectedStartDateCalendar.getTime());
                        mListener.applyChanges(income);
                        dialog.dismiss();
                    }
                });
            }
        });

        initDialog();
        if(mIncome != null){
            updateVisuals();
        }

        return dialog;
    }

    private void initDialog(){
        selectedStartDateCalendar = Calendar.getInstance();
        Spinner incomeFreqKey = mView.findViewById(R.id.IncomeFreqKey);
        CalendarView startDateCal = mView.findViewById(R.id.startDateCal);
        frequencyKeys = new ArrayList<AbstractMap.SimpleEntry<String,Integer>>(){};
        frequencyKeys.add(new AbstractMap.SimpleEntry<>("Year", Calendar.YEAR));
        frequencyKeys.add(new AbstractMap.SimpleEntry<>("Month",Calendar.MONTH));
        frequencyKeys.add(new AbstractMap.SimpleEntry<>("Week",Calendar.WEEK_OF_YEAR));
        frequencyKeys.add(new AbstractMap.SimpleEntry<>("Day",Calendar.DATE));
        String[] frequencyItems = new String[frequencyKeys.size()];
        for(int i=0;i<frequencyItems.length;i++){
            frequencyItems[i] = frequencyKeys.get(i).getKey();
        }
        ArrayAdapter<String> freqAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, frequencyItems);
        freqAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        incomeFreqKey.setAdapter(freqAdapter);
        incomeFreqKey.setSelection(0);

        startDateCal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView,
                                            int i,
                                            int i1,
                                            int i2) {
                selectedStartDateCalendar.set(i,i1,i2);
            }
        });
    }

    public void setIncome(Income income){
       mIncome = income;
    }

    private void updateVisuals(){
        EditText incomeAmountInput = mView.findViewById(R.id.IncomeAmountInput);
        EditText incomefreqValue = mView.findViewById(R.id.IncomeFreqValue);
        Spinner incomeFreqKey = mView.findViewById(R.id.IncomeFreqKey);
        CalendarView startDateCal = mView.findViewById(R.id.startDateCal);

        incomeAmountInput.setText(mIncome.Amount + "");
        AbstractMap.SimpleEntry<Integer,Integer> period = mIncome.Period;
        incomefreqValue.setText(period.getValue()+"");

        for(int i = 0; i< frequencyKeys.size();i++){
            if(frequencyKeys.get(i).getValue() == mIncome.Period.getKey()){
                incomeFreqKey.setSelection(i);
            }
        }

        startDateCal.setDate(mIncome.FirstPaycheck.getTime());
    }

    public void setDialogResult(IncomeDialog.IncomeDialogListener listener) {
        mListener = listener;
    }

    public interface IncomeDialogListener {
        void applyChanges(Income income);
    }

}
