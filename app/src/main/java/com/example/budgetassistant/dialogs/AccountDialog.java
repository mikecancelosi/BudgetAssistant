package com.example.budgetassistant.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;

import com.example.budgetassistant.Bank;
import com.example.budgetassistant.R;
import com.example.budgetassistant.TransactionCategories;
import com.example.budgetassistant.models.Account;
import com.example.budgetassistant.models.RecurringTransaction;

import java.text.ParseException;
import java.util.AbstractMap;

public class AccountDialog extends AppCompatDialogFragment {
   
    private View mView;
    private LinearLayout mCreditCardBlock;
    private LinearLayout mBankBlock;
    private AccountDialogListener mListener;
    private Account mAccount;
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mView = View.inflate(getContext(), R.layout.dialog_account, null);
        builder.setView(mView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.applyChanges(mAccount);
                    }
                });

        Dialog dialog =  builder.create();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setupDialogView();
        return dialog;
    }

    private void setupDialogView(){
        Button bankBtn = mView.findViewById(R.id.BankAccountBtn);
        Button creditCardBtn = mView.findViewById(R.id.CardAccountBtn);
        mCreditCardBlock = mView.findViewById(R.id.CreditCardLayout);
        mBankBlock = mView.findViewById(R.id.BankAccountLayout);

        bankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBlock(true);
            }
        });
        creditCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBlock(false);
            }
        });

        setupBankBlock();
        setupCardBlock();

        showBlock(true);
    }

    private void showBlock(boolean bank){
        mBankBlock.setVisibility(bank ? View.VISIBLE : View.INVISIBLE);
        mCreditCardBlock.setVisibility(bank ? View.INVISIBLE : View.VISIBLE);
    }

    private void setupBankBlock(){
        TableLayout table = mView.findViewById(R.id.BankAccountLayout);

        Bank[] enumVals = Bank.values();
        TableRow row = new TableRow(getContext());
        table.addView(row);
        for(int i =0; i < enumVals.length;i++){
            if(i%2 == 0 && i > 1){
                row = new TableRow(getContext());
                table.addView(row);
            }
            Button btnInstance = new Button(getContext());
            btnInstance.setText(enumVals[i].toString());
            LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                          LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.setMargins(10,10,10,10);
            btnInstance.setLayoutParams(layout);
            btnInstance.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorSurface));
            btnInstance.setPadding(10,10,10,10);
            btnInstance.setTextSize(18);
            btnInstance.setTypeface(Typeface.DEFAULT_BOLD);
            row.addView(btnInstance);
        }
    }

    private void setupCardBlock(){

    }


    public void setDialogResult(AccountDialog.AccountDialogListener listener){
        mListener = listener;
    }

    public interface AccountDialogListener{
        void applyChanges(Account account);
    }
    
}
