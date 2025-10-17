package com.seminario.siglo21.sistemapeluqueria.modelo;

public class Email {
    
    // Atributos
    private int idEmail;
    private String email;

    public Email() {
    }

    public Email(int idEmail, String email) {
        this.idEmail = idEmail;
        this.email = email;
    }

    public int getIdEmail() {
        return idEmail;
    }

    public void setIdEmail(int idEmail) {
        this.idEmail = idEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
}
