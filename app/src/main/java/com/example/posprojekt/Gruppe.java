package com.example.posprojekt;

public class Gruppe {


    String guppenName;
    String gruppenPasswort;//nur für Admins

    public Gruppe(String guppenName, String gruppenPasswort) {
        this.guppenName = guppenName;
        this.gruppenPasswort = gruppenPasswort;
    }
}
