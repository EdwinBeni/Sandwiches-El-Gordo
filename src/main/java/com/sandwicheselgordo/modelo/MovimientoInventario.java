/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sandwicheselgordo.modelo;

import java.time.LocalDateTime;

public class MovimientoInventario {

    private int idMovimiento;
    private Producto producto;
    private LocalDateTime fecha;
    private String tipoMovimiento;
    private int cantidad;
    private int existenciaAnterior;
    private int existenciaNueva;
    private String referencia;

    public MovimientoInventario(
            int idMovimiento,
            Producto producto,
            String tipoMovimiento,
            int cantidad,
            int existenciaAnterior,
            int existenciaNueva,
            String referencia) {

        this.idMovimiento = idMovimiento;
        this.producto = producto;
        this.fecha = LocalDateTime.now();
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.existenciaAnterior = existenciaAnterior;
        this.existenciaNueva = existenciaNueva;
        this.referencia = referencia;
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public Producto getProducto() {
        return producto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getExistenciaAnterior() {
        return existenciaAnterior;
    }

    public int getExistenciaNueva() {
        return existenciaNueva;
    }

    public String getReferencia() {
        return referencia;
    }
}
