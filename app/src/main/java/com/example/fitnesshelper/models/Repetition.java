package com.example.fitnesshelper.models;

public class Repetition {

    private String series, weight, numberOfRepetitions, exerciseName, repetitionKey,fitnessMachineKey;

    public Repetition() {

    }

    public Repetition(String series, String weight, String numberOfRepetitions, String exerciseName, String repetitionKey,String fitnessMachineKey) {
        this.series = series;
        this.weight = weight;
        this.numberOfRepetitions = numberOfRepetitions;
        this.exerciseName = exerciseName;
        this.repetitionKey = repetitionKey;
        this.fitnessMachineKey = fitnessMachineKey;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getNumberOfRepetitions() {
        return numberOfRepetitions;
    }

    public void setNumberOfRepetitions(String numberOfRepetitions) {
        this.numberOfRepetitions = numberOfRepetitions;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getFitnessMachineKey() {
        return fitnessMachineKey;
    }

    public void setFitnessMachineKey(String fitnessMachineKey) {
        this.fitnessMachineKey = fitnessMachineKey;
    }

    public String getRepetitionKey() {
        return repetitionKey;
    }

    public void setRepetitionKey(String repetitionKey) {
        this.repetitionKey = repetitionKey;
    }
}
