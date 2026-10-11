
package com.sandwicheselgordo.servicio;

import com.sandwicheselgordo.util.ConexionOracle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PuntosService {

    public int consultarPuntos(int idCliente)
            throws SQLException {

        if (idCliente <= 0) {
            throw new IllegalArgumentException(
                    "El ID del cliente no es valido."
            );
        }

        String sql = """
            SELECT PUNTOS, ESTADO
            FROM CLIENTE
            WHERE ID_CLIENTE = ?
            """;

        try (Connection cn = ConexionOracle.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    throw new SQLException(
                            "El cliente no existe."
                    );
                }

                if (!"A".equalsIgnoreCase(
                        rs.getString("ESTADO"))) {
                    throw new SQLException(
                            "El cliente esta inactivo."
                    );
                }

                return rs.getInt("PUNTOS");
            }
        }
    }

    public boolean tienePuntosSuficientes(
            int idCliente,
            int puntosNecesarios) throws SQLException {

        if (puntosNecesarios <= 0) {
            throw new IllegalArgumentException(
                    "Los puntos necesarios deben ser mayores que cero."
            );
        }

        return consultarPuntos(idCliente) >= puntosNecesarios;
    }
}
