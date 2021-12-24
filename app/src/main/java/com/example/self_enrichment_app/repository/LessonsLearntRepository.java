package com.example.self_enrichment_app.repository;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.data.model.LessonPost;
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

    public MutableLiveData<List<LessonPost>> getLessonPostListMutableLiveData() {
        firestore.collection("LessonPost").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<LessonPost> lessonPostList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        lessonPostList.add(doc.toObject(LessonPost.class));
                    }
                }
                Collections.sort(lessonPostList, new Comparator<LessonPost>(){

                    public int compare(LessonPost o1, LessonPost o2)
                    {
                        // Descensing order
                        return o2.getCreatedAt().compareTo(o1.getCreatedAt());
                    }
                });
                lessonPostListMutableLiveData.postValue(lessonPostList);
            }
        });
        return lessonPostListMutableLiveData;
    }
    public MutableLiveData<List<Comment>> getCommentListMutableLiveData(String documentId) {
        MutableLiveData<List<Comment>> commentList = new MutableLiveData<>();
        firestore.collection("LessonPost").document(documentId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException error) {
                List<Comment> fBCommentList = doc.toObject(LessonPost.class).getCommentList();
                Collections.sort(fBCommentList, new Comparator<Comment>(){

                    public int compare(Comment o1, Comment o2)
                    {
                        // Descensing order
                        return o2.getCreatedAt().compareTo(o1.getCreatedAt());
                    }
                });
                commentList.postValue(fBCommentList);
            }
        });
        return commentList;
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
        DocumentReference docRef = firestore.collection("LessonPost").document(documentId);
        docRef.update("commentList", FieldValue.arrayUnion(newComment));
    }
}
