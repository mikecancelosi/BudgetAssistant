package com.example.budgetassistant;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.budgetassistant.Enums.TransactionCategories;
import com.example.budgetassistant.Utils.CalendarUtil;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.UserSettings;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    private List<Transaction> mTransactionsInTimePeriod;
    private Date mStartDate;
    private Date mEndDate;

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
        setupCategoryBreakdown();
        setupMonthlyTrendBarChart();
    }

    private void setupCategoryBreakdown(){
        Spinner timeSpinner = view.findViewById(R.id.CategoryPieChartTimeSpinner);
        String[] frequencyItems = new String[]{"Pay Period", "Month"," Year","All Time"};
        ArrayAdapter<String> freqAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item,frequencyItems);
        timeSpinner.setAdapter(freqAdapter);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        mStartDate = mViewModel.getPayPeriodStartDate();
                        mEndDate = mViewModel.getPayPeriodEndDate();
                        mTransactionsInTimePeriod = mViewModel.getTransactionsInTimePeriod(mStartDate,mEndDate);
                        updateCategoryExpensePieChart();
                        updateCategoricalHorizontalBarChart();
                        break;
                    case 1:
                        mStartDate = mViewModel.getMonthStartDate();
                        mEndDate = mViewModel.getMonthEndDate();
                        mTransactionsInTimePeriod = mViewModel.getTransactionsInTimePeriod(mStartDate,mEndDate);
                        updateCategoryExpensePieChart();
                        updateCategoricalHorizontalBarChart();
                        break;
                    case 2:
                        mStartDate = mViewModel.getYearStartDate();
                        mEndDate = mViewModel.getYearEndDate();
                        mTransactionsInTimePeriod = mViewModel.getTransactionsInTimePeriod(mStartDate,mEndDate);
                        updateCategoryExpensePieChart();
                        updateCategoricalHorizontalBarChart();
                        break;
                    case 3:
                        mStartDate = new Date(0L);
                        mEndDate = new Date(Long.MAX_VALUE);
                        mTransactionsInTimePeriod = mViewModel.getTransactionsInTimePeriod(mStartDate,mEndDate);
                        updateCategoryExpensePieChart();
                        updateCategoricalHorizontalBarChart();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setSelection(0);
            }
        });

        timeSpinner.setSelection(0);

    }

    private void updateCategoryExpensePieChart(){
        PieChart chart = view.findViewById(R.id.CategoricalPieChart);

        float savings = mViewModel.getUnspentBudgetForPeriod(mTransactionsInTimePeriod);
        int colorId = savings > 0 ? R.color.colorPrimary : R.color.colorSecondary;
        int colorValue = ContextCompat.getColor(getContext(),colorId);

        List<PieEntry> pieEntries = new ArrayList<>();
        for(Map.Entry<TransactionCategories,Float> t : mViewModel.getCategorizedExpenses(mTransactionsInTimePeriod).entrySet()){
            pieEntries.add(new PieEntry(t.getValue(), t.getKey().name()));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries,"");
        dataSet.setColors(colorValue);
        dataSet.setDrawValues(false);

        dataSet.setSliceSpace(5f);
        PieData data = new PieData(dataSet);
        chart.setData(data);
        //Remove legend.
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        //Remove description
        Description des = chart.getDescription();
        des.setEnabled(false);
        chart.setHoleColor(00000000);
        chart.setTouchEnabled(false);

        dataSet.setColors(colorValue);
        //Set center text
        BigDecimal savingsBigDecimal = new BigDecimal(Float.toString(savings));
        savingsBigDecimal = savingsBigDecimal.setScale(2, RoundingMode.FLOOR);
        chart.setCenterText("$" + savingsBigDecimal.toString());
        chart.setCenterTextSize(20f);
        int colorDarkId = savings > 0 ? R.color.colorPrimaryDark : R.color.colorSecondaryDark;
        int colorDarkValue = ContextCompat.getColor(getContext(),colorDarkId);
        chart.setCenterTextColor(colorDarkValue);
        chart.invalidate();
    }

    // TODO: Adjust for percentage / Actual $
    private void updateCategoricalHorizontalBarChart(){
        HorizontalBarChart chart = view.findViewById(R.id.CategoricalBreakdownSummaryBarChart);
        List<BarEntry> entries = new ArrayList<>();

        int daysBetween = CalendarUtil.daysBetween(mStartDate, mEndDate);

        final List<String> labels = new ArrayList<>();
        for(int i = 0 ; i < TransactionCategories.values().length;i++){
            TransactionCategories category = TransactionCategories.values()[i];
            if(category != TransactionCategories.INCOME) {
                labels.add(category.name());
                //Find Ideal Value
                float ideal = mViewModel.getIdealValueForCategory(category,
                                                                  mStartDate,mEndDate,
                                                                  mTransactionsInTimePeriod,
                                                                  mViewModel.getSettings().getValue().income.Period);
                //Find Current Pay Period Value
                float current = mViewModel.getCurrentValueForCategory(category,mTransactionsInTimePeriod);
                //Find Lifetime average value
                float average = mViewModel.getLifetimeAverageValueForCategory(category,daysBetween);
                float[] valueArray = new float[]{ideal,current,average};
                Arrays.sort(valueArray);
                //Set entry
                BarEntry entry = new BarEntry(labels.size() - 1, valueArray);
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

        chart.setFitBars(true);

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