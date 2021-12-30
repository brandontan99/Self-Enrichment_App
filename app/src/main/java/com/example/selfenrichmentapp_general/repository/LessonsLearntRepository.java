package com.example.selfenrichmentapp_general.repository;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.selfenrichmentapp_general.data.model.Comment;
import com.example.selfenrichmentapp_general.data.model.LessonPost;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

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
