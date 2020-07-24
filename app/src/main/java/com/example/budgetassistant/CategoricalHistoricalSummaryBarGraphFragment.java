package com.example.budgetassistant;

import android.os.Bundle;
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
import com.github.mikephil.charting.data.BarEntry;

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
        return inflater.inflate(R.layout.categorical_historical_summary_bar_graph_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCurrentSummaryViewModel = new ViewModelProvider(this).get(TransactionSummaryViewModel.class);
        mLifetimeSummaryViewModel = new ViewModelProvider(this).get(TransactionSummaryViewModel.class);
        mSettingsViewModel = new ViewModelProvider(this).get(UserSettingsViewModel.class);
        mSettingsViewModel.init();
        mCurrentSummaryViewModel.init();
        mLifetimeSummaryViewModel.init();
        mLifetimeSummaryViewModel.setDateRange(new Date(0L), new Date(Long.MAX_VALUE));
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

    private void setupChart(){
        BarChart chart = (BarChart)getView().findViewById(R.id.CategoryBreakdownSummary);
        TransactionSummary summary = mCurrentSummaryViewModel.getSummary().getValue();
        List<BarEntry> entries = new ArrayList<>();
        UserSettings settings = mSettingsViewModel.getSettings().getValue();
        TransactionSummary currentSummary = mCurrentSummaryViewModel.getSummary().getValue();
        TransactionSummary lifetimeSummary = mLifetimeSummaryViewModel.getSummary().getValue();
        for(int i = 0 ; i < TransactionCategories.values().length;i++){
            TransactionCategories category = TransactionCategories.values()[i];
            float ideal = 0f;
            if(settings.getIdealBreakdown().containsKey(category))
            {
                ideal = settings.getIdealBreakdown().get(category);
            }
            float current = mCurrentSummaryViewModel.GetCategoryExpenseValue(category);
            int daysForCurrent = mCurrentSummaryViewModel.GetTimePeriodInDays();
            float average = mLifetimeSummaryViewModel.GetCategoryExpenseValue(category);
            BarEntry entry = new BarEntry(i,new float[] {ideal,current,average});
            entries.add(entry);
        }


    }

}
