package com.example.fitnesshelper.models;

public class Exercise {
    private String exerciseName, exerciseType, description, muscleGroup, icon, exerciseKey;
    private Boolean selected;
    //private Boolean expanded;

    public Exercise() {

    }

    public Exercise(String exerciseName, String exerciseType, String description, String muscleGroup, String icon, String exerciseKey, Boolean selected) {
        this.exerciseName = exerciseName;
        this.exerciseType = exerciseType;
        this.description = description;
        this.muscleGroup = muscleGroup;
        this.icon = icon;
        this.exerciseKey = exerciseKey;
        this.selected = selected;
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
