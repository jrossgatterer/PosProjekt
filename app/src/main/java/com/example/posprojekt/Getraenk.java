package com.example.posprojekt;

public class Getraenk {

    String name;
    double preis;
    String gruppenName;


    public Getraenk(String name, double preis, String gruppenName) {
        this.name = name;
        this.preis = preis;
        this.gruppenName = gruppenName;
    }

    public Getraenk() {
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
}