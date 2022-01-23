package com.example.self_enrichment_app.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.data.model.User;
import com.example.self_enrichment_app.repository.LessonsLearntRepository;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import java.util.List;

public class LessonsLearntViewModel extends ViewModel {
    LessonsLearntRepository lessonsLearntRepository;

    public LessonsLearntViewModel() {
        lessonsLearntRepository = new LessonsLearntRepository();
    }

    public MutableLiveData<User> getLiveUserData(String userId) {
        return lessonsLearntRepository.getUserMutableLiveData(userId);
    }

    public MutableLiveData<List<String>> getLiveUsersLikedData(String documentId) {
        return lessonsLearntRepository.getUsersLikedMutableLiveData(documentId);
    }

    public void addUserLiked(String userCreatedPostId, String lessonPostId, String userId){
        lessonsLearntRepository.addUserLiked(userCreatedPostId,lessonPostId, userId);
    }
    public void removeUserLiked(String userCreatedPostId,String lessonPostId, String userId){
        lessonsLearntRepository.removeUserLiked(userCreatedPostId,lessonPostId, userId);
    }

    public void addLessonPost(LessonPost newLessonPost){
        lessonsLearntRepository.addLessonPost(newLessonPost);
    }
    public void addComment(String userCreatedPostId,String documentId, Comment newComment){
        lessonsLearntRepository.addComment(userCreatedPostId,documentId,newComment);
    }

}