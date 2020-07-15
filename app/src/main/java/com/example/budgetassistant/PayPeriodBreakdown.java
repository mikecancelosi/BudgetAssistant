package com.example.budgetassistant;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.budgetassistant.viewmodels.PayPeriodBreakdownViewModel;

public class PayPeriodBreakdown extends Fragment {

    private PayPeriodBreakdownViewModel mViewModel;

    public static PayPeriodBreakdown newInstance() {
        return new PayPeriodBreakdown();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pay_period_breakdown_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PayPeriodBreakdownViewModel.class);
        // TODO: Use the ViewModel
    }

}