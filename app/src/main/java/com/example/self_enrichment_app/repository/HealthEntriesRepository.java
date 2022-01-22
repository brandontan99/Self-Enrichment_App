package com.example.self_enrichment_app.repository;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.self_enrichment_app.data.model.HealthEntry;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

public class HealthEntriesRepository {
    MutableLiveData<List<HealthEntry>> healthEntriesListMutableLiveData;
    FirebaseFirestore firestore;
    //MutableLiveData<LessonPost> healthEntryMutableLiveData;

    public HealthEntriesRepository() {
        healthEntriesListMutableLiveData = new MutableLiveData<>();
        firestore = FirebaseFirestore.getInstance();
        //healthEntryMutableLiveData = new MutableLiveData<>();
    }

    // unused
    public MutableLiveData<String> getHealthEntriesMutableLiveData(String documentId) {
        MutableLiveData<String> healthEntry = new MutableLiveData<>();
        firestore.collection("HealthEntries").document(documentId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException error) {
                healthEntry.postValue(doc.toObject(HealthEntry.class).getDate());
            }
        });
        return healthEntry;
    }

    public DocumentReference getHealthEntry(String documentId){
        DocumentReference docRef = firestore.collection("HealthEntries").document(documentId);
        return docRef;
    }

    public void addHealthEntry(HealthEntry newHealthEntry){
        firestore.collection("HealthEntries").add(newHealthEntry);
    }

    public void updateHealthEntry(String documentId, int weight, int height,
                                  int sys, int dia, int pulse, int steps_goal){
        DocumentReference docRef = firestore.collection("HealthEntries").document(documentId);
        docRef.update("weight", weight);
        docRef.update("height", height);
        docRef.update("sys", sys);
        docRef.update("dia", dia);
        docRef.update("pulse", pulse);
        docRef.update("steps_goal", steps_goal);
    }

    public void updateStepsCount(String documentId, int steps_count){
        DocumentReference docRef = firestore.collection("HealthEntries").document(documentId);
        docRef.update("steps_count", steps_count);
    }


}
