package com.example.jogo;

public class Persona {
    public static String nombreU;
    public Persona(String nombreU){
        this.nombreU =  nombreU;
    }

    public void setNombreU(String nombreU){
        this.nombreU = nombreU;
    }

    public String getNombreU(){
        return this.nombreU;
    }

    public Persona(){

    }
}
