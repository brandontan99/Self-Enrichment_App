package com.example.self_enrichment_app.data.model;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.List;

public class LessonPost {
    private String lesson;
    private int likeCount;
    private List<Comment> commentList;
    private Long createdAt;
    @DocumentId
    private String lessonPostId;

    public LessonPost() {
    }

    public LessonPost(String lesson) {
        this.lesson = lesson;
        this.likeCount = 0;
        this.commentList = null;
        this.createdAt = System.currentTimeMillis();
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

    public int getLikeCount() {
        return likeCount;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
}
