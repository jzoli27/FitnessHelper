package com.example.fitnesshelper.models;

public class ExpandedExercise {

    private String exerciseName, exerciseType, description, muscleGroup, icon, exerciseKey, FmName, FmImgLink, FmKey;
    private Boolean selected;
    //private Boolean expanded;

    public ExpandedExercise() {

    }

    public ExpandedExercise(String exerciseName, String exerciseType, String description, String muscleGroup, String icon, String exerciseKey, Boolean selected,
                            String FmName, String FmImgLink, String FmKey) {
        this.exerciseName = exerciseName;
        this.exerciseType = exerciseType;
        this.description = description;
        this.muscleGroup = muscleGroup;
        this.icon = icon;
        this.exerciseKey = exerciseKey;
        this.selected = selected;
        this.FmName = FmName;
        this.FmImgLink = FmImgLink;
        this.FmKey = FmKey;
        //this.expanded = expanded;
    }
    /*
    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

     */

    public String getFmName() {
        return FmName;
    }

    public void setFmName(String fmName) {
        FmName = fmName;
    }

    public String getFmImgLink() {
        return FmImgLink;
    }

    public void setFmImgLink(String fmImgLink) {
        FmImgLink = fmImgLink;
    }

    public String getFmKey() {
        return FmKey;
    }

    public void setFmKey(String fmKey) {
        FmKey = fmKey;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getExerciseKey() {  return exerciseKey;  }

    public void setExerciseKey(String exerciseKey) { this.exerciseKey = exerciseKey;  }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

}
