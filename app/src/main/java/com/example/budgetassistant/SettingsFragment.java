package com.example.budgetassistant;

import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.budgetassistant.Enums.TransactionCategories;
import com.example.budgetassistant.Utils.CalendarUtil;
import com.example.budgetassistant.Utils.ColorUtil;
import com.example.budgetassistant.adapters.RecurringPaymentAdapter;
import com.example.budgetassistant.adapters.AccountAdapter;
import com.example.budgetassistant.dialogs.AccountDialog;
import com.example.budgetassistant.dialogs.BreakdownDialog;
import com.example.budgetassistant.dialogs.IncomeDialog;
import com.example.budgetassistant.dialogs.ProfilePictureDialog;
import com.example.budgetassistant.dialogs.RecurringPaymentDialog;
import com.example.budgetassistant.models.Account;
import com.example.budgetassistant.models.Income;
import com.example.budgetassistant.models.RecurringTransaction;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.viewmodels.SettingsViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private SettingsViewModel mViewModel;
    private View view;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        mViewModel.init();
        mViewModel.getSettings().observe(getViewLifecycleOwner(), new Observer<UserSettings>() {
            @Override
            public void onChanged(UserSettings settings) {
                setupUI();
            }
        });


        setupUI();

        return view;
    }

    private void setupUI() {
        UserSettings settings = mViewModel.getSettings().getValue();

        setupHeader(settings);
        setupAccountsList(settings);
        setupRecurringPayments(settings);
        setupIncome(settings);
        setupBreakdown(settings);

    }

    //region Header
    private void setupHeader(UserSettings settings) {
        ImageView userProfilePic = view.findViewById(R.id.UserProfilePicture);
        TextView userHeading = view.findViewById(R.id.UsernameDisplay);
        TextView joinLabel = view.findViewById(R.id.memberSinceLabel);

        userProfilePic.setImageResource(settings.profilePicture);
        userProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPictureDialog();
            }
        });

        userHeading.setText(settings.name);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        joinLabel.setText("Member since " + sdf.format(settings.joinDate));
    }

    public void openPictureDialog() {
        ProfilePictureDialog dialog = new ProfilePictureDialog();
        dialog.setDialogResult(new ProfilePictureDialog.ProfilePictureDialogListener() {
            @Override
            public void applyChanges(String imagePath) {
            }
        });
        dialog.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(), "Example");
    }
    //endregion

    //region Accounts
    private void setupAccountsList(UserSettings settings) {
        RecyclerView accountListView = view.findViewById(R.id.AccountListView);
        Button addAccountBtn = view.findViewById(R.id.AddAccountBtn);
        accountListView.setLayoutManager(new LinearLayoutManager(getContext()));

        AccountAdapter bankAdapter = new AccountAdapter(settings.accounts);
        accountListView.setAdapter(bankAdapter);

        addAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAccountDialog();
            }
        });
    }

    private void openAccountDialog() {
        AccountDialog dialog = new AccountDialog();
        dialog.setDialogResult(new AccountDialog.AccountDialogListener() {
            @Override
            public void applyChanges(Account account) {
                mViewModel.postAccount(account);
            }
        });
        dialog.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(), "Example");
    }
    //endregion

    //region Income
    private void setupIncome(UserSettings settings) {
        TextView paycheckAmount = view.findViewById(R.id.IncomeDollarAmount);
        TextView frequencyText = view.findViewById(R.id.IncomeFrequency);
        TextView nextPayText = view.findViewById(R.id.IncomeNextPaycheck);
        LinearLayout incomeItem = view.findViewById(R.id.IncomeItem);

        paycheckAmount.setText("$" + settings.income.Amount);
        String periodKeyDisplay = CalendarUtil.calendarValueDisplay(settings.income.Period.getKey());
        Integer periodValue = settings.income.Period.getValue();
        if (periodValue == 1) {
            periodKeyDisplay = periodKeyDisplay.substring(0, periodKeyDisplay.length() - 1);
        }
        frequencyText.setText("Every " + periodValue + " " + periodKeyDisplay);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        nextPayText.setText("Next check on " + sdf.format(settings.income.GetNextPaycheckDate()));

        incomeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openIncomeDialog();
            }
        });
    }

    private void openIncomeDialog() {
        IncomeDialog dialog = new IncomeDialog();
        dialog.setIncome(mViewModel.getSettings().getValue().income);
        dialog.setDialogResult(new IncomeDialog.IncomeDialogListener() {
            @Override
            public void applyChanges(Income income) {
                mViewModel.updateIncome(income);
            }
        });
        dialog.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(), "Example");

    }

    //endregion

    //region RecurringPayments

    private void setupRecurringPayments(UserSettings settings) {
        RecyclerView recurList = view.findViewById(R.id.RecurPaymentList);
        Button addPaymentBtn = view.findViewById(R.id.AddRecurPaymentBtn);

        recurList.setLayoutManager(new LinearLayoutManager(getContext()));
        RecurringPaymentAdapter recurringPaymentAdapter = new RecurringPaymentAdapter(settings.recurringTransactions);
        recurList.setAdapter(recurringPaymentAdapter);

        addPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecurringPaymentDialog();
            }
        });
    }

    private void openRecurringPaymentDialog() {
        RecurringPaymentDialog dialog = new RecurringPaymentDialog();
        dialog.setDialogResult(new RecurringPaymentDialog.RecurringPaymentDialogListener() {
            @Override
            public void applyChanges(RecurringTransaction transaction) {
                mViewModel.postRecurringTransaction(transaction);
            }
        });
        dialog.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(), "Example");
    }

    //endregion

    private void setupBreakdown(UserSettings settings) {
        Button editBreakdownBtn = view.findViewById(R.id.EditBreakdownBtn);
        PieChart pieChart = view.findViewById(R.id.settingsBreakdownPieChart);

        editBreakdownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBreakdownDialog();
            }
        });

        setupBreakdownPieChart(pieChart, settings.idealBreakdown,getContext());

    }

    public static void setupBreakdownPieChart(PieChart chart,
                                              HashMap<TransactionCategories, Float> breakdown,
                                              Context context) {
        final List<PieEntry> entries = new ArrayList<>();

        int positiveValues =0;
        int negativeValues =0;

        if(breakdown.containsKey(TransactionCategories.INVESTMENT)){
            entries.add(new PieEntry(breakdown.get(TransactionCategories.INVESTMENT),"Investment"));
            positiveValues++;
        }
        if(breakdown.containsKey(TransactionCategories.SAVINGS)){
            entries.add(new PieEntry(breakdown.get(TransactionCategories.SAVINGS),"Savings"));
            positiveValues++;
        }

        for (Map.Entry<TransactionCategories, Float> entry : breakdown.entrySet()) {
            if (entry.getKey() != TransactionCategories.INVESTMENT &&
                entry.getKey() != TransactionCategories.SAVINGS) {
                entries.add(new PieEntry(entry.getValue(),entry.getKey().toString()));
                negativeValues++;
            }
        }


        PieDataSet dataSet = new PieDataSet(entries, "");
        // set colors
        List<Integer> colors = new ArrayList<>();
        int[] positiveColors = ColorUtil.createColorValueGradient(ContextCompat.getColor(context,
                                                                                         R.color.colorPrimary),
                                                                  positiveValues, .2f, 1f);
        int[] negativeColors = ColorUtil.createColorValueGradient(ContextCompat.getColor(context,
                                                                                         R.color.colorSecondary),
                                                                  negativeValues, .2f, 1f);

        for (int color : positiveColors) {
            colors.add(color);
        }

        for (int color : negativeColors) {
            colors.add(color);
        }

        dataSet.setColors(colors);
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

        chart.invalidate();

    }

    private void openBreakdownDialog() {
        BreakdownDialog dialog = new BreakdownDialog();
        UserSettings settings = mViewModel.getSettings().getValue();
        dialog.setBreakdown(settings.idealBreakdown,settings.income.Amount);
        dialog.setDialogResult(new BreakdownDialog.BreakdownDialogListener() {
            @Override
            public void applyChanges(HashMap<TransactionCategories, Float> breakdown) {
                mViewModel.postBreakdown(breakdown);
            }
        });
        dialog.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(), "Example");
    }
}