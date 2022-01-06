package com.example.self_enrichment_app.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.data.model.MainGoals;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GoalsTrackerRepository {
    MutableLiveData<List<MainGoals>> mainGoalsListMutableLiveData;
    FirebaseFirestore firestore;
    //MutableLiveData<MainGoals> mainGoalsMutableLiveData;

    public GoalsTrackerRepository() {
        mainGoalsListMutableLiveData = new MutableLiveData<>();
        firestore = FirebaseFirestore.getInstance();
        //mainGoalsMutableLiveData = new MutableLiveData<>();

    }

    public MutableLiveData<String> getMainGoalsListMutableLiveData(String documentId) {
        MutableLiveData<String> mainGoal = new MutableLiveData<>();
        firestore.collection("MainGoals").document(documentId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException error) {
                mainGoal.postValue(doc.toObject(MainGoals.class).getGoal());
            }
        });
        return mainGoal;
    }

    public DocumentReference getMainGoals(String documentId){
        DocumentReference docRef = firestore.collection("MainGoals").document(documentId);
        return docRef;
    }

    public void updateMainGoals(String documentId, String newMainGoal){
        DocumentReference docRef = firestore.collection("MainGoals").document(documentId);
        docRef.update("goal", newMainGoal);
    }

    public void updateMainGoalsCompletion(String documentId, boolean completed){
        DocumentReference docRef = firestore.collection("MainGoals").document(documentId);
        docRef.update("completed", completed);
    }

    public void updateSubGoals(String documentId, List<String> subGoal){
        DocumentReference docRef = firestore.collection("MainGoals").document(documentId);
        docRef.update("subGoals", subGoal);
    }

    public void updateSubGoalsCompletion(String documentId, List<Boolean> subGoalCompletion){
        DocumentReference docRef = firestore.collection("MainGoals").document(documentId);
        docRef.update("subGoalsCompletion", subGoalCompletion);
    }

    public void addMainGoals(MainGoals newMainGoal){
        firestore.collection("MainGoals").add(newMainGoal);
    }

    public void deleteMainGoals(String documentId){
        firestore.collection("MainGoals").document(documentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Delete", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Delete", "Error deleting document", e);
                    }
                });
    }
}
