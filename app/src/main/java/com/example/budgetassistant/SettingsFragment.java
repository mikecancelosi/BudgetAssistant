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
import android.widget.TextView;

import com.example.budgetassistant.adapters.RecurringPaymentAdapter;
import com.example.budgetassistant.adapters.BankAccountAdapter;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.viewmodels.SettingsViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        accountListView.setLayoutManager(new LinearLayoutManager(getContext()));

        BankAccountAdapter bankAdapter = new BankAccountAdapter(settings.accounts);
        accountListView.setAdapter(bankAdapter);
    }

    private void setupIncome(UserSettings settings){
        TextView paycheckAmount = view.findViewById(R.id.IncomeDollarAmount);
        TextView frequencyText = view.findViewById(R.id.IncomeFrequency);
        TextView nextPayText = view.findViewById(R.id.IncomeNextPaycheck);

        paycheckAmount.setText("$" + settings.income.Amount);
        frequencyText.setText("Every " + settings.income.Period.getValue() + settings.income.Period.getKey());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        nextPayText.setText("Next check on " + sdf.format(settings.income.GetNextPaycheckDate()));
    }

    private void setupRecurringPayments(UserSettings settings){
        RecyclerView recurList = view.findViewById(R.id.RecurPaymentList);
        recurList.setLayoutManager(new LinearLayoutManager(getContext()));
        RecurringPaymentAdapter recurringPaymentAdapter = new RecurringPaymentAdapter( settings.recurringTransactions);
        recurList.setAdapter(recurringPaymentAdapter);
    }

    private void setupBreakdown(UserSettings settings){
        TextView investText = view.findViewById(R.id.settingsBreakdownInvestValue);
        TextView savingsText = view.findViewById(R.id.settingsBreakdownSavingsValue);
        PieChart chart = view.findViewById(R.id.settingsBreakdownChart);

        HashMap<TransactionCategories,Float> breakdown = settings.idealBreakdown;
        if(breakdown.containsKey(TransactionCategories.INVESTMENT)) {
            Float percentageInvest = breakdown.get(TransactionCategories.INVESTMENT);
            investText.setText("$" + (settings.income.Amount * percentageInvest));
        }else{
            investText.setText("$0");
        }

        if(breakdown.containsKey(TransactionCategories.SAVINGS)){
            Float percentageSavings = breakdown.get(TransactionCategories.SAVINGS);
            savingsText.setText("$" + (settings.income.Amount * percentageSavings));
        } else{
            savingsText.setText("$0");
        }

        //Setup chart
        List<PieEntry> pieEntries = new ArrayList<>();
        for(Map.Entry<TransactionCategories,Float> t : settings.idealBreakdown.entrySet()){
            pieEntries.add(new PieEntry(t.getValue(), t.getKey().name()));
        }


        PieDataSet dataSet = new PieDataSet(pieEntries,"");
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        dataSet.setDrawValues(false);
        PieData data = new PieData(dataSet);
        chart.setData(data);

        //Remove legend.
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        //Remove description
        Description des = chart.getDescription();
        des.setEnabled(false);
        dataSet.setSliceSpace(2f);
        chart.setHoleColor(00000000);
        chart.setTouchEnabled(false);

        chart.invalidate();


    }
}