package com.example.self_enrichment_app.view.general;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsActivity extends AppCompatActivity {

    FirebaseAuth mauth;
    FirebaseFirestore db;
    DocumentReference documentReference;
    FirebaseUser firebaseUser;
    String userID;

    TextView accountId, accountName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        accountId = (TextView) findViewById(R.id.settingsAccountID);
        accountName = (TextView) findViewById(R.id.settingsAccount);

        //Initialise auth and firestore
        mauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = mauth.getCurrentUser();
        userID = firebaseUser.getUid();
        documentReference = db.collection("Users").document(userID);

        getUserInfo();

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
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                finish();
            }
        };
        doneBtn.setOnClickListener(OCLDoneBtn);

        SwitchCompat darkThemeSwitch = (SwitchCompat) findViewById(R.id.settingsDarkThemeSwitch);

        darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    darkThemeSwitch.setChecked(true);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    darkThemeSwitch.setChecked(false);
                }
            }
        });

        //Change password
        ImageButton changePasswordBtn = (ImageButton) findViewById(R.id.settingsChangePasswordButton);

        View.OnClickListener OCLChangePasswordBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ChangePasswordActivity.class));
                finish();
            }
        };
        changePasswordBtn.setOnClickListener(OCLChangePasswordBtn);

        //Notification switch
        SharedPreferences sharedPreferences = getSharedPreferences("notification", MODE_PRIVATE);
        SwitchCompat goalsSwitch = (SwitchCompat) findViewById(R.id.settingsGoalsSwitch);
        goalsSwitch.setChecked(sharedPreferences.getBoolean("goals value", false));
        goalsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences.Editor editor = getSharedPreferences("notification", MODE_PRIVATE).edit();
                    editor.putBoolean("goals value",true);
                    editor.apply();
                    goalsSwitch.setChecked(true);
                }else{
                    SharedPreferences.Editor editor = getSharedPreferences("notification", MODE_PRIVATE).edit();
                    editor.putBoolean("goals value",false);
                    editor.apply();
                    goalsSwitch.setChecked(false);
                }
            }
        });

        SwitchCompat lessonLearntSwitch = (SwitchCompat) findViewById(R.id.settingsLessonLearntSwitch);
        lessonLearntSwitch.setChecked(sharedPreferences.getBoolean("lesson learnt value", false));
        lessonLearntSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences.Editor editor = getSharedPreferences("notification", MODE_PRIVATE).edit();
                    editor.putBoolean("lesson learnt value",true);
                    editor.apply();
                    lessonLearntSwitch.setChecked(true);
                }else{
                    SharedPreferences.Editor editor = getSharedPreferences("notification", MODE_PRIVATE).edit();
                    editor.putBoolean("lesson learnt value",false);
                    editor.apply();
                    lessonLearntSwitch.setChecked(false);
                }
            }
        });

        SwitchCompat healthSwitch = (SwitchCompat) findViewById(R.id.settingsHealthSwitch);
        healthSwitch.setChecked(sharedPreferences.getBoolean("health value", false));
        healthSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences.Editor editor = getSharedPreferences("notification", MODE_PRIVATE).edit();
                    editor.putBoolean("health value",true);
                    editor.apply();
                    healthSwitch.setChecked(true);
                }else{
                    SharedPreferences.Editor editor = getSharedPreferences("notification", MODE_PRIVATE).edit();
                    editor.putBoolean("health value",false);
                    editor.apply();
                    healthSwitch.setChecked(false);
                }
            }
        });

        SwitchCompat moodSwitch = (SwitchCompat) findViewById(R.id.settingsMoodSwitch);
        moodSwitch.setChecked(sharedPreferences.getBoolean("mood value", false));
        moodSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SharedPreferences.Editor editor = getSharedPreferences("notification", MODE_PRIVATE).edit();
                    editor.putBoolean("mood value",true);
                    editor.apply();
                    moodSwitch.setChecked(true);
                }else{
                    SharedPreferences.Editor editor = getSharedPreferences("notification", MODE_PRIVATE).edit();
                    editor.putBoolean("mood value",false);
                    editor.apply();
                    moodSwitch.setChecked(false);
                }
            }
        });


    }

    //Get user Id and email address
    private void getUserInfo() {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                accountId.setText(userID);
                accountName.setText(user.getEmailAddress());
            }
        });
    }

}