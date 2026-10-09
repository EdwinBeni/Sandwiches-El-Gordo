/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sandwicheselgordo.servicio;

import com.sandwicheselgordo.dao.PedidoDAO;
import com.sandwicheselgordo.modelo.DetallePedido;
import com.sandwicheselgordo.modelo.Pedido;

import java.sql.SQLException;

public class PedidoService {

    private final PedidoDAO pedidoDAO;

    public PedidoService() {
        this.pedidoDAO = new PedidoDAO();
    }

    public int registrarPedido(Pedido pedido) throws SQLException {

        if (pedido == null) {
            throw new IllegalArgumentException(
                    "El pedido es obligatorio."
            );
        }

        if (!"PENDIENTE".equals(pedido.getEstado())) {
            throw new IllegalArgumentException(
                    "Solo se pueden registrar pedidos pendientes."
            );
        }

        if (pedido.getDetalles().isEmpty()) {
            throw new IllegalArgumentException(
                    "El pedido debe tener al menos un producto o menu."
            );
        }

        for (DetallePedido detalle : pedido.getDetalles()) {

            if (!detalle.getItem().hayExistencia(
                    detalle.getCantidad())) {

                throw new IllegalArgumentException(
                        "Inventario insuficiente para un detalle."
                );
            }
        }

        if (pedido.calcularTotal() < 0) {
            throw new IllegalArgumentException(
                    "El total no puede ser negativo."
            );
        }

        return pedidoDAO.guardar(pedido);
    }
}
