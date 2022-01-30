package com.example.self_enrichment_app.view.general;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.User;
import com.example.self_enrichment_app.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

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

        //Initialize firebase auth and firestore
        mauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = mauth.getCurrentUser().getUid();
        documentReference = db.collection("Users").document(userID);

        //get username from current user dataset (not used)
        //userName.setText(GlobalVariable.currentUser.getUserName());

        View.OnClickListener OCLMainActivity = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        };
        ProfilePicture.setOnClickListener(OCLMainActivity);

        //navigate to edit profile
        View.OnClickListener OCLeditProfile = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        };

        editProfile.setOnClickListener(OCLeditProfile);

        //navigate to settings
        View.OnClickListener OCLSettings = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
            }
        };

        settings.setOnClickListener(OCLSettings);

        //navigate to review app
        View.OnClickListener OCLReviewApps = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ReviewAppActivity.class));
            }
        };

        reviewApp.setOnClickListener(OCLReviewApps);

        //navigate to contact us
        View.OnClickListener OCLContactUs = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ContactUsActivity.class));
            }
        };

        contactUs.setOnClickListener(OCLContactUs);

        ////navigate to about us
        View.OnClickListener OCLAboutUs = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, AboutUsActivity.class));
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        startActivity(logOut);
        finish();
    }

    //Get user name and profile picture from database
    private void getUserInfo() {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                String name = user.getUserName();
                String imageURL = user.getImageURL();

                Picasso.get().load(imageURL).into(ProfilePicture);

                userName.setText(name);
            }
        });
    }
}