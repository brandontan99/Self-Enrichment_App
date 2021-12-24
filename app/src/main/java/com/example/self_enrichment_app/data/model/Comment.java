package com.example.self_enrichment_app.data.model;

import java.io.Serializable;

public class Comment implements Serializable {
    private String name;
    private String imageURL;
    private String commentMsg;
    private Long createdAt;

    public Comment() {
    }
    public Comment(String commentMsg) {
        this.name = "Brandon";
        this.imageURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcScSc2b1SgH7LH8wFw_vrAX85vVftQ0c8Pc3SxrU71e0Fa2SwXikvhS_LekmWu-pj26CVE&usqp=CAU";
        this.commentMsg = commentMsg;
        this.createdAt = System.currentTimeMillis();
    }
    public Comment(String name, String imageURL, String commentMsg) {
        this.name = name;
        this.imageURL = imageURL;
        this.commentMsg = commentMsg;
        this.createdAt = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCommentMsg() {
        return commentMsg;
    }

    public void setCommentMsg(String commentMsg) {
        this.commentMsg = commentMsg;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
