package com.example.fitnesshelper.models;

public class FitnessMachine {

    private String machineName, imgLink, seatHeight, fmKey;

    public FitnessMachine() {

    }

    public FitnessMachine(String machineName, String imgLink, String seatHeight, String fmKey) {
        this.machineName = machineName;
        this.imgLink = imgLink;
        this.seatHeight = seatHeight;
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

    public String getSeatHeight() {
        return seatHeight;
    }

    public void setSeatHeight(String seatHeight) {
        this.seatHeight = seatHeight;
    }

    public String getFmKey() {
        return fmKey;
    }

    public void setFmKey(String fmKey) {
        this.fmKey = fmKey;
    }
}
