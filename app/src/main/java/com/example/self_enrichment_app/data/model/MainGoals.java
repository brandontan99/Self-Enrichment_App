package com.example.self_enrichment_app.data.model;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainGoals {
    private String goal;
    private boolean isCompleted;
    private List<String> subGoals;
    private List<Boolean> subGoalsCompletion;
    private Long createdAt;
    public MainGoals() {
    }

    public MainGoals(String goal){
        this.goal=goal;
        this.isCompleted=false;
        this.subGoals=null;
        this.subGoalsCompletion=null;
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

    public List<String> getSubGoals() {return subGoals;}

    public List<Boolean> getSubGoalsCompletion() {return subGoalsCompletion;}

    /*public void setSubGoals(String subGoal,String newSubGoal){
        int index=this.subGoals.indexOf(subGoal);
        this.subGoals.set(index,newSubGoal);
    }

    public void setSubGoalsCompletion(String subGoal,boolean isCompleted){
        int index=this.subGoalsCompletion.indexOf(subGoal);
        this.subGoalsCompletion.set(index,isCompleted);
    }

    public void addSubGoals(String newSubGoal){
        this.subGoals.add(newSubGoal);
        this.subGoalsCompletion.add(false);
    }

    public void removeSubGoals(String subGoal){
        int index=this.subGoals.indexOf(subGoal);
        this.subGoals.remove(index);
        this.subGoalsCompletion.remove(index);
    }*/

}

