package com.example.selfenrichmentapp_general;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.net.URL;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    //reference video
    //https://www.youtube.com/watch?v=e_04jFbbnq4
    //https://www.youtube.com/watch?v=Em31dkFkwV8&t=664s

    FirebaseAuth mauth;
    FirebaseDatabase db;
    DatabaseReference userReference;
    CircleImageView editProfilePicture, editProfilePictureBtn;

    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storageReference;
    private TextView editFullName, editUserName, editBirthdayYear, editBirthdayMonth, editBirthdayDay, editEmailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editProfilePicture = (CircleImageView) findViewById(R.id.editProfilePicture);
        editProfilePictureBtn = (CircleImageView) findViewById(R.id.editProfilePictureButton);

        ImageButton closeBtn = (ImageButton) findViewById(R.id.editProfileCloseButton);
        ImageButton saveBtn = (ImageButton) findViewById(R.id.editProfileDoneButton);

        editFullName = (TextView) findViewById(R.id.editProfileFullName);
        editUserName = (TextView) findViewById(R.id.editProfileUserName);

        editBirthdayYear = (TextView) findViewById(R.id.editProfileBirthdayYear);
        editBirthdayMonth = (TextView) findViewById(R.id.editProfileBirthdayMonth);
        editBirthdayDay = (TextView) findViewById(R.id.editProfileBirthdayDay);

        editEmailAddress = (TextView) findViewById(R.id.editProfileEmailAddress);

        //Initialization of Firebase Auth, Firebase Realtime Databae and Firebase Storage
        mauth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://self-enrichment-app-default-rtdb.asia-southeast1.firebasedatabase.app");
        userReference = db.getReference("User");
        storageReference = FirebaseStorage.getInstance().getReference("Profile Picture");

        //Close Button
        View.OnClickListener OCLCloseBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
            }
        };

        closeBtn.setOnClickListener(OCLCloseBtn);

        //Save Button
        View.OnClickListener OCLSaveBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSave();
                startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
            }
        };

        saveBtn.setOnClickListener(OCLSaveBtn);

        //Change Profile Picture Button (small camera beside profile picture)
        View.OnClickListener OCLEditProfilePictureBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1).start(EditProfileActivity.this);
            }
        };

        editProfilePictureBtn.setOnClickListener(OCLEditProfilePictureBtn);

        getUserInfo();
    }

    //Method to validate and save the changed user information to database
    private void validateAndSave() {

        String fullName = editFullName.getText().toString();
        String userName = editUserName.getText().toString();
        String birthdayYear = editBirthdayYear.getText().toString();
        String birthdayMonth = editBirthdayMonth.getText().toString();
        String birthdayDay = editBirthdayDay.getText().toString();
        String emailAddress = editEmailAddress.getText().toString();

        //Empty check
        if(fullName.isEmpty()){
            Toast.makeText(EditProfileActivity.this, "Please enter your full name.", Toast.LENGTH_SHORT).show();
        } else if(userName.isEmpty()){
            Toast.makeText(EditProfileActivity.this, "Please enter your user name.", Toast.LENGTH_SHORT).show();
        } else if(birthdayYear.isEmpty()){
            Toast.makeText(EditProfileActivity.this, "Please enter your birthday in year.", Toast.LENGTH_SHORT).show();
        } else if(birthdayMonth.isEmpty()){
            Toast.makeText(EditProfileActivity.this, "Please enter your birthday in month.", Toast.LENGTH_SHORT).show();
        }else if(birthdayDay.isEmpty()){
            Toast.makeText(EditProfileActivity.this, "Please enter your birthday in day.", Toast.LENGTH_SHORT).show();
        }else if(emailAddress.isEmpty()){
            Toast.makeText(EditProfileActivity.this, "Please enter your email address.", Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("fullName", fullName);
            userMap.put("userName", userName);
            userMap.put("birthdayYear", birthdayYear);
            userMap.put("birthdayMonth", birthdayMonth);
            userMap.put("birthdayDay", birthdayDay);
            userMap.put("emailAddress", emailAddress);

            //Update the new user information to database
            userReference.child(mauth.getCurrentUser().getUid()).updateChildren(userMap);

            uploadProfileImage();
        }
    }

    //Method to get user information from database to app
    private void getUserInfo() {
        userReference.child(mauth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 0){

                    String fullName = snapshot.child("fullName").getValue().toString();
                    String userName = snapshot.child("userName").getValue().toString();
                    String birthdayYear = snapshot.child("birthdayYear").getValue().toString();
                    String birthdayMonth = snapshot.child("birthdayMonth").getValue().toString();
                    String birthdayDay = snapshot.child("birthdayDay").getValue().toString();
                    String emailAddress = snapshot.child("emailAddress").getValue().toString();

                    editFullName.setText(fullName);
                    editUserName.setText(userName);
                    editBirthdayYear.setText(birthdayYear);
                    editBirthdayMonth.setText(birthdayMonth);
                    editBirthdayDay.setText(birthdayDay);
                    editEmailAddress.setText(emailAddress);

                    if(snapshot.hasChild("image")){
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(editProfilePicture);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Error occurs. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Get and crop from phone
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            editProfilePicture.setImageURI(imageUri);
        }else{
            Toast.makeText(EditProfileActivity.this, "Error, Please Try Again.", Toast.LENGTH_SHORT).show();
        }

    }

    //Method to upload cropped profile picture to firebase storage
    private void uploadProfileImage() {

        if(imageUri != null){
            final StorageReference fileRef = storageReference.child(mauth.getCurrentUser().getUid()+ ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        myUri = downloadUri.toString();

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("image", myUri);

                        userReference.child(mauth.getCurrentUser().getUid()).updateChildren(userMap);
                    }
                }
            });
        }
        else{
            Toast.makeText(EditProfileActivity.this, "Image not selected", Toast.LENGTH_SHORT).show();
        }
    }
}