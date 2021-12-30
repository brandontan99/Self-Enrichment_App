package com.example.selfenrichmentapp_general.view.lessons;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class LessonsLearntHelper {
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    public static void deleteLessonPost(String documentId){
        firestore.collection("LessonPost").document(documentId).delete();
    }

    public static void updateLikeCount(String documentId, int updateCount){
        DocumentReference docRef = firestore.collection("LessonPost").document(documentId);
        docRef.update("likeCount", FieldValue.increment(updateCount));
    }

}

