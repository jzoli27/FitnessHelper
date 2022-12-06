package com.example.fitnesshelper.models;

import java.util.Calendar;

public class VitaminDetails {
    int hour, minute, second;
    String message;
    int id;

    public VitaminDetails(int hour, int minute, int second, String message, int id) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.message = message;
        this.id = id;
    }

    public VitaminDetails() {
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
