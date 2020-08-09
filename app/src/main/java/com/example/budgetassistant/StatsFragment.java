package com.example.budgetassistant;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.service.autofill.Dataset;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.viewmodels.HomeViewModel;
import com.example.budgetassistant.viewmodels.StatsViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends Fragment {

    private StatsViewModel mViewModel;
    private View view;

    public StatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_stats, container, false);

        mViewModel = new ViewModelProvider(this).get(StatsViewModel.class);
        mViewModel.init();

        mViewModel.getSettings().observe(getViewLifecycleOwner(), new Observer<UserSettings>() {
            @Override
            public void onChanged(UserSettings settings) {
                setUpUI();
            }
        });

        mViewModel.getTransactions().observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                setUpUI();
            }
        });
        setUpUI();

        return view;
    }

    private void setUpUI(){
        setupCategoryExpensePieChart();
        setupCategoricalHorizontalBarChart();
        setupMonthlyTrendBarChart();
    }

    private void setupCategoryExpensePieChart(){
        Spinner timeSpinner = view.findViewById(R.id.CategoryPieChartTimeSpinner);
        String[] frequencyItems = new String[]{"Pay Period", "Month"," Year","All Time"};
        ArrayAdapter<String> freqAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item,frequencyItems);
        timeSpinner.setAdapter(freqAdapter);
        timeSpinner.setSelection(0);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        updateCategoryExpensePieChart(mViewModel.getPayPeriodStartDate(),mViewModel.getPayPeriodEndDate());
                        break;
                    case 1:
                        updateCategoryExpensePieChart(mViewModel.getMonthStartDate(),mViewModel.getMonthEndDate());
                        break;
                    case 2:
                        updateCategoryExpensePieChart(mViewModel.getYearStartDate(),mViewModel.getYearEndDate());
                        break;
                    case 3:
                        updateCategoryExpensePieChart(new Date(0L),new Date(Long.MAX_VALUE));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setSelection(0);
            }
        });

    }

    private void updateCategoryExpensePieChart(Date startDate, Date endDate){
        PieChart chart = view.findViewById(R.id.CategoricalPieChart);

        float savings = mViewModel.getUnspentBudgetForPayPeriod();
        int colorId = savings > 0 ? R.color.colorPrimary : R.color.colorSecondary;
        int colorValue = ContextCompat.getColor(getContext(),colorId);

        List<PieEntry> pieEntries = new ArrayList<>();
        for(Map.Entry<TransactionCategories,Float> t : mViewModel.getCategorizedExpensesForTimeLine(startDate,endDate).entrySet()){
            pieEntries.add(new PieEntry(t.getValue(), t.getKey().name()));
        }

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

        dataSet.setColors(colorValue);
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

    // TODO: Adjust for percentage / Actual $
    // TODO: Adjust for month view.
    private void setupCategoricalHorizontalBarChart(){
        HorizontalBarChart chart = (HorizontalBarChart) view.findViewById(R.id.CategoricalBreakdownSummaryBarChart);
        List<BarEntry> entries = new ArrayList<>();

        int daysInPeriod = mViewModel.getTimePeriodInDays();
        final List<String> labels = new ArrayList<>();
        for(int i = 0 ; i < TransactionCategories.values().length;i++){
            TransactionCategories category = TransactionCategories.values()[i];
            if(category != TransactionCategories.INCOME) {
                labels.add(category.name());
                //Find Ideal Value
                float ideal = mViewModel.getIdealValueForCategory(category);
                //Find Current Pay Period Value
                float current = mViewModel.getCurrentValueForCategory(category);
                //Find Lifetime average value
                float average = mViewModel.getLifetimeAverageValueForCategory(category);
                //Set entry
                BarEntry entry = new BarEntry(labels.size() - 1, new float[]{ideal, current, average});
                entries.add(entry);
            }
        }

        BarDataSet set = new BarDataSet(entries,"");
        int[] colorId = new int[] {R.color.colorPrimary,R.color.colorPrimaryDark,R.color.colorSecondary};
        int[] colors = new int[] {ContextCompat.getColor(getContext(),colorId[0]),
                                  ContextCompat.getColor(getContext(),colorId[1]),
                                  ContextCompat.getColor(getContext(),colorId[2])};

        set.setColors(colors);
        set.setDrawIcons(false);
        set.setDrawValues(false);
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

        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return labels.get((int) value);
            }
        });
        chart.getXAxis().setLabelCount(labels.size());

        chart.invalidate();


    }

    private void setupMonthlyTrendBarChart(){
        BarChart chart = (BarChart) view.findViewById(R.id.MonthlyTrendChart);

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
        // Remove right labels
        chart.getAxisRight().setDrawLabels(false);
        //Set XAxis labels to the bottom
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        //Remove bottom padding
        chart.getAxisLeft().setAxisMinimum(0f);
        chart.getAxisRight().setAxisMinimum(0f);
        //Remove interactivity
        chart.setTouchEnabled(false);


        final List<String> labels = new ArrayList<>();
        List<BarEntry> entries = new ArrayList<>();

        for(int i = 13; i >= 0; i--){
            Calendar monthCal = Calendar.getInstance();
            monthCal.add(Calendar.MONTH,i * -1);
            String monthName = new SimpleDateFormat("MMM").format(monthCal.getTime());

            labels.add(monthName);
            entries.add(new BarEntry(i,mViewModel.getExpensesInMonth(i)));
        }

        BarDataSet set = new BarDataSet(entries,"");
        set.setColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        set.setDrawIcons(false);
        set.setDrawValues(false);
        BarData data = new BarData(set);
        data.setBarWidth(.1f);
        chart.setData(data);
        chart.getXAxis().setLabelCount(labels.size());

        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(){
        @Override
                public String getFormattedValue(float value){
            return labels.get((int) value);
        }
        });
        chart.invalidate();
    }

}