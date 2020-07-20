package com.example.budgetassistant;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.budgetassistant.adapters.AlertAdapter;
import com.example.budgetassistant.adapters.TransactionHistoryAdapter;
import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.viewmodels.AlertViewModel;
import com.example.budgetassistant.viewmodels.BankAccountViewModel;
import com.example.budgetassistant.viewmodels.TransactionHistoryViewModel;
import com.example.budgetassistant.viewmodels.UserSettingsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private AlertViewModel mAlertViewModel;
    private AlertAdapter mAlertAdapter;
    private ListView mListView;
    private UserSettingsViewModel mUserSettings;
    private BankAccountViewModel mBankAccount;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        setUpHeader(view);
        setUpList(view);

        return view;
    }

    private void setUpHeader(View view){
        //Set account balance
        mBankAccount = new BankAccountViewModel();
        mBankAccount.init();
        TextView AccountBalance = (TextView) view.findViewById(R.id.BankAccountBalance);
        BankAccount account = mBankAccount.getAccount().getValue();
        AccountBalance.setText("$" + account.Balance);

        //Set user name and picture
        mUserSettings =  new UserSettingsViewModel();

        TextView nameText = (TextView) view.findViewById(R.id.UserName);
        UserSettings settings = mUserSettings.getSettings().getValue();
        nameText.setText(settings.getName());

    }

    private void setUpList(View view){
        Resources res = getResources();
        ListView myListView = (ListView) view.findViewById(R.id.BillList);


        AlertAdapter alertAdapter = new AlertAdapter(view.getContext());
        myListView.setAdapter(alertAdapter);
    }
}