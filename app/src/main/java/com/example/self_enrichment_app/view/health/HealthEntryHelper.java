package com.example.self_enrichment_app.view.health;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class HealthEntryHelper {
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    public static void updateUserLiked(String lessonPostId, String userId){
        DocumentReference docRef = firestore.collection("LessonPosts").document(lessonPostId);
        docRef.update("usersLiked", FieldValue.arrayUnion(userId));
    }
}
