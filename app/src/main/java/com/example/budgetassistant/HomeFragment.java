package com.example.budgetassistant;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.budgetassistant.adapters.AlertAdapter;
import com.example.budgetassistant.adapters.TransactionHistoryAdapter;
import com.example.budgetassistant.viewmodels.AlertViewModel;
import com.example.budgetassistant.viewmodels.TransactionHistoryViewModel;
import com.example.budgetassistant.viewmodels.UserSettingsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private AlertViewModel mAlertViewModel;
    private AlertAdapter mAlertAdapter;
    private ListView mListView;
    private UserSettingsViewModel mUserSettings;

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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mUserSettings = UserSettingsViewModel.getInstance();

        setUpList(view);

        return view;
    }

    private void setUpList(View view){
        Resources res = getResources();
        ListView myListView = (ListView) view.findViewById(R.id.BillList);


        AlertAdapter alertAdapter = new AlertAdapter(view.getContext());
        myListView.setAdapter(alertAdapter);
    }
}