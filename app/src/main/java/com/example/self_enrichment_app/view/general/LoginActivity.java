package com.example.self_enrichment_app.view.general;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.view.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    //For now only got Google sign-in and custom email sign-in
    //actually I think Facebook sign-in can be optional (see got time or not)
    //Google sign-in code has no problem, but nothing pop out when I click it (need help)
    //In my opinion, maybe my fingerprint got problem or dependencies version (maybe)
    
    //firebase documentation
    //https://firebase.google.com/docs/auth/android/google-signin?authuser=0
    
    //google sign-in video
    //https://www.youtube.com/watch?v=bBJF1M5h_UU
    //https://www.youtube.com/watch?v=gCrVwjh4LiY&t=167s

    //custom register and sign-in video
    //https://www.youtube.com/watch?v=TGjDNNtO_3U
    
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText loginEmailAddress = findViewById(R.id.editTextEmailAddress);
        EditText loginPassword = findViewById(R.id.editTextLoginPassword);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Login button (custom login)
        Button loginBtn = (Button) findViewById(R.id.buttonlogin);

        View.OnClickListener OCLLogin = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = loginEmailAddress.getText().toString();
                String password = loginPassword.getText().toString();

                //Empty check
                if(emailAddress.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter your email address.", Toast.LENGTH_SHORT).show();

                }else if(password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter your password.", Toast.LENGTH_SHORT).show();

                }else{
                    //Sign in with Email and Password
                    mAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Sign In Successful.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }else{
                                Toast.makeText(LoginActivity.this, "Sign In Failed.", Toast.LENGTH_SHORT).show();
                            };
                        };
                    });
                }
            }
        };

        loginBtn.setOnClickListener(OCLLogin);

        //Register using email button (custom register as new user)
        Button registerUsingEmail = (Button) findViewById(R.id.registerUsingEmail);

        View.OnClickListener OCLRegisterUsingEmail = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        };

        registerUsingEmail.setOnClickListener(OCLRegisterUsingEmail);

        //Forget Password
        TextView forgetPasswordBtn = (TextView) findViewById(R.id.LoginForgetPasswordBtn);

        View.OnClickListener OCLForgetPassword = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                finish();
            }
        };

        forgetPasswordBtn.setOnClickListener(OCLForgetPassword);



    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

    }

}
