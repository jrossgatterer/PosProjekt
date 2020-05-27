package com.example.posprojekt;

public class User {

    String email;
    String passwort;
    String gruppe;
    boolean admin;


    public User(String email, String passwort, String gruppe,boolean admin) {
        this.email = email;
        this.passwort = passwort;
        this.gruppe = gruppe;
        this.admin = admin;
    }
}
