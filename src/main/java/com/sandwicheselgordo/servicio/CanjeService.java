
package com.sandwicheselgordo.servicio;

import com.sandwicheselgordo.dao.CanjeDAO;
import com.sandwicheselgordo.dao.RecompensaDAO;

import java.sql.SQLException;
import java.util.List;

public class CanjeService {

    private final CanjeDAO canjeDAO;
    private final RecompensaDAO recompensaDAO;
    private final PuntosService puntosService;

    public CanjeService() {
        this.canjeDAO = new CanjeDAO();
        this.recompensaDAO = new RecompensaDAO();
        this.puntosService = new PuntosService();
    }

    public List<String[]> listarRecompensas()
            throws SQLException {

        return recompensaDAO.listarActivas();
    }

    public int consultarPuntos(int idCliente)
            throws SQLException {

        return puntosService.consultarPuntos(idCliente);
    }

    public boolean realizarCanje(
            int idCliente,
            int idRecompensa) throws SQLException {

        if (idCliente <= 0) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un cliente valido."
            );
        }

        if (idRecompensa <= 0) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una recompensa valida."
            );
        }

        String[] recompensa =
                recompensaDAO.buscarPorId(idRecompensa);

        if (recompensa == null) {
            throw new IllegalArgumentException(
                    "La recompensa no existe."
            );
        }

        if (!"A".equalsIgnoreCase(recompensa[6])) {
            throw new IllegalArgumentException(
                    "La recompensa esta inactiva."
            );
        }

        int puntosNecesarios =
                Integer.parseInt(recompensa[3]);

        if (!puntosService.tienePuntosSuficientes(
                idCliente, puntosNecesarios)) {

            throw new IllegalArgumentException(
                    "Puntos insuficientes. Necesita "
                    + puntosNecesarios + " puntos."
            );
        }

        return canjeDAO.registrarCanje(
                idCliente, idRecompensa);
    }
}
