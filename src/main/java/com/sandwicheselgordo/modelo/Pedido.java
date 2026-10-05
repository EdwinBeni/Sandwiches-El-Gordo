/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sandwicheselgordo.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Pedido {

    private int idPedido;
    private Cliente cliente;
    private LocalDateTime fechaCreacion;

    private ArrayList<DetallePedido> detalles;

    private double descuento;
    private String estado;

    public Pedido(int idPedido, Cliente cliente) {

        if (cliente == null) {
            throw new IllegalArgumentException(
                    "El pedido debe tener un cliente."
            );
        }

        this.idPedido = idPedido;
        this.cliente = cliente;
        this.fechaCreacion = LocalDateTime.now();
        this.detalles = new ArrayList<>();
        this.descuento = 0;
        this.estado = "PENDIENTE";
    }

    public void agregarDetalle(DetallePedido detalle) {

        if (detalle == null) {
            throw new IllegalArgumentException(
                    "El detalle no puede ser nulo."
            );
        }

        if (!detalle.getItem()
                .hayExistencia(detalle.getCantidad())) {

            throw new IllegalArgumentException(
                    "No existe inventario suficiente."
            );
        }

        detalles.add(detalle);
    }

    public void eliminarDetalle(int posicion) {

        if (posicion < 0 || posicion >= detalles.size()) {
            throw new IndexOutOfBoundsException(
                    "Detalle inexistente."
            );
        }

        detalles.remove(posicion);
    }

    public double calcularSubtotal() {

        double subtotal = 0;

        for (DetallePedido detalle : detalles) {
            subtotal += detalle.calcularSubtotal();
        }

        return subtotal;
    }

    public double calcularTotal() {

        double total = calcularSubtotal() - descuento;

        return Math.max(total, 0);
    }

    public int calcularPuntos() {

        int puntos = 0;

        for (DetallePedido detalle : detalles) {
            puntos += detalle.calcularPuntos();
        }

        return puntos;
    }

    public void cambiarEstado(String nuevoEstado) {

        if (nuevoEstado == null) {
            throw new IllegalArgumentException(
                    "El estado es obligatorio."
            );
        }

        boolean valido =
                estado.equals("PENDIENTE")
                && (nuevoEstado.equals("PAGADO")
                || nuevoEstado.equals("ANULADO"));

        valido = valido
                || (estado.equals("PAGADO")
                && nuevoEstado.equals("ENTREGADO"));

        if (!valido) {
            throw new IllegalStateException(
                    "Cambio de estado no permitido: "
                    + estado + " -> " + nuevoEstado
            );
        }

        estado = nuevoEstado;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public ArrayList<DetallePedido> getDetalles() {
        return new ArrayList<>(detalles);
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {

        if (descuento < 0) {
            throw new IllegalArgumentException(
                    "El descuento no puede ser negativo."
            );
        }

        this.descuento = descuento;
    }

    public String getEstado() {
        return estado;
    }
}
