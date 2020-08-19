package com.example.budgetassistant.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.budgetassistant.models.Account;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.repositories.UserSettingsRepository;

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

    public void postAccount(Account account){
        mSettingsRepo.postAccount(account);
    }
}
