package com.example.self_enrichment_app.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.data.model.MainGoals;
import com.example.self_enrichment_app.data.model.SubGoals;
import com.example.self_enrichment_app.repository.GoalsTrackerRepository;
import com.example.self_enrichment_app.repository.LessonsLearntRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class GoalsTrackerViewModel extends ViewModel {
    MutableLiveData<List<MainGoals>> mainGoalsListMutableLiveData;
    GoalsTrackerRepository goalsTrackerRepository;

    public GoalsTrackerViewModel() {
        goalsTrackerRepository = new GoalsTrackerRepository();
    }

    public List<String> getSubGoalData(MainGoals mainGoals) {
        return mainGoals.getSubGoals();
    }

    public void updateMainGoals(String documentId, String newMainGoal){
        goalsTrackerRepository.updateMainGoals(documentId, newMainGoal);
    }

    public void updateMainGoalsCompletion(String documentId, boolean isCompleted, String userId, int numGoals){
        goalsTrackerRepository.updateMainGoalsCompletion(documentId, isCompleted, userId, numGoals);
    }

    public void updateSubGoals(String documentId, List<SubGoals> subGoalsArrayList){
        List<String> subGoals=new ArrayList<>();
        for (int i=0;i<subGoalsArrayList.size();i++){
            subGoals.add(subGoalsArrayList.get(i).getGoal());
        }
        goalsTrackerRepository.updateSubGoals(documentId, subGoals);
    }

    public void updateSubGoalsCompletion(String documentId, List<SubGoals> subGoalsArrayList){
        List<Boolean> subGoalsCompletion=new ArrayList<>();
        for (int i=0;i<subGoalsArrayList.size();i++){
            subGoalsCompletion.add(subGoalsArrayList.get(i).getCompleted());
        }
        goalsTrackerRepository.updateSubGoalsCompletion(documentId, subGoalsCompletion);
    }

    public void addMainGoals(String userId, String newGoal){
        MainGoals newMainGoal=new MainGoals(userId,newGoal);
        goalsTrackerRepository.addMainGoals(newMainGoal);
    }

    public void addSubGoals(String documentId, List<SubGoals> subGoalsArrayList){
        updateSubGoals(documentId,subGoalsArrayList);
        updateSubGoalsCompletion(documentId,subGoalsArrayList);
    }

    public void deleteMainGoals(String documentId) {
        goalsTrackerRepository.deleteMainGoals(documentId);
    }

    public void deleteSubGoals(String documentId, List<SubGoals> subGoalsArrayList){
        updateSubGoals(documentId,subGoalsArrayList);
        updateSubGoalsCompletion(documentId,subGoalsArrayList);
    }
}
