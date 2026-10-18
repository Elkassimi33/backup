package com.chakri.amine;


public class Person {

    String nom;
    String prenom;
    String addr;

    public Person(String nom, String prenom, String addr) {
        this.nom = nom;
        this.prenom = prenom;
        this.addr = addr;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
