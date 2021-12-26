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

    public MutableLiveData<List<MainGoals>> getMainGoalsListMutableLiveData() {
        firestore.collection("MainGoals").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<MainGoals> mainGoalsList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        mainGoalsList.add(doc.toObject(MainGoals.class));
                    }
                }
                Collections.sort(mainGoalsList, new Comparator<MainGoals>(){

                    public int compare(MainGoals o1, MainGoals o2)
                    {
                        // Descensing order
                        return o2.getCreatedAt().compareTo(o1.getCreatedAt());
                    }
                });
                mainGoalsListMutableLiveData.postValue(mainGoalsList);
            }
        });
        return mainGoalsListMutableLiveData;
    }

    public void updateMainGoals(String documentId, String newMainGoal){
        DocumentReference docRef = firestore.collection("MainGoals").document(documentId);
        docRef.update("goal", newMainGoal);
    }

    public void updateMainGoalsCompletion(String documentId, boolean isCompleted){
        DocumentReference docRef = firestore.collection("MainGoals").document(documentId);
        docRef.update("isCompleted", isCompleted);
    }

    public void updateSubGoals(String documentId, List<String> subGoal){
        DocumentReference docRef = firestore.collection("MainGoals").document(documentId);
        docRef.update("subGoal", subGoal);
    }

    public void updateSubGoalsCompletion(String documentId, List<Boolean> subGoalCompletion){
        DocumentReference docRef = firestore.collection("MainGoals").document(documentId);
        docRef.update("subGoalCompletion", subGoalCompletion);
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
