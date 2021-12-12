package com.example.self_enrichment_app.data.model;

public class LessonPost {
    private String lesson;
    private int likeCount;
    private int commentCount;

    public LessonPost(String lesson, int likeCount, int commentCount) {
        this.lesson = lesson;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
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

    public void like() {
        this.likeCount ++;
    }
    public void unlike() {
        this.likeCount --;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
