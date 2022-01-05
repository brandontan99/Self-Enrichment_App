package com.example.self_enrichment_app.view.lessons;

import androidx.lifecycle.MutableLiveData;

import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class LessonsLearntHelper {
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    public static void deleteLessonPost(String documentId){
        firestore.collection("LessonPosts").document(documentId).delete();
    }
    public static void addUserLiked(String lessonPostId, String userId){
        DocumentReference docRef = firestore.collection("LessonPosts").document(lessonPostId);
        docRef.update("usersLiked", FieldValue.arrayUnion(userId));
    }
    public static void removeUserLiked(String lessonPostId, String userId){
        DocumentReference docRef = firestore.collection("LessonPosts").document(lessonPostId);
        docRef.update("usersLiked", FieldValue.arrayRemove(userId));
    }
}

