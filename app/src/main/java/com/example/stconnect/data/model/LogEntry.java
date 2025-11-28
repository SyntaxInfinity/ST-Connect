package com.example.stconnect.data.model;

public class LogEntry {
    private String id;
    private String userId;
    private String userName;
    private String action;
    private long timestamp;

    public LogEntry() {
        // Constructor vacío requerido por Firebase
    }

    public LogEntry(String userId, String userName, String action, long timestamp) {
        this.userId = userId;
        this.userName = userName;
        this.action = action;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
