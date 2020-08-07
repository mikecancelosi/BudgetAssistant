package com.example.budgetassistant.dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.budgetassistant.R;
import com.example.budgetassistant.TransactionCategories;
import com.example.budgetassistant.models.RecurringTransaction;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecurringPaymentDialog extends AppCompatDialogFragment {

    private RecurringTransaction mTransaction;
    private View view;
    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private RecurringPaymentDialogListener mListener;
    private int mIndex;
    private EditText descriptionText;
    private Spinner dialogFrequency;
    private EditText dialogFrequencyValue;
    private  List<AbstractMap.SimpleEntry<String,Integer>> frequencyKeys;
    private EditText amountText;
    private Spinner dialogCategory;
    private EditText dateTextInput;




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view = View.inflate(getContext(), R.layout.dialog_recurring_payment,null);
        builder.setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            mTransaction.StartDate = sdf.parse(dateTextInput.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        mTransaction.Frequency = getFrequency();
                        mTransaction.Amount = Float.parseFloat(amountText.getText().toString());
                        mTransaction.Category = TransactionCategories.values()[dialogCategory.getSelectedItemPosition()];

                        //Set frequency
                        int spinnerVal =  dialogFrequency.getSelectedItemPosition();
                        int calendarKey = frequencyKeys.get(spinnerVal).getValue();
                        int calendarValue = Integer.parseInt(dialogFrequencyValue.getText().toString());
                        mTransaction.Frequency = new AbstractMap.SimpleEntry<>(calendarKey,calendarValue);

                        mListener.applyChanges(mTransaction);
                    }
                });

        Dialog dialog =  builder.create();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        setupDialogView();
        return dialog;
    }

    public void setTransaction(RecurringTransaction recurringTransaction,int index){
        mTransaction = recurringTransaction;
        mIndex = index;
    }

    final DatePickerDialog.OnDateSetListener date =  new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            Calendar calInstance = Calendar.getInstance();
            calInstance.set(Calendar.YEAR,year);
            calInstance.set(Calendar.MONTH,monthOfYear);
            calInstance.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            dateTextInput.setText(sdf.format(calInstance.getTime()));
        }
    };

    public void setupDialogView(){
        descriptionText = view.findViewById(R.id.RecurringPaymentTitleLabel);
        amountText = view.findViewById(R.id.dialogAmount);
        dialogCategory = view.findViewById(R.id.dialogCategorySpinner);
        dateTextInput = view.findViewById(R.id.startDateTextInput);
        dialogFrequency = view.findViewById(R.id.dialogFrequencySpinner);
        dialogFrequencyValue = view.findViewById(R.id.dialogFrquencyValue);

        descriptionText.setText(mTransaction.Description);

        amountText.setText("" + mTransaction.Amount);
        TransactionCategories[] categories = TransactionCategories.values();
        String[] categoryItems = new String[categories.length];
        for(int i = 0; i<categories.length; i++){
            categoryItems[i] = categories[i].name();
        }
        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item,categoryItems);
        catAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        dialogCategory.setAdapter(catAdapter);
        dialogCategory.setSelection(0);

        dateTextInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(mTransaction.StartDate);
                new DatePickerDialog(getContext(), date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        dateTextInput.setText(sdf.format(mTransaction.StartDate));


        // Initialize the frequency inputs.
        //Setup the keys array in such a way to keep reference to what the string's int value is in Calendar
        frequencyKeys = new ArrayList<AbstractMap.SimpleEntry<String,Integer>>(){};
        frequencyKeys.add(new AbstractMap.SimpleEntry<>("Year",Calendar.YEAR));
        frequencyKeys.add(new AbstractMap.SimpleEntry<>("Month",Calendar.MONTH));
        frequencyKeys.add(new AbstractMap.SimpleEntry<>("Week",Calendar.WEEK_OF_YEAR));
        frequencyKeys.add(new AbstractMap.SimpleEntry<>("Day",Calendar.DATE));
        String[] frequencyItems = new String[frequencyKeys.size()];
        for(int i=0;i<frequencyItems.length;i++){
            frequencyItems[i] = frequencyKeys.get(i).getKey();
        }
        ArrayAdapter<String> freqAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item,frequencyItems);
        freqAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        dialogFrequency.setAdapter(freqAdapter);
        dialogFrequency.setSelection(0);

    }

    private AbstractMap.SimpleEntry<Integer,Integer> getFrequency(){
        return null;
    }

    public void setDialogResult(RecurringPaymentDialogListener listener){
        mListener = listener;
    }

    public interface RecurringPaymentDialogListener{
        void applyChanges(RecurringTransaction transaction);
    }
}
