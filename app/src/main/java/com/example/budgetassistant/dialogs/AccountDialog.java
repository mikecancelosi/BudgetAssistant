package com.example.budgetassistant.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;

import com.example.budgetassistant.Bank;
import com.example.budgetassistant.CardCarrier;
import com.example.budgetassistant.R;
import com.example.budgetassistant.models.Account;
import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.CreditCardAccount;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class AccountDialog extends AppCompatDialogFragment {

    private View mView;
    private ScrollView mCreditCardBlock;
    private ScrollView mBankBlock;
    private AccountDialogListener mListener;

    private int selectedColor;
    private int surfaceColor;
    private int onSurfaceColor;
    private int errorColor;
    private int onErrorColor;
    private boolean bankSelected;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mView = View.inflate(getContext(), R.layout.dialog_account, null);
        final Dialog dialog = builder.setView(mView)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button posBtn = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                posBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText accountDisplayNameInput = mView.findViewById(R.id.AccountDisplayNameInput);
                        String displayName = accountDisplayNameInput.getText().toString();

                        if (bankSelected) {
                            EditText routingNumberInput = mView.findViewById(R.id.BankRoutingNumberInput);
                            EditText accountNumberInput = mView.findViewById(R.id.BankAccountNumberInput);

                            Bank bank = null;
                            int routingNumber = -1;
                            int accountNumber = -1;

                            if(currentlySelectedBankBtn != null) {
                                String bankDisplay = currentlySelectedBankBtn.getText().toString();

                                Bank[] bankVals = Bank.values();
                                for (int j = 0; j < bankVals.length; j++) {
                                    if (bankVals[j].toString() == bankDisplay) {
                                        bank = bankVals[j];
                                        break;
                                    }
                                }
                            }

                            String routingInputVal = routingNumberInput.getText().toString();
                            if(!routingInputVal.equals("")) {
                                routingNumber = Integer.parseInt(routingNumberInput.getText().toString());
                            }
                            String accountInputVal = accountNumberInput.getText().toString();
                            if(!accountInputVal.equals("")) {
                                accountNumber = Integer.parseInt(accountInputVal);
                            }
                            //Check for errors
                            if (accountNumber != -1 &&
                                !displayName.equals("") &&
                                bank != null &&
                                routingNumber != -1) {

                                BankAccount account = new BankAccount(accountNumber,
                                                                      displayName,
                                                                      bank,
                                                                      routingNumber);
                                mListener.applyChanges(account);
                                dialog.dismiss();
                            } else{
                                showBankInputErrors();
                                dialog.show();
                            }

                        } else {
                            EditText cardNumberInput = mView.findViewById(R.id.cardNumberInput);
                            EditText monthExpInput = mView.findViewById(R.id.CCExpirationMonth);
                            EditText yearExpInput = mView.findViewById(R.id.CCExpirationYear);
                            EditText csvInput = mView.findViewById(R.id.CCCSVInput);

                            Integer cardNumber = -1;
                            Integer monthExp = -1;
                            Integer yearExp = -1;
                            Integer csv =-1;
                            CardCarrier card = null;

                            String cardNumberVal =cardNumberInput.getText().toString();
                            if(!cardNumberVal.equals("")){
                                cardNumber = Integer.parseInt(cardNumberVal);
                            }

                            String monthExpVal = monthExpInput.getText().toString();
                            if(!monthExpVal.equals("")){
                                monthExp = Integer.parseInt(monthExpVal);
                            }

                            String yearExpVal = yearExpInput.getText().toString();
                            if(!yearExpVal.equals("")){
                                yearExp = Integer.parseInt(yearExpVal);
                            }

                            AbstractMap.SimpleEntry<Integer, Integer> expiration = new AbstractMap.SimpleEntry<>(
                                    monthExp,
                                    yearExp);

                            String csvVal = csvInput.getText().toString();
                            if(!csvVal.equals("")) {
                                csv = Integer.parseInt(csvVal);
                            }

                            if(currentlySelectedCardBtn != null) {
                                String cardDisplay = currentlySelectedCardBtn.getText().toString();
                                CardCarrier[] cardVals = CardCarrier.values();
                                for (CardCarrier cardVal : cardVals) {
                                    if (cardVal.toString() == cardDisplay) {
                                        card = cardVal;
                                        break;
                                    }
                                }
                            }

                            if (cardNumber != -1 &&
                                csv != -1 &&
                                card != null &&
                                monthExp != -1 &&
                                yearExp != -1 &&
                                !displayName.equals("")) {
                                CreditCardAccount account = new CreditCardAccount(cardNumber,
                                                                                  csv,
                                                                                  card,
                                                                                  expiration,
                                                                                  displayName);
                                mListener.applyChanges(account);
                                dialog.dismiss();
                            } else{
                                showCardInputErrors();
                            }
                        }


                    }
                });
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                                     ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        setupDialogView();
        return dialog;
    }

    private void setupDialogView() {
        selectedColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        surfaceColor = ContextCompat.getColor(getContext(), R.color.colorSurface);
        onSurfaceColor = ContextCompat.getColor(getContext(), R.color.colorOnSurface);
        errorColor = ContextCompat.getColor(getContext(),R.color.colorError);
        onErrorColor = ContextCompat.getColor(getContext(),R.color.colorOnError);

        Button bankBtn = mView.findViewById(R.id.BankAccountBtn);
        Button creditCardBtn = mView.findViewById(R.id.CardAccountBtn);
        mCreditCardBlock = mView.findViewById(R.id.CreditCardLayout);
        mBankBlock = mView.findViewById(R.id.BankAccountLayout);
        TableLayout bankTable = mView.findViewById(R.id.BankOptionsTable);
        TableLayout cardTable = mView.findViewById(R.id.CardOptionsTable);

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

        List<Button> bankBtns = setupBlock(bankTable, Bank.values());
        for (int i = 0; i < bankBtns.size(); i++) {
            Button btn = bankBtns.get(i);
            final int finalI = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBankSelected(finalI);
                }
            });
        }

        List<Button> cardBtns = setupBlock(cardTable, CardCarrier.values());
        for (int i = 0; i < cardBtns.size(); i++) {
            Button btn = cardBtns.get(i);
            final int finalI = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCardSelected(finalI);
                }
            });
        }

        showBlock(true);
    }

    private void showBlock(boolean bank) {

        Button bankBtn = mView.findViewById(R.id.BankAccountBtn);
        Button creditCardBtn = mView.findViewById(R.id.CardAccountBtn);
        mBankBlock.setVisibility(bank ? View.VISIBLE : View.INVISIBLE);
        mCreditCardBlock.setVisibility(bank ? View.INVISIBLE : View.VISIBLE);

        bankBtn.setBackgroundColor(bank ? selectedColor : surfaceColor);
        creditCardBtn.setBackgroundColor(bank ? surfaceColor : selectedColor);

        bankSelected = bank;
    }

    private List<Button> setupBlock(TableLayout table, Enum[] enumVals) {

        List<Button> buttons = new ArrayList<>();

        //Create rows
        int rowCount = ((int) Math.ceil(enumVals.length / 2f));
        for (int i = 0; i <= rowCount; i++) {
            TableRow row = new TableRow(getContext());
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                                                     TableRow.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            row.setLayoutParams(params);
            table.addView(row);
        }

        //Create buttons
        for (int i = 0; i < enumVals.length; i++) {
            TableRow row = (TableRow) table.getChildAt(i / 2);

            Button btnInstance = new Button(getContext());
            btnInstance.setText(enumVals[i].toString());
            TableRow.LayoutParams layout = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                                                     TableRow.LayoutParams.WRAP_CONTENT);
            layout.setMargins(20, 20, 20, 20);
            btnInstance.setLayoutParams(layout);
            btnInstance.setBackgroundColor(ContextCompat.getColor(getContext(),
                                                                  R.color.colorSurface));
            btnInstance.setPadding(20, 20, 20, 20);
            btnInstance.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            btnInstance.setTypeface(Typeface.DEFAULT_BOLD);
            buttons.add(btnInstance);

            row.addView(btnInstance);
        }
        return buttons;
    }

    //region BankBlock

    private Button currentlySelectedBankBtn;

    private void onBankSelected(int index) {

        if (currentlySelectedBankBtn != null) {
            currentlySelectedBankBtn.setBackgroundColor(surfaceColor);
        }

        TableLayout table = mView.findViewById(R.id.BankOptionsTable);
        TableRow row = (TableRow) table.getChildAt(index / 2);
        currentlySelectedBankBtn = (Button) row.getChildAt(index % 2);
        currentlySelectedBankBtn.setBackgroundColor(selectedColor);
    }

    private void showBankInputErrors() {
        TextView bankHeader = mView.findViewById(R.id.BankHeader);
        EditText routingInput = mView.findViewById(R.id.BankRoutingNumberInput);
        EditText accountInput = mView.findViewById(R.id.BankAccountNumberInput);

        if(currentlySelectedBankBtn == null){
            bankHeader.setBackgroundColor(errorColor);
        } else{
            bankHeader.setBackgroundColor(surfaceColor);
        }

        displayEditTextEmptyError(routingInput);
        displayEditTextEmptyError(accountInput);
    }

    //endregion

    //region CreditCardBlock

    private Button currentlySelectedCardBtn;

    private void onCardSelected(int index) {
        if (currentlySelectedCardBtn != null) {
            currentlySelectedCardBtn.setBackgroundColor(surfaceColor);
        }

        TableLayout table = mView.findViewById(R.id.CardOptionsTable);
        TableRow row = (TableRow) table.getChildAt(index / 2);
        currentlySelectedCardBtn = (Button) row.getChildAt(index % 2);
        currentlySelectedCardBtn.setBackgroundColor(selectedColor);
    }

    private void showCardInputErrors(){
        TextView cardHeader = mView.findViewById(R.id.CardHeader);
        EditText cardNumberInput = mView.findViewById(R.id.cardNumberInput);
        EditText expirMonthInput = mView.findViewById(R.id.CCExpirationMonth);
        EditText expirYearInput = mView.findViewById(R.id.CCExpirationYear);
        EditText csvInput = mView.findViewById(R.id.CCCSVInput);

        if(currentlySelectedCardBtn == null){
            cardHeader.setBackgroundColor(errorColor);
        } else{
            cardHeader.setBackgroundColor(surfaceColor);
        }

        displayEditTextEmptyError(cardNumberInput);
        displayEditTextEmptyError(expirMonthInput);
        displayEditTextEmptyError(expirYearInput);
        displayEditTextEmptyError(csvInput);
    }

    //endregion

    private void displayEditTextEmptyError(EditText input){
        if(input.getText().toString().equals("")){
            input.setBackgroundColor(errorColor);
            input.setTextColor(onErrorColor);
        } else{
            input.setBackgroundColor(surfaceColor);
            input.setTextColor(onSurfaceColor);
        }
    }

    public void setDialogResult(AccountDialog.AccountDialogListener listener) {
        mListener = listener;
    }

    public interface AccountDialogListener {
        void applyChanges(Account account);
    }

}
