package com.example.budgetassistant;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.budgetassistant.adapters.AlertAdapter;
import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.viewmodels.HomeViewModel;
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
public class HomeFragment extends Fragment {
    private ListView mListView;
    private HomeViewModel mViewModel;
    private View view;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        mViewModel.init();

        mViewModel.getSettings().observe(getViewLifecycleOwner(), new Observer<UserSettings>() {
            @Override
            public void onChanged(UserSettings settings) {
                setUpUI();
            }
        });

        mViewModel.getAccount().observe(getViewLifecycleOwner(), new Observer<BankAccount>() {
            @Override
            public void onChanged(BankAccount account) {
                setUpUI();
            }
        });

        setUpUI();

        return view;
    }

    private void setUpUI(){
        setUpHeader();
        setUpList();
        setupPayPeriodSummaryPieChart();
    }

    private void setUpHeader(){
        //Set account balance
        TextView AccountBalance = (TextView) view.findViewById(R.id.BankAccountBalance);
        AccountBalance.setText("$" + mViewModel.getAccount().getValue().Balance);

        //Set user name and picture
        TextView nameText = (TextView) view.findViewById(R.id.UserName);
        nameText.setText(mViewModel.getSettings().getValue().name);
    }

    private void setUpList(){
        Resources res = getResources();
        RecyclerView myRecView = (RecyclerView) view.findViewById(R.id.BillList);
        myRecView.setHasFixedSize(true);
        myRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        AlertAdapter alertAdapter = new AlertAdapter(mViewModel.getSettings().getValue().recurringTransactions);
        myRecView.setAdapter(alertAdapter);
    }

    public void setupPayPeriodSummaryPieChart(){
        PieChart mChart = (PieChart) view.findViewById(R.id.PieBreakdown);

        //Populating a list of PieEntries
        List<PieEntry> pieEntries = new ArrayList<>();

        float expensePercentage = mViewModel.getExpenseAsPercentage();
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
        int daysLeftInPayPeriod = mViewModel.getDaysUntilNextPaycheck();
        mChart.setCenterText(daysLeftInPayPeriod + " Days left");

        mChart.invalidate();

    }
}