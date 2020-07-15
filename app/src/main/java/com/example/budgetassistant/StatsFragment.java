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
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Test data.
    float pieBreakdownData[] = {10f,13.4f,36f,24f,5f,1f,3f};
    String categoryNames[] = {"Rent","Booze","Em","Food","Home","Clothing","Gift"};

    public StatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatsFragment newInstance(String param1, String param2) {
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        setupPieChart(view);
        setupMonthlyTrendBarChart(view);
        setupCategoryBreakdown(view);
        return view;
    }

    private void setupPieChart(View view) {
        //Populating a list of PieEntries
        List<PieEntry> pieEntries = new ArrayList<>();
        for(int i =0; i<pieBreakdownData.length;i++){
            pieEntries.add(new PieEntry(pieBreakdownData[i],categoryNames[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries,"");
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS); //
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setSliceSpace(2f);
        dataSet.setValueLinePart2Length(.5f);
        dataSet.setValueTextSize(14f);
        PieData data = new PieData(dataSet);

        PieChart chart = (PieChart) view.findViewById(R.id.PieBreakdown);
        chart.setData(data);
        chart.setExtraLeftOffset(30f);
        chart.setExtraRightOffset(30f);

        chart.setEntryLabelColor(Color.BLACK);
        chart.spin(2500,270,360,Easing.EaseOutBounce);

        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        Description des = chart.getDescription();
        des.setEnabled(false);
        chart.invalidate();
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

    private  void setupCategoryBreakdown(View view){
        BarChart chart = view.findViewById(R.id.CategoryBreakdown);

        ArrayList<BarEntry> monthlyTrendData = new ArrayList<>();
        for(int i=0; i< 6; i++){
            int catCount = 3;
            float[] values = new float[catCount];
            for(int j=0; j< catCount; j++){
                values[j] = (float)(Math.random() * 10) + 5;
            }
            monthlyTrendData.add(new BarEntry(i,values));
        }

        BarDataSet set = new BarDataSet(monthlyTrendData,"Category Breakdown");
        set.setColors(ColorTemplate.LIBERTY_COLORS);
        set.setDrawIcons(false);
        BarData data = new BarData(set);
        chart.setData(data);
        data.setBarWidth(.3f);

        //Chart Visuals
        XAxis xAxis = chart.getXAxis();

        //Remove description
        Description des = chart.getDescription();
        des.setEnabled(false);
        //remove legend
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        //Remove grid lines
        xAxis.setDrawGridLines(false);
        chart.setFitBars(true);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        // Animate graph
        chart.animateY(1000);
        // Remove right labels
        chart.getAxisRight().setDrawLabels(false);
        //Set XAxis label to the bottom
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //Remove bottom padding
        chart.getAxisLeft().setAxisMinimum(0f);
        chart.getAxisRight().setAxisMinimum(0f);
        //Remove interactivity
        chart.setTouchEnabled(false);
        //Set Labels
        final String[] labels = new String[]{"Rent","Food","Em","Booze","Gas","Games"};
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return labels[(int)value];
            }
        });
        chart.invalidate();
    }
}