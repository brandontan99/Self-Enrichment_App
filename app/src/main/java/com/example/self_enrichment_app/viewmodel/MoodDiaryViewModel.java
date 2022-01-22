package com.example.self_enrichment_app.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.self_enrichment_app.data.model.MoodDiaryEntry;
import com.example.self_enrichment_app.repository.MoodDiaryRepository;
import com.google.firebase.firestore.DocumentReference;

public class MoodDiaryViewModel extends ViewModel {
    MoodDiaryRepository moodDiaryRepository;

    public MoodDiaryViewModel() {
        this.moodDiaryRepository = new MoodDiaryRepository();
    }

    public MoodDiaryEntry getMoodDiaryEntry(String userID, String entryDate){
        return moodDiaryRepository.getMoodDiaryEntry(userID, entryDate);
    }

    public void addNewMoodDiaryEntry(MoodDiaryEntry newEntry){
        moodDiaryRepository.addNewMoodDiaryEntry(newEntry);
    }

    public void updateMoodDiaryEntry(MoodDiaryEntry updateEntry){
        moodDiaryRepository.updateMoodDiaryEntry(updateEntry);
    }
}
