package com.example.budgetassistant.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.repositories.TransactionRepository;
import com.example.budgetassistant.repositories.UserSettingsRepository;

import java.util.List;

public class UserSettingsViewModel extends ViewModel {

    private MutableLiveData<UserSettings> mSettings;
    private UserSettingsRepository mRepo;

    public UserSettingsViewModel(){
        init();
    }

    private void init(){
        if(mSettings != null){
            return;
        }
        mRepo = UserSettingsRepository.getInstance();
        mSettings = mRepo.getSettings();
    }

    public LiveData<UserSettings> getSettings(){
        return mSettings;
    }
}
