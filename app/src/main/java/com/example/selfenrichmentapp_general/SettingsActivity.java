package com.example.selfenrichmentapp_general;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Cancel and close
        ImageButton closeBtn = (ImageButton) findViewById(R.id.settingsCloseButton);

        View.OnClickListener OCLCloseBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
                finish();
            }
        };
        closeBtn.setOnClickListener(OCLCloseBtn);

        //Save and done
        ImageButton doneBtn = (ImageButton) findViewById(R.id.settingsDoneButton);

        View.OnClickListener OCLDoneBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
                finish();
            }
        };
        doneBtn.setOnClickListener(OCLDoneBtn);


        //Change passoword
        ImageButton changePasswordBtn = (ImageButton) findViewById(R.id.settingsChangePasswordButton);

        View.OnClickListener OCLChangePasswordBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ChangePasswordActivity.class));
                finish();
            }
        };
        changePasswordBtn.setOnClickListener(OCLChangePasswordBtn);


    }
}