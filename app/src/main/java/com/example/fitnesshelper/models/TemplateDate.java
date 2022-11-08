package com.example.fitnesshelper.models;

public class TemplateDate {
    String date;
    String templateName;

    public TemplateDate() {
    }

    public TemplateDate(String date, String templateName) {
        this.date = date;
        this.templateName = templateName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
