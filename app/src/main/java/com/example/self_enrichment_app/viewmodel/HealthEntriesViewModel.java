package com.example.self_enrichment_app.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.data.model.HealthEntry;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.data.model.MainGoals;
import com.example.self_enrichment_app.data.model.User;
import com.example.self_enrichment_app.repository.HealthEntriesRepository;

import java.util.List;

public class HealthEntriesViewModel extends ViewModel {
    MutableLiveData<List<HealthEntry>> HealthEntriesListMutableLiveData;
    HealthEntriesRepository healthEntriesRepository;

    public HealthEntriesViewModel() {
        healthEntriesRepository = new HealthEntriesRepository();
    }

    public void addHealthEntry(HealthEntry newHealthEntry){
        healthEntriesRepository.addHealthEntry(newHealthEntry);
    }
    public void updateHealthEntry(String documentId, int weight, int height,
                                  int sys, int dia, int pulse, int steps_goal){
        healthEntriesRepository.updateHealthEntry(documentId, weight, height, sys, dia, pulse, steps_goal);
    }

    public void updateStepsCount(String documentId, int steps_count){
        healthEntriesRepository.updateStepsCount(documentId, steps_count);
    }


}
