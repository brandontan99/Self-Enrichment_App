package com.example.selfenrichmentapp_general;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    //Reference Video
    //https://www.youtube.com/watch?v=3l_T9FyqCTw

    FirebaseAuth mauth;
    FirebaseDatabase db;
    DatabaseReference userReference;
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
        db = FirebaseDatabase.getInstance("https://self-enrichment-app-default-rtdb.asia-southeast1.firebasedatabase.app");
        userReference = db.getReference().child("User");

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

        getUserInfo();

        //logout button
        View.OnClickListener OCLLogOut = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mauth.signOut();
                logOutUser();
            }
        };

        logOut.setOnClickListener(OCLLogOut);
    }

    private void logOutUser() {
        Intent logOut = new Intent(ProfileActivity.this, LoginActivity.class);
        logOut.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logOut);
        finish();
    }

    //Get user name and profile picture from database
    private void getUserInfo() {
        userReference.child(mauth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 0){
                    String name = snapshot.child("userName").getValue().toString();
                    userName.setText(name);

                    if(snapshot.hasChild("image")){
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(ProfilePicture);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Error occurs. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}