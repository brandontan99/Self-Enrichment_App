package com.example.self_enrichment_app.repository;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.data.model.LessonPost;
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


    public MutableLiveData<Integer> getLikeCountMutableLiveData(String documentId) {
        MutableLiveData<Integer> likeCount = new MutableLiveData<>();
        firestore.collection("LessonPost").document(documentId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException error) {
                likeCount.postValue(doc.toObject(LessonPost.class).getLikeCount());
            }
        });
        return likeCount;
    }

    public void updateLessonPost(String documentId, String field, int updateCount){
        // Update an existing document
        DocumentReference docRef = firestore.collection("LessonPost").document(documentId);
        docRef.update(field, FieldValue.increment(updateCount));
    }
    public void addLessonPost(LessonPost newLessonPost){
        firestore.collection("LessonPost").add(newLessonPost);
    }
    public void addComment(String documentId, Comment newComment){
        CollectionReference colRef = firestore.collection("LessonPost").document(documentId).collection("Comment");
        colRef.add(newComment);
        // Update commentCount
        DocumentReference docRef = firestore.collection("LessonPost").document(documentId);
        docRef.update("commentCount", FieldValue.increment(1));
    }
}
