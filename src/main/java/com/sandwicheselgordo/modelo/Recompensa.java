/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sandwicheselgordo.modelo;

public class Recompensa {

    private int idRecompensa;
    private String nombre;
    private String descripcion;
    private int puntosNecesarios;
    private ItemVenta item;
    private boolean activo;

    public Recompensa(int idRecompensa,
                      String nombre,
                      String descripcion,
                      int puntosNecesarios,
                      ItemVenta item,
                      boolean activo) {

        this.idRecompensa = idRecompensa;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.puntosNecesarios = puntosNecesarios;
        this.item = item;
        this.activo = activo;
    }

    public int getIdRecompensa() {
        return idRecompensa;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getPuntosNecesarios() {
        return puntosNecesarios;
    }

    public ItemVenta getItem() {
        return item;
    }

    public boolean isActivo() {
        return activo;
    }
}
