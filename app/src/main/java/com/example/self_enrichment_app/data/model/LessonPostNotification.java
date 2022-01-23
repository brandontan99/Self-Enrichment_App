package com.example.self_enrichment_app.data.model;

public class LessonPostNotification {
    private String createdBy;
    private String postId;
    private Long createdAt;
    private String action;

    public LessonPostNotification() {
    }

    public LessonPostNotification(String createdBy, String postId, String action) {
        this.createdBy = createdBy;
        this.postId = postId;
        this.createdAt =System.currentTimeMillis();
        this.action = action;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
