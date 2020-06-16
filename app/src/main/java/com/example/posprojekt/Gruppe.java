package com.example.posprojekt;

public class Gruppe {


    String guppenName;
    String gruppenPasswort;//nur für Admins

    public Gruppe(String guppenName, String gruppenPasswort) {
        this.guppenName = guppenName;
        this.gruppenPasswort = gruppenPasswort;
    }


    public String getGuppenName() {
        return guppenName;
    }

    public void setGuppenName(String guppenName) {
        this.guppenName = guppenName;
    }

    public String getGruppenPasswort() {
        return gruppenPasswort;
    }

    public void setGruppenPasswort(String gruppenPasswort) {
        this.gruppenPasswort = gruppenPasswort;
    }


}
