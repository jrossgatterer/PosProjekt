package com.example.posprojekt;

public class Getraenk {

    String name;
    double preis;
    String gruppenName;
    int anzahl;


    public Getraenk(String name, double preis, String gruppenName, int anzahl) {
        this.name = name;
        this.preis = preis;
        this.gruppenName = gruppenName;
        this.anzahl = anzahl;
    }

    public Getraenk(String name, double preis, String gruppenName) {
        this.name = name;
        this.preis = preis;
        this.gruppenName = gruppenName;
    }


    public int getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public String getGruppenName() {
        return gruppenName;
    }

    public void setGruppenName(String gruppenName) {
        this.gruppenName = gruppenName;
    }

    @Override
    public String toString()
    {
        return this.name+" "+this.preis + "€";
    }


    public String GetraenkeNameundAnzahl()
    {
        return getName()+ ":  "+getAnzahl()+"x";
    }
}