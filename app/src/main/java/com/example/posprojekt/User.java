package com.example.posprojekt;

public class User {

    String email;
    String passwort;
    String gruppe;
    boolean admin;


    public User(String email, String passwort, String gruppe, boolean admin) {
        this.email = email;
        this.passwort = passwort;
        this.gruppe = gruppe;
        this.admin = admin;
    }


    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getGruppe() {
        return gruppe;
    }

    public void setGruppe(String gruppe) {
        this.gruppe = gruppe;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }


}
