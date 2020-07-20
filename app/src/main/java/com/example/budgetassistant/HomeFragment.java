package com.example.budgetassistant;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.budgetassistant.adapters.AlertAdapter;
import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.viewmodels.BankAccountViewModel;
import com.example.budgetassistant.viewmodels.UserSettingsViewModel;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private ListView mListView;


    private View view;
    private UserSettingsViewModel mSettingsViewModel;
    private UserSettings mUserSettings;
    private BankAccountViewModel mAccountViewModel;
    private BankAccount mAccount;

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
        view = inflater.inflate(R.layout.fragment_home, container, false);

        mSettingsViewModel = new ViewModelProvider(this).get(UserSettingsViewModel.class);
        mSettingsViewModel.init();
        mUserSettings = mSettingsViewModel.getSettings().getValue();
        mSettingsViewModel.getSettings().observe(getViewLifecycleOwner(), new Observer<UserSettings>() {
            @Override
            public void onChanged(UserSettings settings) {
                mUserSettings = settings;
                setUpHeader();
                setUpList();
            }
        });

        mAccountViewModel = new ViewModelProvider(this).get(BankAccountViewModel.class);
        mAccountViewModel.init();
        mAccount = mAccountViewModel.getAccount().getValue();
        mAccountViewModel.getAccount().observe(getViewLifecycleOwner(), new Observer<BankAccount>() {
            @Override
            public void onChanged(BankAccount account) {
                mAccount = account;
                setUpHeader();
                setUpList();
            }
        });

        setUpHeader();
        setUpList();

        return view;
    }

    private void setUpHeader(){
        //Set account balance
        TextView AccountBalance = (TextView) view.findViewById(R.id.BankAccountBalance);
        Log.d("?","?" + (mAccount ==null));
        AccountBalance.setText("$" + mAccount.Balance);

        //Set user name and picture
        TextView nameText = (TextView) view.findViewById(R.id.UserName);
        nameText.setText(mUserSettings.getName());
    }

    private void setUpList(){
        Resources res = getResources();
        ListView myListView = (ListView) view.findViewById(R.id.BillList);

        AlertAdapter alertAdapter = new AlertAdapter(view.getContext());
        for(Transaction transaction : mUserSettings.getRecurringTransactions()){
            Log.d("!","" + transaction.GetDaysLeft());
            if(transaction.GetDaysLeft() <= mUserSettings.GetIncome().GetNumberOfDaysToNextPaycheck()) {
                alertAdapter.addItem(transaction);
            }
        }

        myListView.setAdapter(alertAdapter);
    }
}