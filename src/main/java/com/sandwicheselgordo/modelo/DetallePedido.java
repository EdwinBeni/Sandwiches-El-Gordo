/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sandwicheselgordo.modelo;

public class DetallePedido {

    private int idDetalle;
    private ItemVenta item;
    private int cantidad;
    private double precioUnitario;

    public DetallePedido(int idDetalle,
                         ItemVenta item,
                         int cantidad) {

        if (item == null) {
            throw new IllegalArgumentException(
                    "El item de venta es obligatorio."
            );
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        this.idDetalle = idDetalle;
        this.item = item;
        this.cantidad = cantidad;
        this.precioUnitario = item.getPrecio();
    }

    public double calcularSubtotal() {
        return precioUnitario * cantidad;
    }

    public int calcularPuntos() {
        return item.calcularPuntos() * cantidad;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public ItemVenta getItem() {
        return item;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }
}
