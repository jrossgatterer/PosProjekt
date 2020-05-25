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



}