package com.example.self_enrichment_app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentId;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MoodDiaryEntry implements Parcelable {
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

    protected MoodDiaryEntry(Parcel in) {
        mood = in.readString();
        reasons = in.createStringArrayList();
        entryDescription = in.readString();
        sentiment = in.readInt();
        createdBy = in.readString();
        createdDate = in.readString();
    }

    public static final Creator<MoodDiaryEntry> CREATOR = new Creator<MoodDiaryEntry>() {
        @Override
        public MoodDiaryEntry createFromParcel(Parcel in) {
            return new MoodDiaryEntry(in);
        }

        @Override
        public MoodDiaryEntry[] newArray(int size) {
            return new MoodDiaryEntry[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mood);
        parcel.writeStringList(reasons);
        parcel.writeString(entryDescription);
        parcel.writeInt(sentiment);
        parcel.writeString(createdBy);
        parcel.writeString(createdDate);
    }
}

