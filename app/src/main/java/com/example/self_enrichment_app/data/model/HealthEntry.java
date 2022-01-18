package com.example.self_enrichment_app.data.model;

import androidx.annotation.Nullable;

import com.google.type.Date;

public class HealthEntry {
    private String date;
    private int weight, height, bmi, pulse, sys, dia, steps_count, steps_goals;

    public HealthEntry(String date, @Nullable int weight, @Nullable int height, @Nullable int pulse,
                       @Nullable int sys, @Nullable int dia, @Nullable int steps_count, @Nullable int steps_goals) {
        this.date = date;
        this.weight = weight;
        this.height = height;
        this.bmi = weight/(height*height);
        this.pulse = pulse;
        this.sys = sys;
        this.dia = dia;
        this.steps_count = steps_count;
        this.steps_goals = steps_goals;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public int getBmi() {
        return bmi;
    }

    public void setBmi(int bmi) {
        this.bmi = bmi;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
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

    public int getSteps_count() {
        return steps_count;
    }

    public void setSteps_count(int steps_count) {
        this.steps_count = steps_count;
    }

    public int getSteps_goals() {
        return steps_goals;
    }

    public void setSteps_goals(int steps_goals) {
        this.steps_goals = steps_goals;
    }
}
