package com.example.budgetassistant.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.budgetassistant.R;
import com.example.budgetassistant.Enums.TransactionCategories;
import com.example.budgetassistant.models.Transaction;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TransactionDialog extends AppCompatDialogFragment {

    private View view;
    private TransactionDialogListener mListener;
    private Transaction mTransaction;
    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private final DecimalFormat df = new DecimalFormat("0.00");

    TextView mHeader;
    EditText mDescription;
    EditText mAmount;
    Spinner mCatSpinner;
    EditText mDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view = View.inflate(getContext(), R.layout.dialog_transaction, null);
        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mTransaction.Description = mDescription.getText().toString();
                        mTransaction.Amount = Float.parseFloat(mAmount.getText().toString());
                        mTransaction.Category =  TransactionCategories.values()[mCatSpinner.getSelectedItemPosition()];
                        try {
                            mTransaction.DateOfTransaction = sdf.parse(mDate.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        mListener.applyChanges(mTransaction);
                    }
                });

        Dialog dialog =  builder.create();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(mTransaction == null) {
            mTransaction = new Transaction();
        }
        setupDialogView();
        return dialog;
    }

    public void setTransaction(Transaction transaction){
        mTransaction = transaction;
    }

    private void setupDialogView(){
        mHeader = view.findViewById(R.id.transactionDialogHeader);
        mDescription = view.findViewById(R.id.TransactionDialogDescription);
        mAmount = view.findViewById(R.id.transactionDialogAmount);
        mCatSpinner = view.findViewById(R.id.TransactionDialogCategory);
        mDate = view.findViewById(R.id.transactionDialogDate);

        mDescription.setText(mTransaction.Description);
        mAmount.setText(df.format(mTransaction.Amount));
        mDate.setText(sdf.format(mTransaction.DateOfTransaction));

        mCatSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, TransactionCategories.values()));
        int catIndex = mTransaction.Category.getValue();
        mCatSpinner.setSelection(catIndex);
    }

    public void setDialogResult(TransactionDialog.TransactionDialogListener listener){
        mListener = listener;
    }

    public interface TransactionDialogListener{
        void applyChanges(Transaction transaction);
    }
}
