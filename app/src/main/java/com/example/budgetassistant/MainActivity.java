package com.example.budgetassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavController navController =  Navigation.findNavController(this, R.id.nav_host_fragment);
        setBottomNavMenu(navController);

    }

    private void setBottomNavMenu(NavController navController){
        BottomNavigationView bottomNav = this.findViewById(R.id.BottomNav);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        boolean navigated = NavigationUI.onNavDestinationSelected(item,navController);
        return navigated || super.onOptionsItemSelected(item);
    }
}

