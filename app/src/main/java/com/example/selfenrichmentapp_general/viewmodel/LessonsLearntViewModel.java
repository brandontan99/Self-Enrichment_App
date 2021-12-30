package com.example.selfenrichmentapp_general.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.selfenrichmentapp_general.data.model.Comment;
import com.example.selfenrichmentapp_general.data.model.LessonPost;
import com.example.selfenrichmentapp_general.repository.LessonsLearntRepository;

import java.util.List;

public class LessonsLearntViewModel extends ViewModel {
    MutableLiveData<List<LessonPost>> lessonPostListMutableLiveData;
    LessonsLearntRepository lessonsLearntRepository;

    public LessonsLearntViewModel() {
        lessonsLearntRepository = new LessonsLearntRepository();
    }

    public MutableLiveData<List<LessonPost>> getLiveLessonPostData() {
        return lessonPostListMutableLiveData;
    }

    public MutableLiveData<Integer> getLiveLikeCountData(String documentId) {
        return lessonsLearntRepository.getLikeCountMutableLiveData(documentId);
    }
    public void updateLikeCount(String documentId, int updateCount){
        lessonsLearntRepository.updateLessonPost(documentId, "likeCount", updateCount);
    }

    public void addLessonPost(LessonPost newLessonPost){
        lessonsLearntRepository.addLessonPost(newLessonPost);
    }
    public void addComment(String documentId, Comment newComment){
        lessonsLearntRepository.addComment(documentId,newComment);
    }

}