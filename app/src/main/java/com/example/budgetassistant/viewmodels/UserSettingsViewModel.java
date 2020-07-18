package com.example.budgetassistant.viewmodels;

import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.repositories.TransactionRepository;

public class UserSettingsViewModel {

    private static UserSettingsViewModel instance;
    public static UserSettingsViewModel getInstance(){
        if(instance == null){
            instance = new UserSettingsViewModel();
        }
        return instance;
    }

    private UserSettings userSettings;

    public UserSettings GetUserSettings(){
        return userSettings;
    }
}
