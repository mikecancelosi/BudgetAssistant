package com.example.budgetassistant.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;

import com.example.budgetassistant.Bank;
import com.example.budgetassistant.R;
import com.example.budgetassistant.TransactionCategories;
import com.example.budgetassistant.models.Account;
import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.CreditCardAccount;
import com.example.budgetassistant.models.RecurringTransaction;

import java.text.ParseException;
import java.util.AbstractMap;

public class AccountDialog extends AppCompatDialogFragment {
   
    private View mView;
    private LinearLayout mCreditCardBlock;
    private LinearLayout mBankBlock;
    private AccountDialogListener mListener;

    private int selectedColor;
    private int unselectedColor;
    private boolean bankSelected;
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mView = View.inflate(getContext(), R.layout.dialog_account, null);
        builder.setView(mView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Account account;
                        EditText accountDisplayNameInput = mView.findViewById(R.id.AccountDisplayNameInput);
                        String displayName = accountDisplayNameInput.getText().toString();

                        if(bankSelected){
                            EditText routingNumberInput = mView.findViewById(R.id.BankRoutingNumberInput);
                            EditText accountNumberInput = mView.findViewById(R.id.BankAccountNumberInput);

                            String bankDisplay = currentlySelectedBankBtn.getText().toString();
                            Bank bank = Bank.CHASE;
                            Bank[] bankVals = Bank.values();
                            for(int j = 0 ; j<bankVals.length;j++){
                                if(bankVals[j].toString() == bankDisplay){
                                    bank = bankVals[j];
                                    break;
                                }
                            }
                            int routingNumber = Integer.parseInt(routingNumberInput.getText().toString());
                            int accountNumber = Integer.parseInt(accountNumberInput.getText().toString());
                            account = new BankAccount(accountNumber,displayName,bank,routingNumber);
                        }else{
                            account = new CreditCardAccount();
                        }

                        mListener.applyChanges(account);
                    }
                });

        Dialog dialog =  builder.create();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setupDialogView();
        return dialog;
    }

    private void setupDialogView(){
        selectedColor = ContextCompat.getColor(getContext(),R.color.colorPrimary);
        unselectedColor = ContextCompat.getColor(getContext(),R.color.colorSurface);

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

        Button bankBtn = mView.findViewById(R.id.BankAccountBtn);
        Button creditCardBtn = mView.findViewById(R.id.CardAccountBtn);
        mBankBlock.setVisibility(bank ? View.VISIBLE : View.INVISIBLE);
        mCreditCardBlock.setVisibility(bank ? View.INVISIBLE : View.VISIBLE);

        bankBtn.setBackgroundColor(bank ? selectedColor :unselectedColor);
        creditCardBtn.setBackgroundColor(bank ? unselectedColor :selectedColor);

        bankSelected = bank;
    }


    //region BankBlock

    private void setupBankBlock(){
        TableLayout table = mView.findViewById(R.id.BankOptionsTable);

        Bank[] enumVals = Bank.values();

        //Create rows
        int rowCount = ((int)Math.ceil(enumVals.length/2f));
        for(int i =0;i<= rowCount;i++){
            TableRow row = new TableRow(getContext());
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                                                     TableRow.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            row.setLayoutParams(params);
            table.addView(row);
        }

        //Create buttons
        for(int i =0; i < enumVals.length;i++){
            TableRow row =(TableRow) table.getChildAt(i/2);

            Button btnInstance = new Button(getContext());
            btnInstance.setText(enumVals[i].toString());
            TableRow.LayoutParams layout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                                                             TableRow.LayoutParams.WRAP_CONTENT);
            layout.setMargins(20,20,20,20);
            btnInstance.setLayoutParams(layout);
            btnInstance.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorSurface));
            btnInstance.setPadding(20,20,20,20);
            btnInstance.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
            btnInstance.setTypeface(Typeface.DEFAULT_BOLD);

            final int finalI = i;
            btnInstance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBankSelected(finalI);
                }
            });

            row.addView(btnInstance);
        }
    }

    private Button currentlySelectedBankBtn;

    private void onBankSelected(int index){


        if(currentlySelectedBankBtn != null){
            currentlySelectedBankBtn.setBackgroundColor(unselectedColor);
        }

        TableLayout table = mView.findViewById(R.id.BankOptionsTable);
        TableRow row = (TableRow) table.getChildAt(index /2);
        currentlySelectedBankBtn = (Button) row.getChildAt(index%2);
        currentlySelectedBankBtn.setBackgroundColor(selectedColor);
    }

    //endregion


    //region CreditCardBlock
    private void setupCardBlock(){

    }

    //endregion


    public void setDialogResult(AccountDialog.AccountDialogListener listener){
        mListener = listener;
    }

    public interface AccountDialogListener{
        void applyChanges(Account account);
    }
    
}
