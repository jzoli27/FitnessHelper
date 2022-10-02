package com.example.fitnesshelper.models;

public class Machine {
    private int gep_id;
    private String gep_megnevezese;
    private String gep_kepe;

    public Machine(int gep_id, String gep_megnevezese, String gep_kepe) {
        this.gep_id = gep_id;
        this.gep_megnevezese = gep_megnevezese;
        this.gep_kepe = gep_kepe;
    }

    public Machine() {
    }

    public int getGep_id() {
        return gep_id;
    }

    public void setGep_id(int gep_id) {
        this.gep_id = gep_id;
    }

    public String getGep_megnevezese() {
        return gep_megnevezese;
    }

    public void setGep_megnevezese(String gep_megnevezese) {
        this.gep_megnevezese = gep_megnevezese;
    }

    public String getGep_kepe() {
        return gep_kepe;
    }

    public void setGep_kepe(String gep_kepe) {
        this.gep_kepe = gep_kepe;
    }
}
