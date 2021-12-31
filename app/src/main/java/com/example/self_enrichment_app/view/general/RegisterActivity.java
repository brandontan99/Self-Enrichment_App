package com.example.self_enrichment_app.view.general;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.self_enrichment_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    FirebaseAuth mauth;
    FirebaseFirestore db;
    DocumentReference documentReference;
    FirebaseUser firebaseUser;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView registerFullName = (TextView) findViewById(R.id.editRegisterFullName);
        TextView registerUserName = (TextView) findViewById(R.id.editRegisterUserName);

        TextView registerBirthdayYear = (TextView) findViewById(R.id.editRegisterBirthdayYear);
        TextView registerBirthdayMonth = (TextView) findViewById(R.id.editRegisterBirthdayMonth);
        TextView registerBirthdayDay = (TextView) findViewById(R.id.editRegisterBirthdayDay);

        TextView registerEmailAddress = (TextView) findViewById(R.id.editTextRegisterEmailAddress);
        TextView registerPassword = (TextView) findViewById(R.id.editRegisterPassword);

        CheckBox registerCheck = (CheckBox) findViewById(R.id.checkBox);

        //initialise firebase instance (auth, firestore)
        mauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if(mauth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            finish();
        }

        //Register button
        Button registerBtn = (Button) findViewById(R.id.buttonRegister);

        View.OnClickListener OCLRegister = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = registerFullName.getText().toString().trim();
                String userName = registerUserName.getText().toString().trim();
                String birthdayYear = registerBirthdayYear.getText().toString().trim();
                String birthdayMonth = registerBirthdayMonth.getText().toString().trim();
                String birthdayDay = registerBirthdayDay.getText().toString().trim();
                String emailAddress = registerEmailAddress.getText().toString().trim();
                String password = registerPassword.getText().toString().trim();

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
                    //Create a new user in auth and create a new dataset to store user information in firestore database
                    mauth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //Collection: Users, Document:user UID
                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                userID = firebaseUser.getUid();
                                documentReference = db.collection("Users").document(userID);

                                //Add fields and fields value
                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap.put("Full Name", fullName );
                                userMap.put("User Name", userName);
                                userMap.put("Birthday (year)", birthdayYear );
                                userMap.put("Birthday (month)", birthdayMonth);
                                userMap.put("Birthday (day)", birthdayDay);
                                userMap.put("Email Address", emailAddress);
                                userMap.put("Password", password);
                                userMap.put("User ID", userID);

                                documentReference.set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(RegisterActivity.this, "User Register Sucessfully", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterActivity.this, "User Register Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            }else{
                                Toast.makeText(RegisterActivity.this, "User Register Failed", Toast.LENGTH_SHORT).show();
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