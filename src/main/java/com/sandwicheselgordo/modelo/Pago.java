/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sandwicheselgordo.modelo;

import java.time.LocalDateTime;

public class Pago {

    private int idPago;
    private int idPedido;
    private LocalDateTime fechaPago;
    private String metodo;
    private double monto;
    private double dineroRecibido;
    private double cambio;
    private String referencia;
    private String estado;

    public Pago() {
    }

    public Pago(int idPago,
                int idPedido,
                String metodo,
                double monto,
                String estado) {

        this.idPago = idPago;
        this.idPedido = idPedido;
        this.fechaPago = LocalDateTime.now();
        this.metodo = metodo;
        this.monto = monto;
        this.estado = estado;
    }

    public int getIdPago() {
        return idPago;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {

        if (monto < 0) {
            throw new IllegalArgumentException(
                    "El monto no puede ser negativo."
            );
        }

        this.monto = monto;
    }

    public double getDineroRecibido() {
        return dineroRecibido;
    }

    public void setDineroRecibido(double dineroRecibido) {
        this.dineroRecibido = dineroRecibido;
    }

    public double getCambio() {
        return cambio;
    }

    public void setCambio(double cambio) {
        this.cambio = cambio;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
