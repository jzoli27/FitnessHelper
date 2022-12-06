package com.example.fitnesshelper.models;

public class FitnessMachine {

    private String machineName, imgLink, fmKey, assignedExercise;

    public FitnessMachine() {

    }

    public FitnessMachine(String machineName, String imgLink, String fmKey, String assignedExercise) {
        this.machineName = machineName;
        this.imgLink = imgLink;
        this.fmKey = fmKey;
        this.assignedExercise = assignedExercise;
    }

    public String getAssignedExercise() {
        return assignedExercise;
    }

    public void setAssignedExercise(String assignedExercise) {
        this.assignedExercise = assignedExercise;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getFmKey() {
        return fmKey;
    }

    public void setFmKey(String fmKey) {
        this.fmKey = fmKey;
    }
}
