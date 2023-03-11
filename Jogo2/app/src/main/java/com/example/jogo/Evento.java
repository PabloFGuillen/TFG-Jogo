package com.example.jogo;

import java.sql.Date;
import java.sql.Time;

public class Evento {
    private String ciudad;
    private String calle;
    private String localidad;
    private Time hora;
    private Date dia;
    private String nombre;
    private String descripcion;
    private int plazas;
    private String nombreU;
    private byte[] fotoU;
    private byte[] qr;
    private double latitud;
    private double longitud;

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPlazas() {
        return plazas;
    }

    public void setPlazas(int plazas) {
        this.plazas = plazas;
    }

    public String getNombreU() {
        return nombreU;
    }

    public void setNombreU(String nombreU) {
        this.nombreU = nombreU;
    }

    public byte[] getFotoU() {
        return fotoU;
    }

    public void setFotoU(byte[] fotoU) {
        this.fotoU = fotoU;
    }

    public byte[] getQr() {
        return qr;
    }

    public void setQr(byte[] qr) {
        this.qr = qr;
    }

    public Evento(String ciudad, String calle, String localidad, Time hora, Date dia, String nombre, String descripcion, int plazas, String nombreU, byte[] fotoU, byte[] qr) {
        this.ciudad = ciudad;
        this.calle = calle;
        this.localidad = localidad;
        this.hora = hora;
        this.dia = dia;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.plazas = plazas;
        this.nombreU = nombreU;
        this.fotoU = fotoU;
        this.qr = qr;
    }

    public Evento(){

    }
}
