package com.example.self_enrichment_app.data.model;

import com.google.firebase.firestore.DocumentId;

public class User {

    private String fullName, userName, birthdayYear, birthdayMonth, birthdayDay, emailAddress, imageURL;
    private int numGoals;

    public User(){
    }

    public User(String fullName, String userName, String birthdayYear, String birthdayMonth, String birthdayDay,
                String emailAddress) {
        this.fullName = fullName;
        this.userName = userName;
        this.birthdayYear = birthdayYear;
        this.birthdayMonth = birthdayMonth;
        this.birthdayDay = birthdayDay;
        this.emailAddress = emailAddress;
        this.imageURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcScSc2b1SgH7LH8wFw_vrAX85vVftQ0c8Pc3SxrU71e0Fa2SwXikvhS_LekmWu-pj26CVE&usqp=CAU";
        this.numGoals=0;

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBirthdayYear() {
        return birthdayYear;
    }

    public void setBirthdayYear(String birthdayYear) {
        this.birthdayYear = birthdayYear;
    }

    public String getBirthdayMonth() {
        return birthdayMonth;
    }

    public void setBirthdayMonth(String birthdayMonth) {
        this.birthdayMonth = birthdayMonth;
    }

    public String getBirthdayDay() {
        return birthdayDay;
    }

    public void setBirthdayDay(String birthdayDay) {
        this.birthdayDay = birthdayDay;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getNumGoals() {
        return numGoals;
    }

    public void setNumGoals(int numGoals) {
        this.numGoals = numGoals;
    }


    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
