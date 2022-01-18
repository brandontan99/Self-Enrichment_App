package com.example.self_enrichment_app.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.data.model.User;
import com.example.self_enrichment_app.repository.HealthEntryRepository;
import com.example.self_enrichment_app.repository.LessonsLearntRepository;

import java.util.List;

public class HealthEntryViewModel {
    HealthEntryRepository healthEntryRepository;

    public HealthEntryViewModel() {
        healthEntryRepository = new HealthEntryRepository();
    }

    public MutableLiveData<User> getLiveUserData(String userId) {
        return healthEntryRepository.getUserMutableLiveData(userId);
    }

    public MutableLiveData<List<String>> getLiveUsersLikedData(String documentId) {
        return healthEntryRepository.getUsersLikedMutableLiveData(documentId);
    }

    public void addUserLiked(String lessonPostId, String userId){
        healthEntryRepository.addUserLiked(lessonPostId, userId);
    }
    public void removeUserLiked(String lessonPostId, String userId){
        healthEntryRepository.removeUserLiked(lessonPostId, userId);
    }

    public void addLessonPost(LessonPost newLessonPost){
        healthEntryRepository.addLessonPost(newLessonPost);
    }
    public void addComment(String documentId, Comment newComment){
        healthEntryRepository.addComment(documentId,newComment);
    }

}
