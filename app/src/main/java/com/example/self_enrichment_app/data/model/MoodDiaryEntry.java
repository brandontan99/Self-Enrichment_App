package com.example.self_enrichment_app.data.model;

import com.google.firebase.firestore.DocumentId;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MoodDiaryEntry {
    private String mood;
    private List<String> reasons;
    private String entryDescription;
    private int sentiment;
    private String createdBy;
    private String createdDate;

    private final String[] positiveMoods = {"happy"};

    public MoodDiaryEntry() {
    }

    public MoodDiaryEntry(String mood, List<String> reasons, String entryDescription, String userId, String createdDate) {
        this.mood = mood;
        this.reasons = reasons;
        this.entryDescription = entryDescription;
        this.sentiment = Arrays.asList(positiveMoods).contains(mood) ? 1:0;
        this.createdBy = userId;
        this.createdDate = createdDate;
    }

    public String getMood() {return mood;}

    public void setMood(String mood) {this.mood = mood;}

    public List<String> getReasons() {return reasons; }

    public void setReasons(List<String> reasons) {this.reasons = reasons; }

    public String getEntryDescription() {return entryDescription; }

    public void setEntryDescription(String entryDescription) {this.entryDescription = entryDescription; }

    public int getSentiment() {return sentiment; }

    public void setSentiment(int sentiment) {this.sentiment = sentiment; }

    public String getCreatedBy() {return createdBy; }

    public void setCreatedBy(String createdBy) {this.createdBy = createdBy; }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "MoodDiaryEntry{" +
                "mood='" + mood + '\'' +
                ", reasons=" + reasons +
                ", entryDescription='" + entryDescription + '\'' +
                ", sentiment=" + sentiment +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }
}

