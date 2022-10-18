package com.example.fitnesshelper.models;

public class FitnessMachine {

    private String machineName, imgLink, fmKey;

    public FitnessMachine() {

    }

    public FitnessMachine(String machineName, String imgLink, String fmKey) {
        this.machineName = machineName;
        this.imgLink = imgLink;
        this.fmKey = fmKey;
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
