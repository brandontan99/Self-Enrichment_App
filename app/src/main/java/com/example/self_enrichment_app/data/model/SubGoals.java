package com.example.self_enrichment_app.data.model;

import java.util.List;

public class SubGoals {
    private String goal;
    private boolean completed;
    private Long createdAt;
    public SubGoals() {
    }

    public SubGoals(String goal,boolean completed){
        this.goal=goal;
        this.completed=completed;
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

    public boolean getCompleted(){return this.completed;}

    public void setCompleted(boolean completed){this.completed=completed;}

}
