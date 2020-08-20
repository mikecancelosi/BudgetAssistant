package com.example.budgetassistant.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.budgetassistant.R;
import com.example.budgetassistant.Enums.TransactionCategories;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.HashMap;

public class BreakdownDialog extends AppCompatDialogFragment {

     private HashMap<TransactionCategories,Float> breakdown;
     private View mView;
     private BreakdownDialogListener mListener;

     public void setBreakdown(HashMap<TransactionCategories,Float> newBreakdown){
         breakdown = newBreakdown;
     }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mView = View.inflate(getContext(), R.layout.dialog_breakdown, null);
        builder.setView(mView)
                .setPositiveButton("Save", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mListener.applyChanges(breakdown);
                    }
                });

        Dialog dialog =  builder.create();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(breakdown == null){
            breakdown = new HashMap<>();
        }
        setupDialogView();
        return dialog;
    }

    private void setupDialogView(){
        Button percentViewBtn = mView.findViewById(R.id.dialogBreakdownPercentView);
        Button dollarViewBtn = mView.findViewById(R.id.dialogBreakdownDollarView);
        BarChart barChart = mView.findViewById(R.id.dialogBreakdownBarChart);
        PieChart pieChart = mView.findViewById(R.id.dialogBreakdownPieChart);

        percentViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView(true);
            }
        });
        dollarViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView(false);
            }
        });

        //region BarChart
        //endregion

        //region PieChart
        //endregion

    }

    private void changeView(boolean percent){

    }

    public void setDialogResult(BreakdownDialog.BreakdownDialogListener listener){
        mListener = listener;
    }

    public interface BreakdownDialogListener{
        void applyChanges(HashMap<TransactionCategories,Float> breakdown);
    }
}
