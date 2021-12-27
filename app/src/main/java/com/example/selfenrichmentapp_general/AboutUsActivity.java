package com.example.selfenrichmentapp_general;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        //Close button
        ImageButton closeBtn = (ImageButton) findViewById(R.id.aboutUsCloseButton);

        View.OnClickListener OCLCloseBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutUsActivity.this, ProfileActivity.class));
                finish();
            }
        };
        closeBtn.setOnClickListener(OCLCloseBtn);
    }
}