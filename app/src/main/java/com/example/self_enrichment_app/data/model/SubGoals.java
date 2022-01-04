package com.example.self_enrichment_app.data.model;

import java.util.List;

public class SubGoals {
    private String goal;
    private boolean isCompleted;
    private Long createdAt;
    public SubGoals() {
    }

    public SubGoals(String goal,boolean isCompleted){
        this.goal=goal;
        this.isCompleted=isCompleted;
        this.createdAt = System.currentTimeMillis();
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getGoal(){return this.goal;}

    public void setGoal(String goal){this.goal=goal;}

    public boolean getCompleted(){return this.isCompleted;}

    public void setCompleted(boolean isCompleted){this.isCompleted=isCompleted;}

}
