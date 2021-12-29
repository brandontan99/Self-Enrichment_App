package com.example.selfenrichmentapp_general;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    //Reference Video
    //https://www.youtube.com/watch?v=3l_T9FyqCTw

    FirebaseAuth mauth;
    FirebaseFirestore db;
    DocumentReference documentReference;
    String userID;

    CircleImageView ProfilePicture;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button editProfile  = (Button) findViewById(R.id.EditProfileBtn);
        Button settings  = (Button) findViewById(R.id.SettingsBtn);
        Button reviewApp  = (Button) findViewById(R.id.ReviewAppBtn);
        Button contactUs  = (Button) findViewById(R.id.ContactUsBtn);
        Button aboutUs  = (Button) findViewById(R.id.AboutUsBtn);
        Button logOut  = (Button) findViewById(R.id.LogoutBtn);

        ProfilePicture = (CircleImageView) findViewById(R.id.profilePictureView);

        userName = (TextView) findViewById(R.id.profileNameView);



        //Initialize firebase auth and realtime db
        mauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = mauth.getCurrentUser().getUid();
        documentReference = db.collection("Users").document(userID);

        //get username from current user dataset (not used)
        //userName.setText(GlobalVariable.currentUser.getUserName());

        //navigate to edit profile
        View.OnClickListener OCLeditProfile = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
                finish();
            }
        };

        editProfile.setOnClickListener(OCLeditProfile);

        //navigate to settings
        View.OnClickListener OCLSettings = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
                finish();
            }
        };

        settings.setOnClickListener(OCLSettings);

        //navigate to review app
        View.OnClickListener OCLReviewApps = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ReviewAppActivity.class));
                finish();
            }
        };

        reviewApp.setOnClickListener(OCLReviewApps);

        //navigate to contact us
        View.OnClickListener OCLContactUs = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ContactUsActivity.class));
                finish();
            }
        };

        contactUs.setOnClickListener(OCLContactUs);

        ////navigate to about us
        View.OnClickListener OCLAboutUs = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, AboutUsActivity.class));
                finish();
            }
        };

        aboutUs.setOnClickListener(OCLAboutUs);

        //logout button
        View.OnClickListener OCLLogOut = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mauth.signOut();
                logOutUser();
            }
        };

        logOut.setOnClickListener(OCLLogOut);

        getUserInfo();
    }

    private void logOutUser() {
        Intent logOut = new Intent(ProfileActivity.this, LoginActivity.class);
        logOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logOut);
        finish();
    }

    //Get user name and profile picture from database
    private void getUserInfo() {
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String name = task.getResult().getString("User Name");
                    String imageUri = task.getResult().getString("Url");

                    Picasso.get().load(imageUri).into(ProfilePicture);

                    userName.setText(name);

                }else{
                    Toast.makeText(ProfileActivity.this, "No Profile exist", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}