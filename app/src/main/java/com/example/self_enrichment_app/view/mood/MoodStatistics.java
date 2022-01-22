package com.example.self_enrichment_app.view.mood;

import com.example.self_enrichment_app.data.model.MoodDiaryEntry;

import java.util.ArrayList;

public class MoodStatistics {

    private ArrayList<MoodDiaryEntry> allEntries;
    private ArrayList<String> happyReasons;
    private ArrayList<String> sadReasons;
    private ArrayList<String> angryReasons;
    private ArrayList<String> tiredReasons;
    private int totalEntries;
    private int totalPositives;

    public MoodStatistics(ArrayList<MoodDiaryEntry> allEntries) {
        this.allEntries = allEntries;
        this.happyReasons = new ArrayList<>();
        this.sadReasons = new ArrayList<>();
        this.angryReasons = new ArrayList<>();
        this.tiredReasons = new ArrayList<>();
        this.totalEntries = allEntries.size();
        this.totalPositives = getTotalPositives();
        setReasons();
    }

    public int getTotalPositives() {
        int total = 0;
        for (MoodDiaryEntry entry : this.allEntries) {
            total += entry.getSentiment();
        }
        return total;
    }

    public void setReasons() {
        for (MoodDiaryEntry entry: this.allEntries) {
            if (entry.getMood().equalsIgnoreCase("happy")){
                this.happyReasons.addAll(entry.getReasons());
            }
            else if (entry.getMood().equalsIgnoreCase("sad")){
                this.sadReasons.addAll(entry.getReasons());
            }
            else if (entry.getMood().equalsIgnoreCase("angry")){
                this.angryReasons.addAll(entry.getReasons());
            }
            else if (entry.getMood().equalsIgnoreCase("tired")){
                this.tiredReasons.addAll(entry.getReasons());
            }
        }
    }

    public ArrayList<MoodDiaryEntry> getAllEntries() {
        return allEntries;
    }

    public void setAllEntries(ArrayList<MoodDiaryEntry> allEntries) {
        this.allEntries = allEntries;
    }

    public ArrayList<String> getHappyReasons() {
        return happyReasons;
    }

    public void setHappyReasons(ArrayList<String> happyReasons) {
        this.happyReasons = happyReasons;
    }

    public ArrayList<String> getSadReasons() {
        return sadReasons;
    }

    public void setSadReasons(ArrayList<String> sadReasons) {
        this.sadReasons = sadReasons;
    }

    public ArrayList<String> getAngryReasons() {
        return angryReasons;
    }

    public void setAngryReasons(ArrayList<String> angryReasons) {
        this.angryReasons = angryReasons;
    }

    public ArrayList<String> getTiredReasons() {
        return tiredReasons;
    }

    public void setTiredReasons(ArrayList<String> tiredReasons) {
        this.tiredReasons = tiredReasons;
    }

    public int getTotalEntries() {
        return totalEntries;
    }

    public void setTotalEntries(int totalEntries) {
        this.totalEntries = totalEntries;
    }

    public void setTotalPositives(int totalPositives) {
        this.totalPositives = totalPositives;
    }
}
