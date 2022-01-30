package com.example.self_enrichment_app.repository;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.data.model.LessonPostNotification;
import com.example.self_enrichment_app.data.model.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LessonsLearntRepository {
    MutableLiveData<List<LessonPost>> lessonPostListMutableLiveData;
    FirebaseFirestore firestore;
    MutableLiveData<LessonPost> lessonLearntMutableLiveData;

    public LessonsLearntRepository() {
        lessonPostListMutableLiveData = new MutableLiveData<>();
        firestore = FirebaseFirestore.getInstance();
        lessonLearntMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<User> getUserMutableLiveData(String userId){
        MutableLiveData<User> user = new MutableLiveData<>();
        firestore.collection("Users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException error) {
                if (doc !=null){
                    user.postValue(doc.toObject(User.class));
                }
            }
        });
        return user;
    }
    public MutableLiveData<List<String>> getUsersLikedMutableLiveData(String documentId) {
        MutableLiveData<List<String>> usersLiked = new MutableLiveData<>();
        firestore.collection("LessonPosts").document(documentId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException error) {
                if (doc !=null && doc.toObject(LessonPost.class) != null){
                    usersLiked.postValue(doc.toObject(LessonPost.class).getUsersLiked());
                }
            }
        });
        return usersLiked;
    }

    public void addUserLiked(String userCreatedPostId, String lessonPostId, String userLikedId){
        DocumentReference lessonPostDocRef = firestore.collection("LessonPosts").document(lessonPostId);
        lessonPostDocRef.update("usersLiked", FieldValue.arrayUnion(userLikedId));
        CollectionReference userColRef = firestore.collection("Users").document(userCreatedPostId).collection("Notifications");
        userColRef.add(new LessonPostNotification(userLikedId,lessonPostId,"liked"));
    }
    public void removeUserLiked(String userCreatedPostId, String lessonPostId, String userLikedId){
        DocumentReference lessonPostDocRef = firestore.collection("LessonPosts").document(lessonPostId);
        lessonPostDocRef.update("usersLiked", FieldValue.arrayRemove(userLikedId));
        CollectionReference userColRef = firestore.collection("Users").document(userCreatedPostId).collection("Notifications");
        userColRef.add(new LessonPostNotification(userLikedId,lessonPostId,"unliked"));
    }

    public void addLessonPost(LessonPost newLessonPost){
        firestore.collection("LessonPosts").add(newLessonPost);
    }
    public void addComment(String userCreatedPostId, String documentId, Comment newComment){
        CollectionReference colRef = firestore.collection("LessonPosts").document(documentId).collection("Comments");
        colRef.add(newComment);
        // Update commentCount
        DocumentReference docRef = firestore.collection("LessonPosts").document(documentId);
        docRef.update("commentCount", FieldValue.increment(1));
        CollectionReference userColRef = firestore.collection("Users").document(userCreatedPostId).collection("Notifications");
        userColRef.add(new LessonPostNotification(newComment.getUserId(),documentId,"commented"));
    }
}
