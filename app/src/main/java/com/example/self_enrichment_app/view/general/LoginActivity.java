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
    
    GoogleSignInClient mGoogleSignInClient;
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


        //////////////////////////////Google Sign in///////////////////////////////////////
        //Send request to google
        createRequest();

        //OnClickListener for Google Sign In
        findViewById(R.id.googleSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultLauncher.launch(new Intent(mGoogleSignInClient.getSignInIntent()));
            }
        });
    }
    private void createRequest() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))//build/generated/res/google-services/debug/values/values.xml
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    //onActivityResult in firebase documentation has deprecated, this is a new version
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                    , new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent intent = result.getData();
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e);
                }
            }

        }
            });

    //After a user successfully signs in, get an ID token from the GoogleSignInAccount object, exchange it for a Firebase credential,
    // and authenticate with Firebase using the Firebase credential
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                }
            }
        });
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
