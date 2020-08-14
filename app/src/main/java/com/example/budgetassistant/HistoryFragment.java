package com.example.budgetassistant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.budgetassistant.adapters.TransactionHistoryAdapter;
import com.example.budgetassistant.dialogs.RecurringPaymentDialog;
import com.example.budgetassistant.dialogs.TransactionDialog;
import com.example.budgetassistant.models.RecurringTransaction;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.repositories.TransactionRepository;
import com.example.budgetassistant.viewmodels.TransactionHistoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static com.example.budgetassistant.adapters.RecurringPaymentAdapter.applyChangeToTransaction;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private TransactionHistoryViewModel mViewModel;
    private TransactionHistoryAdapter mTransactionAdapter;
    private ListView mListView;
    private View view;

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
        view =  inflater.inflate(R.layout.fragment_history, container, false);
        mListView = (ListView) view.findViewById(R.id.HistoryList);


        mViewModel = new ViewModelProvider(this).get(TransactionHistoryViewModel.class);
        mViewModel.init();
        mViewModel.getTransactions().observe(getViewLifecycleOwner(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                mTransactionAdapter.notifyDataSetChanged();
            }
        });
        initListView();

        FloatingActionButton addBtn = view.findViewById(R.id.HistoryAddTransBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDialog();
            }
        });


        return view;
    }

    private void initListView(){
        mTransactionAdapter = new TransactionHistoryAdapter(getActivity(),this);
        TreeMap<Date,List<Transaction>> map = mViewModel.getDatedTransactions();
        for(Map.Entry<Date,List<Transaction>> set : map.entrySet()){
            mTransactionAdapter.addHeader(set.getKey());

            for(Transaction trans : set.getValue()){
                mTransactionAdapter.addItem(trans);
            }

        }

        mListView.setAdapter(mTransactionAdapter);
    }

    private void openAddDialog(){
        TransactionDialog dialog = new TransactionDialog();
        dialog.setDialogResult(new TransactionDialog.TransactionDialogListener() {
            @Override
            public void applyChanges(Transaction transaction) {
                TransactionRepository.getInstance().postTransaction(transaction);
                initListView();
            }
        });
        dialog.show(((FragmentActivity)view.getContext()).getSupportFragmentManager(), "Example");
    }

    public void openEditDialog(Integer index){
        TransactionDialog dialog = new TransactionDialog();
        dialog.setTransaction(mTransactionAdapter.getItem(index));
        dialog.setDialogResult(new TransactionDialog.TransactionDialogListener() {
            @Override
            public void applyChanges(Transaction transaction) {
                TransactionRepository.getInstance().postTransaction(transaction);
                initListView(); //TODO: Make more efficient than redrawing whole list.
            }
        });

        dialog.show(((FragmentActivity)view.getContext()).getSupportFragmentManager(), "Example");



    }
}