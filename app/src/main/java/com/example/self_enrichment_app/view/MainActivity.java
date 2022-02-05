package com.example.self_enrichment_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.User;
import com.example.self_enrichment_app.view.general.ProfileActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private TextView tvTitle, tvSubTitle;
    private BottomNavigationView bnvMain;
    private Toolbar toolbar;
    private CircleImageView ibtnProfile;
    private FirebaseAuth mauth;
    private FirebaseFirestore db;
    private String userID;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialize firebase auth and firestore
        mauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = mauth.getCurrentUser().getUid();
        documentReference = db.collection("Users").document(userID);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bnvMain = findViewById(R.id.bnvMain);
        tvTitle = findViewById(R.id.tvTitle);
        tvSubTitle = findViewById(R.id.tvSubTitle);
        ibtnProfile = findViewById(R.id.ibtnProfile);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nhfMain);
        NavController navController = host.getNavController();
        NavigationUI.setupWithNavController(bnvMain, navController);
        ibtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });
        getUserInfo();

    }
    public void setToolbarTitle(int stringRes){
        String title = getResources().getString(stringRes);
        tvTitle.setText(title);
        tvSubTitle.setVisibility(View.GONE);
    }
    public void setToolbarTitle(String title, String subTitle){
        tvTitle.setText(title);
        tvSubTitle.setText(subTitle);
        tvSubTitle.setVisibility(View.VISIBLE);
    }
    //Get user name and profile picture from database
    private void getUserInfo() {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                String imageURL = user.getImageURL();

                Picasso.get().load(imageURL).into(ibtnProfile);
            }
        });
    }
}