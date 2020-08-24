package com.example.budgetassistant.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetassistant.R;
import com.example.budgetassistant.Enums.TransactionCategories;
import com.example.budgetassistant.SettingsFragment;
import com.example.budgetassistant.adapters.BreakdownInputAdapter;
import com.example.budgetassistant.models.Transaction;
import com.github.mikephil.charting.charts.PieChart;

import java.util.HashMap;
import java.util.Map;

public class BreakdownDialog extends AppCompatDialogFragment {

    private HashMap<TransactionCategories, Float> mBreakdown;
    private View mView;
    private BreakdownDialogListener mListener;
    private PieChart mChart;
    private RecyclerView mRecycler;
    private Float mIncomeAmount;
    private int selectedColor;
    private int surfaceColor;
    private int errorColor;
    private boolean mPercentView = false;

    public void setBreakdown(HashMap<TransactionCategories, Float> newBreakdown,
                             Float incomeAmount) {
        mBreakdown = newBreakdown;
        mIncomeAmount = incomeAmount;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mView = View.inflate(getContext(), R.layout.dialog_breakdown, null);
        final AlertDialog dialog = builder.setView(mView)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button posBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                posBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Float total = 0f;
                        for (Float value : mBreakdown.values()) {
                            total += value;
                        }
                        if (total.equals(1f)) {
                            mListener.applyChanges(mBreakdown);
                            dialog.dismiss();
                        } else {
                            TextView totalView = mView.findViewById(R.id.DialogBreakdownTotalValue);
                            totalView.setBackgroundColor(errorColor);
                            dialog.show();
                        }
                    }
                });
            }
        });

        if (mBreakdown == null) {
            mBreakdown = new HashMap<>();
        }
        setupDialogView();
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                                     ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        return dialog;
    }

    private void setupDialogView() {
        selectedColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        surfaceColor = ContextCompat.getColor(getContext(), R.color.colorSurface);
        errorColor = ContextCompat.getColor(getContext(),R.color.colorError);
        mChart = mView.findViewById(R.id.dialogBreakdownPieChart);
        mRecycler = mView.findViewById(R.id.BreakdownDialogRecycler);
        Button percentButton = mView.findViewById(R.id.dialogBreakdownPercentView);
        Button dollarButton = mView.findViewById(R.id.dialogBreakdownDollarView);
        final Button addCategoryButton = mView.findViewById(R.id.BreakdownDialogAddCategoryBtn);

        mRecycler.setHasFixedSize(false);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        final BreakdownInputAdapter mAdapter = new BreakdownInputAdapter(mBreakdown, mIncomeAmount);
        mAdapter.setListener(new BreakdownInputAdapter.BreakdownInputAdapterListener() {
            @Override
            public void applyChanges(HashMap<TransactionCategories, Float> breakdown) {
                mBreakdown = breakdown;
                updateView();
            }
        });
        mRecycler.setAdapter(mAdapter);

        addCategoryButton.setVisibility((mBreakdown.keySet().size() < (TransactionCategories.values().length -1) ?
                                         View.VISIBLE :View.INVISIBLE));

        percentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView(true);
            }
        });
        dollarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView(false);
            }
        });
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAdapter.addCategory()){
                   addCategoryButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        updateView();
        changeView(true);
    }

    private void updateView() {

        Button percentButton = mView.findViewById(R.id.dialogBreakdownPercentView);
        Button dollarButton = mView.findViewById(R.id.dialogBreakdownDollarView);
        TextView incomeAmountText = mView.findViewById(R.id.dialogbreakdownIncomeAmount);
        TextView totalValueText = mView.findViewById(R.id.DialogBreakdownTotalValue);
        Button addCategoryButton = mView.findViewById(R.id.BreakdownDialogAddCategoryBtn);

        percentButton.setBackgroundColor(mPercentView ? selectedColor : surfaceColor);
        dollarButton.setBackgroundColor(!mPercentView ? selectedColor : surfaceColor);
        SettingsFragment.setupBreakdownPieChart(mChart, mBreakdown, getContext());
        incomeAmountText.setVisibility(mPercentView ? View.INVISIBLE : View.VISIBLE);
        incomeAmountText.setText("/$" + mIncomeAmount);

        Float total = 0f;


        for (Float value : mBreakdown.values()) {
            total += value;
        }
        if (mPercentView) {
            total *= 100;
            totalValueText.setText(String.format("%.1f", total) + "%");
        } else {
            String format = "%.2f";
            Float dollarAmount = mIncomeAmount * total;
            String dollarAmountFormatted = "$" + String.format(format, dollarAmount);
            totalValueText.setText(dollarAmountFormatted);
        }

    }

    private void changeView(boolean percent) {
        if (mPercentView != percent) {
            mPercentView = percent;

            updateView();
            BreakdownInputAdapter adapter = (BreakdownInputAdapter) mRecycler.getAdapter();
            assert adapter != null;
            adapter.changeView(mPercentView);
            adapter.notifyDataSetChanged();
        }

    }

    public void setDialogResult(BreakdownDialog.BreakdownDialogListener listener) {
        mListener = listener;
    }

    public interface BreakdownDialogListener {
        void applyChanges(HashMap<TransactionCategories, Float> breakdown);
    }
}
