package com.example.budgetassistant;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.TransactionSummary;
import com.example.budgetassistant.viewmodels.TransactionSummaryViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoricalPieChartBreakdownFragment extends Fragment {

    private TransactionSummaryViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.categorical_pie_chart_breakdown_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TransactionSummaryViewModel.class);
        mViewModel.init();
        mViewModel.getSummary().observe(getViewLifecycleOwner(), new Observer<TransactionSummary>() {
            @Override
            public void onChanged(TransactionSummary summary) {
                setupChart();
            }
        });
        setupChart();
    }

    private void setupChart(){
        PieChart chart = getView().findViewById(R.id.CategoricalPieChart);
        List<PieEntry> pieEntries = new ArrayList<>();
        TransactionSummary summary = mViewModel.getSummary().getValue();
        for(Map.Entry<TransactionCategories,Float> t : mViewModel.GetCategorizedExpenseValues().entrySet()){
            pieEntries.add(new PieEntry(t.getValue(), t.getKey().name()));
        }
        float savings = summary.Budget - summary.GetExpenseTotal();


        int colorId = savings > 0 ? R.color.colorPrimary : R.color.colorSecondary;
        int colorValue = ContextCompat.getColor(getContext(),colorId);

        PieDataSet dataSet = new PieDataSet(pieEntries,"");
        dataSet.setColors(colorValue);
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
        //Set center text
        BigDecimal savingsBigDecimal = new BigDecimal(Float.toString(savings));
        savingsBigDecimal = savingsBigDecimal.setScale(2, RoundingMode.FLOOR);
        chart.setCenterText( (savings > 0 ? "+" : "-") + "$" +savingsBigDecimal.toString());
        chart.setCenterTextSize(20f);
        int colorDarkId = savings > 0 ? R.color.colorPrimaryDark : R.color.colorSecondaryDark;
        int colorDarkValue = ContextCompat.getColor(getContext(),colorDarkId);
        chart.setCenterTextColor(colorDarkValue);

        chart.invalidate();
    }

}