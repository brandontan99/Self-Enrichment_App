package com.example.self_enrichment_app.data.model;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.List;

public class LessonPost {
    private String lesson;
    private int commentCount;
    private Long createdAt;
    private List<String> usersLiked;
    @DocumentId
    private String lessonPostId;

    public LessonPost() {
    }

    public LessonPost(String lesson) {
        this.lesson = lesson;
        this.commentCount = 0;
        this.createdAt = System.currentTimeMillis();
        this.usersLiked = new ArrayList<>();
    }

    public List<String> getUsersLiked() {
        return usersLiked;
    }

    public void setUsersLiked(List<String> usersLiked) {
        this.usersLiked = usersLiked;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getLessonPostId() {
        return lessonPostId;
    }

    public void setLessonPostId(String lessonPostId) {
        this.lessonPostId = lessonPostId;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
