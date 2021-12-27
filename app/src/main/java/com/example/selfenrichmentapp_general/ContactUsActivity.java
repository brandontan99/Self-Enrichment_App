package com.example.selfenrichmentapp_general;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        //Close button
        ImageButton closeBtn = (ImageButton) findViewById(R.id.contactUsCloseButton);

        View.OnClickListener OCLCloseBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactUsActivity.this, ProfileActivity.class));
                finish();
            }
        };
        closeBtn.setOnClickListener(OCLCloseBtn);
    }
}