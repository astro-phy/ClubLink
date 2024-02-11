package com.example.clublink;

public class ApprovalClass {

    public ApprovalClass(String name, String image, String role, String datetime, String content, String approved) {
        this.image = image;
        this.role = role;
        this.datetime = datetime;
        this.content = content;
        this.name = name;
        this.approved = approved;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String image, role, datetime, content, name, approved;





}