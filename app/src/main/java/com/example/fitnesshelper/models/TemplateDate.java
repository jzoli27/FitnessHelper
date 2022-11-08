package com.example.fitnesshelper.models;

public class TemplateDate {
    String date;
    String templateName;
    String templateDateKey;

    public TemplateDate() {
    }

    public TemplateDate(String date, String templateName, String templateDateKey) {
        this.date = date;
        this.templateName = templateName;
        this.templateDateKey = templateDateKey;
    }

    public String getTemplateDateKey() {
        return templateDateKey;
    }

    public void setTemplateDateKey(String templateDateKey) {
        this.templateDateKey = templateDateKey;
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
