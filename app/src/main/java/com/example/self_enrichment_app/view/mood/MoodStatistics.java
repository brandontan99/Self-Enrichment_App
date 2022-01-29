package com.example.self_enrichment_app.view.mood;

import com.example.self_enrichment_app.data.model.MoodDiaryEntry;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public List<BarEntry> getHappyList() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, Collections.frequency(this.happyReasons, "Work")));
        entries.add(new BarEntry(1f, Collections.frequency(this.happyReasons, "Friends")));
        entries.add(new BarEntry(2f, Collections.frequency(this.happyReasons, "Family")));
        entries.add(new BarEntry(3f, Collections.frequency(this.happyReasons, "Health")));
        entries.add(new BarEntry(4f, Collections.frequency(this.happyReasons, "Finance")));
        entries.add(new BarEntry(5f, Collections.frequency(this.happyReasons, "Love")));
        return entries;
    }

    public List<BarEntry> getSadList() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, Collections.frequency(this.sadReasons, "Work")));
        entries.add(new BarEntry(1f, Collections.frequency(this.sadReasons, "Friends")));
        entries.add(new BarEntry(2f, Collections.frequency(this.sadReasons, "Family")));
        entries.add(new BarEntry(3f, Collections.frequency(this.sadReasons, "Health")));
        entries.add(new BarEntry(4f, Collections.frequency(this.sadReasons, "Finance")));
        entries.add(new BarEntry(5f, Collections.frequency(this.sadReasons, "Love")));
        return entries;
    }

    public List<BarEntry> getAngryList() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, Collections.frequency(this.angryReasons, "Work")));
        entries.add(new BarEntry(1f, Collections.frequency(this.angryReasons, "Friends")));
        entries.add(new BarEntry(2f, Collections.frequency(this.angryReasons, "Family")));
        entries.add(new BarEntry(3f, Collections.frequency(this.angryReasons, "Health")));
        entries.add(new BarEntry(4f, Collections.frequency(this.angryReasons, "Finance")));
        entries.add(new BarEntry(5f, Collections.frequency(this.angryReasons, "Love")));
        return entries;
    }

    public List<BarEntry> getTiredList() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, Collections.frequency(this.tiredReasons, "Work")));
        entries.add(new BarEntry(1f, Collections.frequency(this.tiredReasons, "Friends")));
        entries.add(new BarEntry(2f, Collections.frequency(this.tiredReasons, "Family")));
        entries.add(new BarEntry(3f, Collections.frequency(this.tiredReasons, "Health")));
        entries.add(new BarEntry(4f, Collections.frequency(this.tiredReasons, "Finance")));
        entries.add(new BarEntry(5f, Collections.frequency(this.tiredReasons, "Love")));
        return entries;
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
