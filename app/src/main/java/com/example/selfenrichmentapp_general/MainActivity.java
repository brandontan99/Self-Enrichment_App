package com.example.selfenrichmentapp_general;

import android.os.Bundle;
import android.widget.TextView;

import com.example.selfenrichmentapp_general.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    TextView tvTitle;
    BottomNavigationView bnvMain;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bnvMain = findViewById(R.id.bnvMain);
        tvTitle = findViewById(R.id.tvTitle);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nhfMain);
        NavController navController = host.getNavController();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.destGoals,R.id.destLessons,R.id.destDashboard,R.id.destHealth,R.id.destMood).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bnvMain, navController);
    }
    public void setToolbarTitle(int stringRes){
        String title = getResources().getString(stringRes);
        tvTitle.setText(title);
    }
}