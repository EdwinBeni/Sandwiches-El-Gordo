/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sandwicheselgordo.modelo;

public abstract class ItemVenta {

    private String codigo;
    private String nombre;
    private double precio;
    private boolean activo;

    public ItemVenta() {
    }

    public ItemVenta(String codigo, String nombre,
                     double precio, boolean activo) {

        this.codigo = codigo;
        this.nombre = nombre;
        setPrecio(precio);
        this.activo = activo;
    }

    public abstract int calcularPuntos();

    public abstract boolean hayExistencia(int cantidad);

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {

        if (precio < 0) {
            throw new IllegalArgumentException(
                    "El precio no puede ser negativo."
            );
        }

        this.precio = precio;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " - Q" + precio;
    }
}
