
package com.sandwicheselgordo.servicio;

import com.sandwicheselgordo.dao.PagoDAO;
import java.sql.SQLException;

public class PagoService {

    private final PagoDAO pagoDAO;

    public PagoService() {
        this.pagoDAO = new PagoDAO();
    }

    public boolean pagarEfectivo(
            int idPedido,
            double dineroRecibido) throws SQLException {

        if (idPedido <= 0) {
            throw new IllegalArgumentException(
                    "ID de pedido invalido.");
        }

        if (!Double.isFinite(dineroRecibido)
                || dineroRecibido <= 0) {
            throw new IllegalArgumentException(
                    "Cantidad de efectivo invalida.");
        }

        return pagoDAO.registrarPago(
                idPedido, "EFECTIVO",
                dineroRecibido, null, true);
    }

    public boolean pagarTarjeta(
            int idPedido,
            String referencia,
            boolean aprobada) throws SQLException {

        if (idPedido <= 0) {
            throw new IllegalArgumentException(
                    "ID de pedido invalido.");
        }

        if (referencia == null
                || referencia.isBlank()) {
            throw new IllegalArgumentException(
                    "La referencia es obligatoria.");
        }

        return pagoDAO.registrarPago(
                idPedido, "TARJETA",
                0, referencia, aprobada);
    }
}
