package com.example.selfenrichmentapp_general;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.selfenrichmentapp_general.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    //Video for reference
    //https://www.youtube.com/watch?v=fOwh16lzDP8&t=590s
    //https://www.youtube.com/watch?v=nF3rzpy2H-I

    FirebaseAuth mauth;
    FirebaseDatabase db;
    DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView registerFullName = (TextView) findViewById(R.id.editRegisterFullName);
        TextView registerUserName = (TextView) findViewById(R.id.editRegisterUserName);

        TextView registerBirthdayYear = (TextView) findViewById(R.id.editRegisterBirthdayYear);
        TextView registerBirthdayMonth = (TextView) findViewById(R.id.editRegisterBirthdayMonth);
        TextView registerBirthdayDay = (TextView) findViewById(R.id.editRegisterBirthdayDay);

        TextView registerEmailAddress = (TextView) findViewById(R.id.editTextEmailAddress);
        TextView registerPassword = (TextView) findViewById(R.id.editRegisterPassword);

        CheckBox registerCheck = (CheckBox) findViewById(R.id.checkBox);

        //initialise firebase instance (auth, realtime db)
        mauth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://self-enrichment-app-default-rtdb.asia-southeast1.firebasedatabase.app");
        userReference = db.getReference("User");

        //Register button
        Button registerBtn = (Button) findViewById(R.id.buttonRegister);

        View.OnClickListener OCLRegister = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = registerFullName.getText().toString();
                String userName = registerUserName.getText().toString();
                String birthdayYear = registerBirthdayYear.getText().toString();
                String birthdayMonth = registerBirthdayMonth.getText().toString();
                String birthdayDay = registerBirthdayDay.getText().toString();
                String emailAddress = registerEmailAddress.getText().toString();
                String password = registerPassword.getText().toString();

                //check empty
                if(fullName.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please enter your full name.", Toast.LENGTH_SHORT).show();

                }else if(userName.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please enter your user name.", Toast.LENGTH_SHORT).show();

                }else if(birthdayYear.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please enter your birthday in year.", Toast.LENGTH_SHORT).show();

                }else if(birthdayMonth.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please enter your birthday in month.", Toast.LENGTH_SHORT).show();

                }else if(birthdayDay.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please enter your birthday in day.", Toast.LENGTH_SHORT).show();

                }else if(emailAddress.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please enter your email address.", Toast.LENGTH_SHORT).show();

                }else if(password.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please enter your password.", Toast.LENGTH_SHORT).show();

                }else{
                    //Create a new user in auth and create a new dataset to store user information in realtime database
                    mauth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                User user = new User();
                                user.setUserName(registerUserName.getText().toString());
                                user.setFullName(registerFullName.getText().toString());
                                user.setBirthdayYear(registerBirthdayYear.getText().toString());
                                user.setBirthdayMonth(registerBirthdayMonth.getText().toString());
                                user.setBirthdayDay(registerBirthdayDay.getText().toString());
                                user.setEmailAddress(registerEmailAddress.getText().toString());
                                user.setPassword(registerPassword.getText().toString());

                                userReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(RegisterActivity.this, "User Register Sucessfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterActivity.this, "User Register Failed"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    });

                }
            }
        };
        registerBtn.setOnClickListener(OCLRegister);

        //Cancel registration
        ImageButton backToLoginBtn = (ImageButton) findViewById(R.id.registerCloseButton);

        View.OnClickListener OCLBackToLogin = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        };

        backToLoginBtn.setOnClickListener(OCLBackToLogin);


    }
}