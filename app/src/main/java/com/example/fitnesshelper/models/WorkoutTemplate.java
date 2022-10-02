package com.example.fitnesshelper.models;

public class WorkoutTemplate {
    private String name, note, expectedTime, muscleGroup, wtKey, templateType;

    public WorkoutTemplate() {

    }

    public WorkoutTemplate(String name, String note, String expectedTime, String muscleGroup, String wtKey, String templateType) {
        this.name = name;
        this.note = note;
        this.expectedTime = expectedTime;
        this.muscleGroup = muscleGroup;
        this.wtKey = wtKey;
        this.templateType = templateType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(String expectedTime) {
        this.expectedTime = expectedTime;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public String getWtKey() {
        return wtKey;
    }

    public void setWtKey(String wtKey) {
        this.wtKey = wtKey;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }
}
