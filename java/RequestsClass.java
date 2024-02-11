package com.example.clublink;

public class RequestsClass {




    public RequestsClass(String uid, String image, String name, int role, String title, String priority, String phase, String dateTime) {
        this.uid = uid;
        this.image = image;
        this.name = name;
        this.role = role;
        this.phase = phase;
        this.title = title;
        this.priority = priority;
        this.dateTime = dateTime;
    }



    public String image;
    public String phase;
    public String name;
    public int role;
    public String title;
    public String priority;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String dateTime;
    public String uid;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }


    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

}