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
import android.widget.ListView;

import com.example.budgetassistant.adapters.TransactionHistoryAdapter;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.viewmodels.TransactionHistoryViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private TransactionHistoryViewModel mTransactionHistoryViewModel;
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


        mTransactionHistoryViewModel = new ViewModelProvider(this).get(TransactionHistoryViewModel.class);
        mTransactionHistoryViewModel.init();
        mTransactionHistoryViewModel.getTransactions().observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                mTransactionAdapter.notifyDataSetChanged();
            }
        });
        initListView();

        return view;
    }

    private void initListView(){

        mTransactionAdapter = new TransactionHistoryAdapter(getActivity());
        mListView.setAdapter(mTransactionAdapter);
    }
}