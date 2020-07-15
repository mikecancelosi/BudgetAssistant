package com.example.budgetassistant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.viewmodels.TransactionHistoryViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private TransactionHistoryViewModel transactionHistoryViewModel;
    private TransactionAdapter transactionAdapter;
    private RecyclerView recyclerView;

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
        recyclerView = (RecyclerView) view.findViewById(R.id.HistoryList);


        transactionHistoryViewModel = new ViewModelProvider(this).get(TransactionHistoryViewModel.class);
        transactionHistoryViewModel.init();
        transactionHistoryViewModel.getTransactions().observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                transactionAdapter.notifyDataSetChanged();
            }
        });
        initRecyclerView();

        return view;
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        transactionAdapter = new TransactionAdapter(transactionHistoryViewModel.getTransactions().getValue(),getActivity());
        recyclerView.setAdapter(transactionAdapter);
    }
}