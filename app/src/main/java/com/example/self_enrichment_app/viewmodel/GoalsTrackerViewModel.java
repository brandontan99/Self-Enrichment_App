package com.example.self_enrichment_app.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.data.model.MainGoals;
import com.example.self_enrichment_app.repository.GoalsTrackerRepository;
import com.example.self_enrichment_app.repository.LessonsLearntRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class GoalsTrackerViewModel extends ViewModel {
    MutableLiveData<List<MainGoals>> mainGoalsListMutableLiveData;
    GoalsTrackerRepository goalsTrackerRepository;

    public GoalsTrackerViewModel() {
        goalsTrackerRepository = new GoalsTrackerRepository();
    }

    /*public MutableLiveData<List<MainGoals>> getLiveMainGoalData() {
        return mainGoalsListMutableLiveData;
    }*/

    public List<String> getSubGoalData(MainGoals mainGoals) {
        return mainGoals.getSubGoals();
    }

    public void updateMainGoals(String documentId, String newMainGoal){
        goalsTrackerRepository.updateMainGoals(documentId, newMainGoal);
    }

    public void updateMainGoalsCompletion(String documentId, boolean isCompleted){
        goalsTrackerRepository.updateMainGoalsCompletion(documentId, isCompleted);
    }

    public void updateSubGoals(String documentId, List<String> subGoal){
        goalsTrackerRepository.updateSubGoals(documentId, subGoal);
    }

    public void updateSubGoalsCompletion(String documentId, List<Boolean> subGoalCompletion){
        goalsTrackerRepository.updateSubGoalsCompletion(documentId, subGoalCompletion);
    }

    public void addMainGoals(MainGoals newMainGoal){
        goalsTrackerRepository.addMainGoals(newMainGoal);
    }

    public void deleteMainGoals(String documentId) {
        goalsTrackerRepository.deleteMainGoals(documentId);
    }
}
