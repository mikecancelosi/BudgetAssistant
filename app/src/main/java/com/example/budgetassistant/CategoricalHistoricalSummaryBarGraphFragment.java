package com.example.budgetassistant;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.budgetassistant.models.TransactionSummary;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.viewmodels.TransactionSummaryViewModel;
import com.example.budgetassistant.viewmodels.UserSettingsViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CategoricalHistoricalSummaryBarGraphFragment extends Fragment {

    private TransactionSummaryViewModel mCurrentSummaryViewModel;
    private TransactionSummaryViewModel mLifetimeSummaryViewModel;
    private UserSettingsViewModel mSettingsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categorical_historical_summary_bar_graph_fragment, container, false);

        mCurrentSummaryViewModel = new ViewModelProvider(this).get(TransactionSummaryViewModel.class);
        mLifetimeSummaryViewModel = new ViewModelProvider(this).get(TransactionSummaryViewModel.class);
        mSettingsViewModel = new ViewModelProvider(this).get(UserSettingsViewModel.class);
        mSettingsViewModel.init();
        mCurrentSummaryViewModel.init();
        mLifetimeSummaryViewModel.init();
        mLifetimeSummaryViewModel.setDateRange(new Date(0L), new Date(Long.MAX_VALUE));
        mLifetimeSummaryViewModel.adjustDateTimelineToFitTransactions();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCurrentSummaryViewModel.getSummary().observe(getViewLifecycleOwner(), new Observer<TransactionSummary>() {
            @Override
            public void onChanged(TransactionSummary summary) {
                setupChart();
            }
        });
        mLifetimeSummaryViewModel.getSummary().observe(getViewLifecycleOwner(), new Observer<TransactionSummary>() {
            @Override
            public void onChanged(TransactionSummary summary) {
                setupChart();
            }
        });
        mSettingsViewModel.getSettings().observe(getViewLifecycleOwner(), new Observer<UserSettings>() {
            @Override
            public void onChanged(UserSettings settings) {
                setupChart();
            }
        });

        setupChart();
    }

    // TODO: Adjust for percentage / Actual $
    // TODO: Adjust for month view.
    private void setupChart(){
        HorizontalBarChart chart = (HorizontalBarChart)getView().findViewById(R.id.CategoricalBreakdownSummaryBarChart);
        List<BarEntry> entries = new ArrayList<>();
        UserSettings settings = mSettingsViewModel.getSettings().getValue();

        Log.d("!","0 " +  mCurrentSummaryViewModel.GetTimePeriodInDays());

        int daysInPeriod = mCurrentSummaryViewModel.GetTimePeriodInDays();
        Log.d("!","1 " + daysInPeriod);
        for(int i = 0 ; i < TransactionCategories.values().length;i++){
            TransactionCategories category = TransactionCategories.values()[i];
            //Find Ideal Value
            float ideal = 0f;
            if(settings.idealBreakdown.containsKey(category))
            {
                ideal = settings.idealBreakdown.get(category);
            }
            //Find Current Pay Period Value
            float currentExpense = mCurrentSummaryViewModel.GetCategoryExpenseValue(category);
            int currentDays = daysInPeriod - mCurrentSummaryViewModel.GetDaysLeftInTimePeriod();
            float currentExpenseEstimatedForPeriod = currentExpense / (currentDays / daysInPeriod);
            Log.d("?", "2 " + currentDays + " | " + daysInPeriod + " / " + mCurrentSummaryViewModel.GetDaysLeftInTimePeriod());
            float estimatedExpenseAsPercentage = currentExpenseEstimatedForPeriod / settings.income.Amount;
            Log.d("?", "3" + estimatedExpenseAsPercentage);
            //Find Lifetime average value
            float lifetimeExpense = mLifetimeSummaryViewModel.GetCategoryExpenseValue(category);
            float average = lifetimeExpense / (mLifetimeSummaryViewModel.GetTimePeriodInDays() / daysInPeriod);
            //Set entry
            BarEntry entry = new BarEntry(i,new float[] {ideal,estimatedExpenseAsPercentage,average});
            entries.add(entry);
        }

        BarDataSet set = new BarDataSet(entries,"");
        set.setColors(ColorTemplate.LIBERTY_COLORS);
        set.setDrawIcons(false);
        BarData data = new BarData(set);
        chart.setData(data);
        data.setBarWidth(.2f);
        set.setValueTextSize(10f);

        //Chart Visuals
        //Remove description
        Description des = chart.getDescription();
        des.setEnabled(false);
        //remove legend
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        //Remove vertical grid lines
        chart.getXAxis().setDrawGridLines(false);
        chart.setFitBars(true);
        // Animate graph
        chart.animateY(1000);
        // Remove right labels
        chart.getAxisRight().setDrawLabels(false);
        //Set XAxis label to the bottom
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        //Remove bottom padding
        chart.getAxisLeft().setAxisMinimum(0f);
        chart.getAxisRight().setAxisMinimum(0f);
        //Remove interactivity
        chart.setTouchEnabled(false);
        // Set category labels
        final List<String> labels = new ArrayList<>();
        for(TransactionCategories cat : TransactionCategories.values()){
            labels.add(cat.name());
        }
        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return labels.get((int) value);
            }
        });
        chart.getXAxis().setLabelCount(labels.size());

        chart.invalidate();


    }

}
