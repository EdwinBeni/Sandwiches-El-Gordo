/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sandwicheselgordo.modelo;

public class ProductoIndividual extends ItemVenta {

    private Producto producto;

    public ProductoIndividual(Producto producto) {

        super(
                producto.getCodigo(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.isActivo()
        );

        this.producto = producto;
    }

    @Override
    public int calcularPuntos() {

        if ("SANDWICH".equalsIgnoreCase(
                producto.getTipoProducto())) {

            return 2;
        }

        return 0;
    }

    @Override
    public boolean hayExistencia(int cantidad) {
        return producto.hayExistencia(cantidad);
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
