package com.example.self_enrichment_app.view.lessons;

import androidx.lifecycle.MutableLiveData;

import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.data.model.LessonPostNotification;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class LessonsLearntHelper {
    private static FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    public static void deleteLessonPost(String documentId){
        firestore.collection("LessonPosts").document(documentId).delete();
    }
    public static void addUserLiked(String userCreatedPostId, String lessonPostId, String userLikedId){
        DocumentReference lessonPostDocRef = firestore.collection("LessonPosts").document(lessonPostId);
        lessonPostDocRef.update("usersLiked", FieldValue.arrayUnion(userLikedId));
        CollectionReference userColRef = firestore.collection("Users").document(userCreatedPostId).collection("Notifications");
        userColRef.add(new LessonPostNotification(userLikedId,lessonPostId,"liked"));
    }
    public static void removeUserLiked(String userCreatedPostId, String lessonPostId, String userLikedId){
        DocumentReference lessonPostDocRef = firestore.collection("LessonPosts").document(lessonPostId);
        lessonPostDocRef.update("usersLiked", FieldValue.arrayRemove(userLikedId));
        CollectionReference userColRef = firestore.collection("Users").document(userCreatedPostId).collection("Notifications");
        userColRef.add(new LessonPostNotification(userLikedId,lessonPostId,"unliked"));
    }
}

