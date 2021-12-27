package com.example.selfenrichmentapp_general;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //Cancel and close
        ImageButton closeBtn = (ImageButton) findViewById(R.id.changePasswordCloseButton);

        View.OnClickListener OCLCloseBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangePasswordActivity.this, SettingsActivity.class));
                finish();
            }
        };
        closeBtn.setOnClickListener(OCLCloseBtn);

        //Save and done
        ImageButton doneBtn = (ImageButton) findViewById(R.id.changePasswordDoneButton);

        View.OnClickListener OCLDoneBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangePasswordActivity.this, SettingsActivity.class));
                finish();
            }
        };
        doneBtn.setOnClickListener(OCLDoneBtn);
    }
}