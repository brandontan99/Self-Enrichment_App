package com.example.self_enrichment_app.view.general;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.self_enrichment_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ChangePasswordActivity extends AppCompatActivity {

    FirebaseAuth mauth;
    FirebaseFirestore db;
    DocumentReference documentReference;
    FirebaseUser firebaseUser;
    String userID;

    TextView newPassword, confirmNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        newPassword = (TextView) findViewById(R.id.changePassowordEditNewPassword);
        confirmNewPassword = (TextView) findViewById(R.id.changePasswordEditConfirmNewPassword);

        //Initialise auth and firestore
        mauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = mauth.getCurrentUser();
        userID = firebaseUser.getUid();
        documentReference = db.collection("Users").document(userID);


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
                String changeNewPassword = newPassword.getText().toString().trim();
                String confirmChangeNewPassword = confirmNewPassword.getText().toString().trim();

                //Empty check
                if(changeNewPassword.isEmpty()){
                    Toast.makeText(ChangePasswordActivity.this, "Please enter your new password.", Toast.LENGTH_SHORT).show();

                }else if(confirmChangeNewPassword.isEmpty()){
                    Toast.makeText(ChangePasswordActivity.this, "Please enter again your new password.", Toast.LENGTH_SHORT).show();

                }else if(changeNewPassword.equals(confirmChangeNewPassword)){
                    firebaseUser.updatePassword(changeNewPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ChangePasswordActivity.this, "Password updated successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChangePasswordActivity.this, "Password failed to update.", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        };
        doneBtn.setOnClickListener(OCLDoneBtn);
    }

}