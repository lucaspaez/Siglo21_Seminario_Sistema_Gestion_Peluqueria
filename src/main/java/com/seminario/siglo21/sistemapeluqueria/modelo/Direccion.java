package com.seminario.siglo21.sistemapeluqueria.modelo;

public class Direccion {
    
    // Atributos de la clase Direccion
    private int idDireccion;
    private String calle;
    private int numero;
    private int piso;
    private String ciudad;
    private String provincia;
    private String pais;
    private int codigoPostal;

    // Métodos constructores
    public Direccion() {
    }

    public Direccion(int idDireccion, String calle, int numero, int piso, String ciudad, String provincia, String pais, int codigoPostal) {
        this.idDireccion = idDireccion;
        this.calle = calle;
        this.numero = numero;
        this.piso = piso;
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.pais = pais;
        this.codigoPostal = codigoPostal;
    }

    // Métodos getters y setters
    public int getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(int idDireccion) {
        this.idDireccion = idDireccion;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
    
    
}
