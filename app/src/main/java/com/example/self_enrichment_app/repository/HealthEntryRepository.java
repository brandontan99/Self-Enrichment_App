package com.example.self_enrichment_app.repository;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.data.model.HealthEntry;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.data.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

public class HealthEntryRepository {
    MutableLiveData<List<HealthEntry>> healthEntryListMutableLiveData;
    FirebaseFirestore firestore;
    //MutableLiveData<LessonPost> healthEntryMutableLiveData;

    public HealthEntryRepository() {
        healthEntryListMutableLiveData = new MutableLiveData<>();
        firestore = FirebaseFirestore.getInstance();
        //healthEntryMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<String>> getHealthEntriesLiveData(String documentId) {
        MutableLiveData<List<String>> healthEntries = new MutableLiveData<>();
        firestore.collection("HealthEntries").document(documentId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException error) {
                healthEntries.postValue(doc.toObject(LessonPost.class).getUsersLiked());
            }
        });
        return healthEntries;
    }

    public void addUserLiked(String lessonPostId, String userId){
        DocumentReference docRef = firestore.collection("LessonPosts").document(lessonPostId);
        docRef.update("usersLiked", FieldValue.arrayUnion(userId));
    }
    public void removeUserLiked(String lessonPostId, String userId){
        DocumentReference docRef = firestore.collection("LessonPosts").document(lessonPostId);
        docRef.update("usersLiked", FieldValue.arrayRemove(userId));
    }

    public void addLessonPost(LessonPost newLessonPost){
        firestore.collection("LessonPosts").add(newLessonPost);
    }
    public void addComment(String documentId, Comment newComment){
        CollectionReference colRef = firestore.collection("LessonPosts").document(documentId).collection("Comments");
        colRef.add(newComment);
        // Update commentCount
        DocumentReference docRef = firestore.collection("LessonPosts").document(documentId);
        docRef.update("commentCount", FieldValue.increment(1));
    }
}
