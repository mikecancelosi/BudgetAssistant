package com.example.budgetassistant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.budgetassistant.models.Income;
import com.example.budgetassistant.models.TransactionSummary;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.viewmodels.TransactionSummaryViewModel;
import com.example.budgetassistant.viewmodels.UserSettingsViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayPeriodSummaryFragment extends Fragment {

    private TransactionSummaryViewModel mTransactionSummaryViewModel;
    private UserSettingsViewModel mSettingsViewModel;
    private PieChart mChart;
    private View view;

    public PayPeriodSummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pay_period_summary, container, false);

        mSettingsViewModel = new ViewModelProvider(this).get(UserSettingsViewModel.class);
        mTransactionSummaryViewModel = new ViewModelProvider(this).get(TransactionSummaryViewModel.class);
        mSettingsViewModel.init();
        mSettingsViewModel.getSettings().observe(getViewLifecycleOwner(), new Observer<UserSettings>() {
            @Override
            public void onChanged(UserSettings settings) {
                setChart();
            }
        });
        UserSettings settings = mSettingsViewModel.getSettings().getValue();
        Income income = settings.GetIncome();
        mTransactionSummaryViewModel.init();
        mTransactionSummaryViewModel.getSummary().observe(getViewLifecycleOwner(), new Observer<TransactionSummary>() {
            @Override
            public void onChanged(TransactionSummary summary) {
                setChart();
            }
        });
        setChart();

        return view;
    }

    public void setChart(){
        mChart = (PieChart) view.findViewById(R.id.PieBreakdown);
        //Populating a list of PieEntries
        List<PieEntry> pieEntries = new ArrayList<>();
        TransactionSummary breakdown = mTransactionSummaryViewModel.getSummary().getValue();

        float expensePercentage = breakdown.GetExpenseTotal() / breakdown.Budget;
        pieEntries.add(new PieEntry(expensePercentage,"Expenses"));
        if(expensePercentage < 1) {
            pieEntries.add(new PieEntry(1-expensePercentage, "Unspent"));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries,"");
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS); //
        dataSet.setDrawValues(false);
        mChart.setDrawEntryLabels(false);


        PieData data = new PieData(dataSet);
        // Use percentages.
        data.setValueFormatter(new PercentFormatter(mChart));
        mChart.setUsePercentValues(true);
        mChart.setData(data);

        mChart.setTouchEnabled(false);
        //Set ChartSizes
        mChart.setHoleRadius(50f);
        dataSet.setSelectionShift(0f);
        //Set margin
        mChart.setExtraLeftOffset(30f);
        mChart.setExtraRightOffset(30f);
        //Remove legend.
        Legend legend = mChart.getLegend();
        legend.setEnabled(false);
        //Remove description
        Description des = mChart.getDescription();
        des.setEnabled(false);
        //Set colors
        mChart.setHoleColor(00000000);
        dataSet.setColors(new int[]{R.color.colorPrimary,R.color.colorPrimaryDark},getContext());
        //Set Text
        int daysLeftInPayPeriod = breakdown.GetDaysLeftInTimePeriod();
        mChart.setCenterText(daysLeftInPayPeriod + " Days left");



        mChart.invalidate();

    }
}