package com.example.fitnesshelper.models;

public class Altgyakorlatmodell {
    private int altalanos_gyakorlat_id;
    private String gyakorlat_neve;
    private int ajanlott_mennyiseg;
    private int izomcsoport_id;

    public Altgyakorlatmodell(int altalanos_gyakorlat_id, String gyakorlat_neve, int ajanlott_mennyiseg, int izomcsoport_id) {
        this.altalanos_gyakorlat_id = altalanos_gyakorlat_id;
        this.gyakorlat_neve = gyakorlat_neve;
        this.ajanlott_mennyiseg = ajanlott_mennyiseg;
        this.izomcsoport_id = izomcsoport_id;
    }

    public Altgyakorlatmodell() {
    }

    @Override
    public String toString() {
        return
                gyakorlat_neve ;
    }

    public int getAltalanos_gyakorlat_id() {
        return altalanos_gyakorlat_id;
    }

    public void setAltalanos_gyakorlat_id(int altalanos_gyakorlat_id) {
        this.altalanos_gyakorlat_id = altalanos_gyakorlat_id;
    }

    public String getGyakorlat_neve() {
        return gyakorlat_neve;
    }

    public void setGyakorlat_neve(String gyakorlat_neve) {
        this.gyakorlat_neve = gyakorlat_neve;
    }

    public int getAjanlott_mennyiseg() {
        return ajanlott_mennyiseg;
    }

    public void setAjanlott_mennyiseg(int ajanlott_mennyiseg) {
        this.ajanlott_mennyiseg = ajanlott_mennyiseg;
    }

    public int getIzomcsoport_id() {
        return izomcsoport_id;
    }

    public void setIzomcsoport_id(int izomcsoport_id) {
        this.izomcsoport_id = izomcsoport_id;
    }
}
