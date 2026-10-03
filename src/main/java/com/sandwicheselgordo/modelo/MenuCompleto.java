/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sandwicheselgordo.modelo;

public class MenuCompleto extends ItemVenta {

    private int idMenu;

    private Producto sandwich;
    private Producto bebida;
    private Producto acompanamiento;

    public MenuCompleto() {
    }

    public MenuCompleto(int idMenu,
                        String codigo,
                        String nombre,
                        double precio,
                        boolean activo,
                        Producto sandwich,
                        Producto bebida,
                        Producto acompanamiento) {

        super(codigo, nombre, precio, activo);

        this.idMenu = idMenu;
        this.sandwich = sandwich;
        this.bebida = bebida;
        this.acompanamiento = acompanamiento;
    }

    @Override
    public int calcularPuntos() {
        return 8;
    }

    @Override
    public boolean hayExistencia(int cantidad) {

        if (cantidad <= 0) {
            return false;
        }

        return sandwich != null
                && bebida != null
                && acompanamiento != null
                && sandwich.hayExistencia(cantidad)
                && bebida.hayExistencia(cantidad)
                && acompanamiento.hayExistencia(cantidad);
    }

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public Producto getSandwich() {
        return sandwich;
    }

    public void setSandwich(Producto sandwich) {
        this.sandwich = sandwich;
    }

    public Producto getBebida() {
        return bebida;
    }

    public void setBebida(Producto bebida) {
        this.bebida = bebida;
    }

    public Producto getAcompanamiento() {
        return acompanamiento;
    }

    public void setAcompanamiento(Producto acompanamiento) {
        this.acompanamiento = acompanamiento;
    }
}
