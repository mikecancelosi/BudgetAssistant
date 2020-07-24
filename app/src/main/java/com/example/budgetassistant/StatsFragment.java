package com.example.budgetassistant;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends Fragment {

    //TODO: Replace with test data found in repository.java
    float pieBreakdownData[] = {10f,13.4f,36f,24f,5f,1f,3f};
    String categoryNames[] = {"Rent","Booze","Em","Food","Home","Clothing","Gift"};

    public StatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        setupMonthlyTrendBarChart(view);
        return view;
    }


    private void setupMonthlyTrendBarChart(View view){
        BarChart chart = (BarChart) view.findViewById(R.id.MonthlyTrendChart);

        //Create temp data
        ArrayList<BarEntry> monthlyTrendData = new ArrayList<>();
        for(int i=0; i< 6; i++){
            int catCount = ((int)(Math.random() * 5)) + 1;
            float[] values = new float[catCount];
            for(int j=0; j< catCount; j++){
                values[j] = (float)(Math.random() * 10) + 5;
            }
            monthlyTrendData.add(new BarEntry(i,values));
        }

        BarDataSet set = new BarDataSet(monthlyTrendData,"Monthly Trends");
        set.setColors(ColorTemplate.LIBERTY_COLORS);
        set.setDrawIcons(false);
        BarData data = new BarData(set);
        chart.setData(data);
        data.setBarWidth(.3f);

        LimitLine ll = new LimitLine(20f,"Ideal");
        ll.enableDashedLine(50f,20f,1f);
        ll.setLineColor(Color.BLACK);
        ll.setLineWidth(2f);
        chart.getAxisLeft().addLimitLine(ll);

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
        final String[] labels = new String[]{"January","February","March","April","May","June"};
        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return labels[(int)value];
            }
        });
        chart.invalidate();
    }

}