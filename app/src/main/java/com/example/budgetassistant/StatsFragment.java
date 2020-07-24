package com.example.budgetassistant;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.viewmodels.HomeViewModel;
import com.example.budgetassistant.viewmodels.StatsViewModel;
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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
        setupCategoricalHorizontalBarChart();
        setupCategoryExpensePieChart();
    }

    private void setupCategoryExpensePieChart(){
        PieChart chart = view.findViewById(R.id.CategoricalPieChart);
        List<PieEntry> pieEntries = new ArrayList<>();
        for(Map.Entry<TransactionCategories,Float> t : mViewModel.getCategorizedExpensesForPayPeriod().entrySet()){
            pieEntries.add(new PieEntry(t.getValue(), t.getKey().name()));
        }

        float savings = mViewModel.getUnspentBudgetForPayPeriod();
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

    // TODO: Adjust for percentage / Actual $
    // TODO: Adjust for month view.
    private void setupCategoricalHorizontalBarChart(){
        HorizontalBarChart chart = (HorizontalBarChart) view.findViewById(R.id.CategoricalBreakdownSummaryBarChart);
        List<BarEntry> entries = new ArrayList<>();

        int daysInPeriod = mViewModel.getTimePeriodInDays();
        for(int i = 0 ; i < TransactionCategories.values().length;i++){
            TransactionCategories category = TransactionCategories.values()[i];
            //Find Ideal Value
            float ideal = mViewModel.getIdealValueForCategory(category);
            //Find Current Pay Period Value
            float current = mViewModel.getCurrentValueForCategory(category);
            //Find Lifetime average value
            float average = mViewModel.getLifetimeAverageValueForCategory(category);
            //Set entry
            BarEntry entry = new BarEntry(i,new float[] {ideal,current,average});
            entries.add(entry);
        }

        BarDataSet set = new BarDataSet(entries,"");
        int[] colorId = new int[] {R.color.colorPrimary,R.color.colorPrimaryDark,R.color.colorSecondary};
        int[] colors = new int[] {ContextCompat.getColor(getContext(),colorId[0]),
                                  ContextCompat.getColor(getContext(),colorId[1]),
                                  ContextCompat.getColor(getContext(),colorId[2])};

        set.setColors(colors);
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