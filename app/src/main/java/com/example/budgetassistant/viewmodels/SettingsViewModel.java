package com.example.budgetassistant.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.budgetassistant.models.BankAccount;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.repositories.BankRepository;
import com.example.budgetassistant.repositories.TransactionRepository;
import com.example.budgetassistant.repositories.UserSettingsRepository;

import java.util.List;

public class SettingsViewModel extends ViewModel {
    private MutableLiveData<UserSettings> mSettings;
    public LiveData<UserSettings> getSettings(){return mSettings;}
    private UserSettingsRepository mSettingsRepo;

    public void init(){
        if(mSettings != null){
            return;
        }

        mSettingsRepo = UserSettingsRepository.getInstance();
        mSettings = mSettingsRepo.getSettings();
        mSettingsRepo.getSettings().observeForever(new Observer<UserSettings>() {
            @Override
            public void onChanged(UserSettings settings) {
                mSettings.setValue(settings);
            }
        });
    }
}
