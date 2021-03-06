package com.example.budgetassistant;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.budgetassistant.adapters.RecurringPaymentAdapter;
import com.example.budgetassistant.models.BankAccount;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        setUpUI();

        return view;
    }

    private void setUpUI(){
        setUpHeader();
        setUpList();
        setupPayPeriodSummaryPieChart();
    }

    private void setUpHeader(){
        //Set item_account balance
        TextView AccountBalance = (TextView) view.findViewById(R.id.BankAccountBalance);
        AccountBalance.setText("N/A"); //TODO:Integrate View model to fetch bank balance.

        //Set user name and picture
        UserSettings settings = mViewModel.getSettings().getValue();

        TextView nameText = (TextView) view.findViewById(R.id.UserName);
        ImageView profileView = view.findViewById(R.id.ProfilePic);
        profileView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ((BottomNavigationView)getActivity().findViewById(R.id.BottomNav)).setSelectedItemId(R.id.destination_settings);
            }
        });

        nameText.setText(settings.name);
        profileView.setImageResource(settings.profilePicture);
    }

    private void setUpList(){
        RecyclerView myRecView = view.findViewById(R.id.BillList);
        myRecView.setHasFixedSize(true);
        myRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecurringPaymentAdapter recurringPaymentAdapter = new RecurringPaymentAdapter(mViewModel.getSettings().getValue().recurringTransactions);
        myRecView.setAdapter(recurringPaymentAdapter);

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