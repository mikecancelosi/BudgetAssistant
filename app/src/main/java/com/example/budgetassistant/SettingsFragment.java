package com.example.budgetassistant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.budgetassistant.adapters.BankAccountAdapter;
import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.repositories.BankRepository;
import com.example.budgetassistant.viewmodels.SettingsViewModel;
import com.example.budgetassistant.viewmodels.StatsViewModel;

import java.text.SimpleDateFormat;

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

    private void setupUI(){
        ImageView userProfilePic = view.findViewById(R.id.UserProfilePicture);
        TextView userHeading = view.findViewById(R.id.UsernameDisplay);
        TextView joinLabel = view.findViewById(R.id.memberSinceLabel);

        ListView accountListView = view.findViewById(R.id.AccountListView);

        UserSettings settings = mViewModel.getSettings().getValue();

        userProfilePic.setImageResource(settings.profilePicture);
        userHeading.setText(settings.name);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        joinLabel.setText("Member since " + sdf.format(settings.joinDate));

        BankAccountAdapter bankAdapter = new BankAccountAdapter(getContext());
        for(BankAccount account : settings.accounts){
            bankAdapter.addItem(account);
        }
        accountListView.setAdapter(bankAdapter);
    }
}