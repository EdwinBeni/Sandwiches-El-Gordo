
package com.sandwicheselgordo.servicio;

import com.sandwicheselgordo.dao.PedidoDAO;
import com.sandwicheselgordo.modelo.DetallePedido;
import com.sandwicheselgordo.modelo.Pedido;

import java.sql.SQLException;
import java.util.List;

public class PedidoService {

    private final PedidoDAO pedidoDAO;

    public PedidoService() {
        this.pedidoDAO = new PedidoDAO();
    }

    public int registrarPedido(Pedido pedido)
            throws SQLException {

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

    public List<String[]> listarPedidos()
            throws SQLException {

        return pedidoDAO.listarPedidos();
    }

    public boolean entregarPedido(int idPedido)
            throws SQLException {

        validarIdPedido(idPedido);

        return pedidoDAO.marcarEntregado(idPedido);
    }

    public boolean anularPedido(int idPedido)
            throws SQLException {

        validarIdPedido(idPedido);

        return pedidoDAO.anularPendiente(idPedido);
    }

    private void validarIdPedido(int idPedido) {

        if (idPedido <= 0) {
            throw new IllegalArgumentException(
                    "El ID del pedido debe ser mayor que cero."
            );
        }
    }
}
