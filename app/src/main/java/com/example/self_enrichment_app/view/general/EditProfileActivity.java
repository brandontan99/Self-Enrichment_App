package com.example.self_enrichment_app.view.general;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    FirebaseAuth mauth;
    CircleImageView editProfilePicture, editProfilePictureBtn;

    private Uri imageUri;
    private String myUri = "";

    UploadTask uploadTask;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseFirestore db;
    DocumentReference documentReference;
    FirebaseUser firebaseUser;
    String userID;

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

        //Initialization of Firebase Auth, Firebase Firestore Database and Firebase Storage
        mauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = mauth.getCurrentUser();
        userID = firebaseUser.getUid();
        documentReference = db.collection("Users").document(userID);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("Profile Picture");

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

        String fullName = editFullName.getText().toString().trim();
        String userName = editUserName.getText().toString().trim();
        String birthdayYear = editBirthdayYear.getText().toString().trim();
        String birthdayMonth = editBirthdayMonth.getText().toString().trim();
        String birthdayDay = editBirthdayDay.getText().toString().trim();
        String emailAddress = editEmailAddress.getText().toString().trim();

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

            //update new field value to each field
            User user = new User(fullName,userName,birthdayYear,birthdayMonth,birthdayDay,emailAddress);

            uploadProfileImage();

            //Update the new user information to database
            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Update the email address at auth
                    firebaseUser.updateEmail(emailAddress).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EditProfileActivity.this, "Profile updated.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfileActivity.this, "Profile failed to update.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, "Error occurs. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //Method to get user information from database to app
    private void getUserInfo() {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                Picasso.get().load(user.getImageURL()).into(editProfilePicture);
                editUserName.setText(user.getUserName());
                editFullName.setText(user.getFullName());
                editBirthdayYear.setText(user.getBirthdayYear());
                editBirthdayMonth.setText(user.getBirthdayMonth());
                editBirthdayDay.setText(user.getBirthdayDay());
                editEmailAddress.setText(user.getEmailAddress());
            }
        });
    };


    //Get and crop from phone
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            Picasso.get().load(imageUri).into(editProfilePicture);
        }else{
            Toast.makeText(EditProfileActivity.this, "Error, Please Try Again.", Toast.LENGTH_SHORT).show();
        }

    }


    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //Method to upload cropped profile picture to firebase storage
    private void uploadProfileImage() {

        if(imageUri != null){
            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExt(imageUri));
            //final StorageReference fileRef = storageReference.child(mauth.getCurrentUser().getUid() + "jpg");

            uploadTask = fileRef.putFile(imageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        myUri = downloadUri.toString().trim();

                        documentReference.update("imageURL", myUri);
                    }
                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    Toast.makeText(EditProfileActivity.this, "Canceled.", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else{
            Toast.makeText(EditProfileActivity.this, "Image not selected", Toast.LENGTH_SHORT).show();
        }
    }
}