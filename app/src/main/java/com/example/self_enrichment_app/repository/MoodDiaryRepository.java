package com.example.self_enrichment_app.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.self_enrichment_app.data.model.MoodDiaryEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

public class MoodDiaryRepository {
    FirebaseFirestore firestore;

    public MoodDiaryRepository() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<MoodDiaryEntry> getLiveMoodDiaryEntry(String entryDate){
        MutableLiveData<MoodDiaryEntry> moodDiaryEntry = new MutableLiveData<>();
        DocumentReference docRef = firestore.collection("MoodDiaryEntries").document(entryDate);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                moodDiaryEntry.postValue(value.toObject(MoodDiaryEntry.class));
            }
        });
        return moodDiaryEntry;
    }

    public MoodDiaryEntry getMoodDiaryEntry(String userID, String entryDate){
        final MoodDiaryEntry[] diaryEntry = {new MoodDiaryEntry()};
        String entryID = userID+entryDate;
        DocumentReference docRef = firestore.collection("MoodDiaryEntries")
                                            .document(entryID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    diaryEntry[0] = document.toObject(MoodDiaryEntry.class);
                    if (document.exists()){
                        Log.d("Steven", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("Steven", "No such document");
                    }
                } else{
                    Log.d("Steven", "get failed with ", task.getException());
                }
            }
        });
        return diaryEntry[0];
    }

    public void addNewMoodDiaryEntry(MoodDiaryEntry newEntry){
        String entryID = newEntry.getCreatedBy() + newEntry.getCreatedDate();
        firestore.collection("MoodDiaryEntries")
                .document(entryID).set(newEntry).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Steven", "DocumentSnapshot added with ID: " + entryID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Steven", "Error adding document", e);
            }
        });
    }

    public void updateMoodDiaryEntry(MoodDiaryEntry updateEntry){
        String entryID = updateEntry.getCreatedBy() + updateEntry.getCreatedDate();
        DocumentReference prevEntry = firestore.collection("MoodDiaryEntries").document(entryID);
        prevEntry.set(updateEntry).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Steven", "Successfully updated ID: " + prevEntry.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Steven", "Error updating document", e);
            }
        });
    }

}
