package com.example.budgetassistant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.budgetassistant.adapters.AlertAdapter;
import com.example.budgetassistant.adapters.BankAccountAdapter;
import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.repositories.BankRepository;
import com.example.budgetassistant.viewmodels.SettingsViewModel;
import com.example.budgetassistant.viewmodels.StatsViewModel;

import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private SettingsViewModel mViewModel;
    private View view;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        mViewModel.init();
        mViewModel.getSettings().observe(getViewLifecycleOwner(), new Observer<UserSettings>() {
            @Override
            public void onChanged(UserSettings settings) {
                setupUI();
            }
        });


        setupUI();

        return view;
    }

    private void setupUI(){
        UserSettings settings = mViewModel.getSettings().getValue();

        setupHeader(settings);
        setupAccountsList(settings);
        setupRecurringPayments(settings);
        setupIncome(settings);
        setupBreakdown(settings);

    }

    private void setupHeader(UserSettings settings){
        ImageView userProfilePic = view.findViewById(R.id.UserProfilePicture);
        TextView userHeading = view.findViewById(R.id.UsernameDisplay);
        TextView joinLabel = view.findViewById(R.id.memberSinceLabel);

        userProfilePic.setImageResource(settings.profilePicture);
        userHeading.setText(settings.name);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        joinLabel.setText("Member since " + sdf.format(settings.joinDate));
    }

    private void setupAccountsList(UserSettings settings){
        RecyclerView accountListView = view.findViewById(R.id.AccountListView);
        accountListView.setHasFixedSize(false);
        accountListView.setLayoutManager(new LinearLayoutManager(getContext()));

        BankAccountAdapter bankAdapter = new BankAccountAdapter(settings.accounts);
        accountListView.setAdapter(bankAdapter);
    }

    private void setupIncome(UserSettings settings){
        TextView paycheckAmount = view.findViewById(R.id.IncomeDollarAmount);
        TextView frequencyText = view.findViewById(R.id.IncomeFrequency);
        TextView nextPayText = view.findViewById(R.id.IncomeNextPaycheck);

        paycheckAmount.setText("$" + settings.income.Amount);
        frequencyText.setText("Every " + settings.income.PayPeriodInDays + " Days");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        nextPayText.setText("Next check on " + sdf.format(settings.income.GetNextPaycheckDate()));
    }

    private void setupRecurringPayments(UserSettings settings){
        RecyclerView recurList = view.findViewById(R.id.RecurPaymentList);
        recurList.setHasFixedSize(true);
        recurList.setLayoutManager(new LinearLayoutManager(getContext()));
        AlertAdapter alertAdapter = new AlertAdapter( settings.recurringTransactions);
        recurList.setAdapter(alertAdapter);
    }

    private void setupBreakdown(UserSettings settings){

    }
}