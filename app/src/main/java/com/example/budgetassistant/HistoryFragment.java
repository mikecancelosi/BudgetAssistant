package com.example.budgetassistant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.budgetassistant.adapters.TransactionHistoryAdapter;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.TransactionSummary;
import com.example.budgetassistant.viewmodels.TransactionSummaryViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private TransactionSummaryViewModel mTransactionViewModel;
    private TransactionHistoryAdapter mTransactionAdapter;
    private ListView mListView;

    public HistoryFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_history, container, false);
        mListView = (ListView) view.findViewById(R.id.HistoryList);


        mTransactionViewModel = new ViewModelProvider(this).get(TransactionSummaryViewModel.class);
        mTransactionViewModel.init();
        mTransactionViewModel.getSummary().observe(getViewLifecycleOwner(), new Observer<TransactionSummary>() {
            @Override
            public void onChanged(TransactionSummary summary) {
                mTransactionAdapter.notifyDataSetChanged();
            }
        });
        initListView();

        return view;
    }

    private void initListView(){
        mTransactionAdapter = new TransactionHistoryAdapter(getActivity());
        for(Transaction t : mTransactionViewModel.getSummary().getValue().Transactions){
            mTransactionAdapter.addItem(t);
        }
        mListView.setAdapter(mTransactionAdapter);
    }
}