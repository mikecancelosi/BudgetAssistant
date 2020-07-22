package com.example.budgetassistant.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.budgetassistant.DateExtensions;
import com.example.budgetassistant.models.Income;
import com.example.budgetassistant.models.Transaction;
import com.example.budgetassistant.models.UserSettings;
import com.example.budgetassistant.repositories.TransactionRepository;
import com.example.budgetassistant.repositories.UserSettingsRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserSettingsViewModel extends ViewModel {

    private MutableLiveData<UserSettings> mSettings;
    private UserSettingsRepository mRepo;


    public void init(){
        if(mSettings != null){
            return;
        }
        mRepo = UserSettingsRepository.getInstance();
        mSettings = mRepo.getSettings();
    }

    public LiveData<UserSettings> getSettings(){
        return mSettings;
    }

    public Date GetNextPaycheckDate(){
        Income income = getSettings().getValue().GetIncome();
        Calendar c = Calendar.getInstance();
        c.setTime(income.LastPaycheck);
        c.add(Calendar.DAY_OF_MONTH, income.PayPeriodInDays);
        return c.getTime();
    }

    public int GetNumberOfDaysToNextPaycheck(){
        Date nextPaycheckDate = GetNextPaycheckDate();
        Calendar c = Calendar.getInstance();
        return DateExtensions.GetDaysBetween(c.getTime(),nextPaycheckDate);
    }
}
