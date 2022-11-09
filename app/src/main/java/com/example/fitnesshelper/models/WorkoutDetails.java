package com.example.fitnesshelper.models;

import com.google.firebase.database.core.ThreadInitializer;

import java.io.Serializable;

public class WorkoutDetails implements Serializable {
    private String name, repetitionnumber, exercisename, excKey;

    public WorkoutDetails() {
    }

    public WorkoutDetails(String name, String repetitionnumber, String exercisename, String excKey) {
        this.name = name;
        this.repetitionnumber = repetitionnumber;
        this.exercisename = exercisename;
        this.excKey = excKey;

    }

    public String getExcKey() {
        return excKey;
    }

    public void setExcKey(String excKey) {
        this.excKey = excKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRepetitionnumber() {
        return repetitionnumber;
    }

    public void setRepetitionnumber(String repetitionnumber) {
        this.repetitionnumber = repetitionnumber;
    }

    public String getExercisename() {
        return exercisename;
    }

    public void setExercisename(String exercisename) {
        this.exercisename = exercisename;
    }
}
