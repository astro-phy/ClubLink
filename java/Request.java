package com.example.clublink;

public class Request {

    String title;
    String content;
    String datetime;
    String tags;
    long timestamp;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Request(String title, String content, String datetime, String tags, long timestamp) {
        this.title = title;
        this.content = content;
        this.datetime = datetime;
        this.tags = tags;
        this.timestamp = timestamp;
    }



}