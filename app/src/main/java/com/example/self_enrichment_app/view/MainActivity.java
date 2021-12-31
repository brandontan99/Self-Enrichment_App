package com.example.self_enrichment_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.view.general.EditProfileActivity;
import com.example.self_enrichment_app.view.general.ProfileActivity;
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
    ImageButton ibtnProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bnvMain = findViewById(R.id.bnvMain);
        tvTitle = findViewById(R.id.tvTitle);
        ibtnProfile = findViewById(R.id.ibtnProfile);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nhfMain);
        NavController navController = host.getNavController();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.destGoals,R.id.destLessons,R.id.destDashboard,R.id.destHealth,R.id.destMood).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bnvMain, navController);
        ibtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                finish();
            }
        });

    }
    public void setToolbarTitle(int stringRes){
        String title = getResources().getString(stringRes);
        tvTitle.setText(title);
    }
}