/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.directorio_amigos;

/**
 *
 * @author daniel, modified by Julian
 */

public class Friend {

    private final String nombre;
    private final long numero;

    public Friend(String nombre, long numero) {
        this.nombre = nombre;
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public long getNumero() {
        return numero;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + ", Número: " + numero;
    }
}


