package com.example.fitnesshelper.models;

public class WorkoutDetails {
    private String name, repetitionnumber, exercisename;

    public WorkoutDetails() {
    }

    public WorkoutDetails(String name, String repetitionnumber, String exercisename) {
        this.name = name;
        this.repetitionnumber = repetitionnumber;
        this.exercisename = exercisename;
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
