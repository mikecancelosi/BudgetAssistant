package com.example.budgetassistant;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.budgetassistant.models.PayPeriodBreakdown;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.viewmodels.PayPeriodBreakdownViewModel;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
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
public class PayPeriodBreakdownFragment extends Fragment {

    private PayPeriodBreakdownViewModel mPayPeriodViewModel;
    private PieChart mChart;
    private View view;

    public PayPeriodBreakdownFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pay_period_breakdown, container, false);

        mPayPeriodViewModel = new ViewModelProvider(this).get(PayPeriodBreakdownViewModel.class);
        mPayPeriodViewModel.init();
        mPayPeriodViewModel.getBreakdown().observe(getViewLifecycleOwner(), new Observer<PayPeriodBreakdown>() {
            @Override
            public void onChanged(PayPeriodBreakdown payPeriodBreakdown) {

            }
        });
        setChart();

        return view;
    }

    public void setChart(){
        mChart = (PieChart) view.findViewById(R.id.PieBreakdown);
        //Populating a list of PieEntries
        List<PieEntry> pieEntries = new ArrayList<>();
        PayPeriodBreakdown breakdown = mPayPeriodViewModel.getBreakdown().getValue();

        for(Transaction t : breakdown.categorizedTransactions){
            pieEntries.add(new PieEntry(t.Expense,t.Category.name()));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries,"");
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS); //
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setSliceSpace(2f);
        dataSet.setValueLinePart2Length(.5f);
        dataSet.setValueTextSize(14f);
        PieData data = new PieData(dataSet);

        // Use percentages.
        data.setValueFormatter(new PercentFormatter(mChart));
        mChart.setUsePercentValues(true);

        mChart.setData(data);
        mChart.setExtraLeftOffset(30f);
        mChart.setExtraRightOffset(30f);

        mChart.setEntryLabelColor(Color.BLACK);
        mChart.spin(2500,270,360, Easing.EaseOutBounce);

        //Remove legend.
        Legend legend = mChart.getLegend();
        legend.setEnabled(false);
        //Remove description
        Description des = mChart.getDescription();
        des.setEnabled(false);
        mChart.invalidate();
    }
}