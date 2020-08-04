package com.example.budgetassistant.dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.budgetassistant.R;
import com.example.budgetassistant.models.RecurringTransaction;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RecurringPaymentDialog extends AppCompatDialogFragment {

    private RecurringTransaction mTransaction;
    private View view;
    private EditText dateTextInput;
    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view = View.inflate(getContext(), R.layout.dialog_recurring_payment,null);
        builder.setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        Dialog dialog =  builder.create();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        setupDialogView();
        return dialog;
    }

    public void setTransaction(RecurringTransaction recurringTransaction){
        mTransaction = recurringTransaction;
    }

    final DatePickerDialog.OnDateSetListener date =  new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            Calendar calInstance = Calendar.getInstance();
            calInstance.set(Calendar.YEAR,year);
            calInstance.set(Calendar.MONTH,monthOfYear);
            calInstance.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            mTransaction.StartDate = calInstance.getTime();
            saveTransaction();
            updateDateView();
        }
    };

    public void setupDialogView(){
        TextView descripText = view.findViewById(R.id.RecurringPaymentTitleLabel);
        dateTextInput = view.findViewById(R.id.startDateTextInput);
        dateTextInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(mTransaction.StartDate);
                new DatePickerDialog(getContext(), date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        updateDateView();
    }
    private void updateDateView(){
        dateTextInput.setText(sdf.format(mTransaction.StartDate));
    }

    public void saveTransaction(){

    }
}
