package com.example.self_enrichment_app.data.model;

import androidx.annotation.Nullable;

import com.google.type.Date;

public class HealthEntry {
    private String date, userId;
    private int weight, height, sys, dia, pulse, steps_count, steps_goal;

    public HealthEntry(String date, String userId, @Nullable int weight, @Nullable int height,
                       @Nullable int sys, @Nullable int dia, @Nullable int pulse, @Nullable int steps_goal) {
        int steps_count = 0;
        this.date = date;
        this.userId = userId;
        this.weight = weight;
        this.height = height;
        this.sys = sys;
        this.dia = dia;
        this.pulse = pulse;
        this.steps_count = steps_count;
        this.steps_goal = steps_goal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSys() {
        return sys;
    }

    public void setSys(int sys) {
        this.sys = sys;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public int getSteps_count() {
        return steps_count;
    }

    public void setSteps_count(int steps_count) {
        this.steps_count = steps_count;
    }

    public int getSteps_goal() {
        return steps_goal;
    }

    public void setSteps_goal(int steps_goal) {
        this.steps_goal = steps_goal;
    }
}
