package com.example.fitnesshelper.models;

import java.util.Calendar;

public class Vitamin {
    String date, message;
    Calendar calendar;
    int id;

    public Vitamin() {
    }

    public Vitamin(String date, String message, Calendar calendar, int id) {
        this.date = date;
        this.message = message;
        this.calendar = calendar;
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
