package com.example.posprojekt;

public class Person {

    String vorname;
    String nachname;
    double guthaben;
    String emailAdresse;
    long telefonNr;


    public Person(String vorname, String nachname, double guthaben, String emailAdresse, long telefonNr) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.guthaben = guthaben;
        this.emailAdresse = emailAdresse;
        this.telefonNr = telefonNr;
    }

    @Override
    public String toString()
    {
        return this.vorname+" "+this.nachname + " "+ this.guthaben+ "€";
    }


    public  String vorundnachname()
    {
        return this.vorname+""+this.nachname;
    }


    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public double getGuthaben() {
        return guthaben;
    }

    public void setGuthaben(double guthaben) {
        this.guthaben = guthaben;
    }

    public String getEmailAdresse() {
        return emailAdresse;
    }

    public void setEmailAdresse(String emailAdresse) {
        this.emailAdresse = emailAdresse;
    }

    public long getTelefonNr() {
        return telefonNr;
    }

    public void setTelefonNr(long telefonNr) {
        this.telefonNr = telefonNr;
    }
}