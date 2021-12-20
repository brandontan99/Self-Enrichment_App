package com.example.self_enrichment_app.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.repository.LessonsLearntRepository;

import java.util.List;

public class LessonsLearntViewModel extends ViewModel {
    MutableLiveData<List<LessonPost>> lessonPostListMutableLiveData;
    LessonsLearntRepository lessonsLearntRepository;

    public LessonsLearntViewModel() {
        lessonsLearntRepository = new LessonsLearntRepository();
        lessonPostListMutableLiveData =  lessonsLearntRepository.getLessonPostListMutableLiveData();
    }

    public MutableLiveData<List<LessonPost>> getLiveLessonPostData() {
        return lessonPostListMutableLiveData;
    }

    public MutableLiveData<List<Comment>> getLiveCommentData(String documentId) {
        return lessonsLearntRepository.getCommentListMutableLiveData(documentId);
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