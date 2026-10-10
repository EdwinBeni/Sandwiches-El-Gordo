package com.sandwicheselgordo.dao;

import com.sandwicheselgordo.util.ConexionOracle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CanjeDAO {

    public boolean registrarCanje(int idCliente, int idRecompensa) {

        try (Connection cn = ConexionOracle.getConexion()) {

            cn.setAutoCommit(false);

            try {
                int puntosCliente;
                int puntosNecesarios;
                int idProducto;
                int idMenu;

                String sqlCliente = """
                    SELECT PUNTOS, ESTADO
                    FROM CLIENTE
                    WHERE ID_CLIENTE = ?
                    FOR UPDATE
                    """;

                try (PreparedStatement ps =
                        cn.prepareStatement(sqlCliente)) {

                    ps.setInt(1, idCliente);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException("Cliente no encontrado.");
                        }

                        if (!"A".equals(rs.getString("ESTADO"))) {
                            throw new SQLException("Cliente inactivo.");
                        }

                        puntosCliente = rs.getInt("PUNTOS");
                    }
                }

                String sqlRecompensa = """
                    SELECT PUNTOS_NECESARIOS,
                           ID_PRODUCTO,
                           ID_MENU,
                           ESTADO
                    FROM RECOMPENSA
                    WHERE ID_RECOMPENSA = ?
                    """;

                try (PreparedStatement ps =
                        cn.prepareStatement(sqlRecompensa)) {

                    ps.setInt(1, idRecompensa);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException("Recompensa no encontrada.");
                        }

                        if (!"A".equals(rs.getString("ESTADO"))) {
                            throw new SQLException("Recompensa inactiva.");
                        }

                        puntosNecesarios =
                                rs.getInt("PUNTOS_NECESARIOS");

                        idProducto = rs.getInt("ID_PRODUCTO");

                        if (rs.wasNull()) {
                            idProducto = 0;
                        }

                        idMenu = rs.getInt("ID_MENU");

                        if (rs.wasNull()) {
                            idMenu = 0;
                        }
                    }
                }

                if (puntosCliente < puntosNecesarios) {
                    throw new SQLException("Puntos insuficientes.");
                }

                if (idProducto > 0) {

                    descontarProducto(
                            cn, idProducto, 1,
                            "CANJE RECOMPENSA " + idRecompensa
                    );

                } else if (idMenu > 0) {

                    String sqlMenu = """
                        SELECT ID_SANDWICH,
                               ID_BEBIDA,
                               ID_ACOMPANAMIENTO
                        FROM MENU
                        WHERE ID_MENU = ?
                          AND ESTADO = 'A'
                        """;

                    List<Integer> componentes = new ArrayList<>();

                    try (PreparedStatement ps =
                            cn.prepareStatement(sqlMenu)) {

                        ps.setInt(1, idMenu);

                        try (ResultSet rs = ps.executeQuery()) {

                            if (!rs.next()) {
                                throw new SQLException(
                                    "Menú inexistente o inactivo."
                                );
                            }

                            componentes.add(
                                    rs.getInt("ID_SANDWICH")
                            );

                            componentes.add(
                                    rs.getInt("ID_BEBIDA")
                            );

                            componentes.add(
                                    rs.getInt("ID_ACOMPANAMIENTO")
                            );
                        }
                    }

                    for (int componente : componentes) {
                        descontarProducto(
                                cn, componente, 1,
                                "CANJE MENU " + idMenu
                        );
                    }

                } else {
                    throw new SQLException(
                        "La recompensa no tiene producto ni menú."
                    );
                }

                String sqlPuntos = """
                    UPDATE CLIENTE
                    SET PUNTOS = PUNTOS - ?
                    WHERE ID_CLIENTE = ?
                      AND PUNTOS >= ?
                    """;

                try (PreparedStatement ps =
                        cn.prepareStatement(sqlPuntos)) {

                    ps.setInt(1, puntosNecesarios);
                    ps.setInt(2, idCliente);
                    ps.setInt(3, puntosNecesarios);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                            "No se pudieron descontar puntos."
                        );
                    }
                }

                String sqlCanje = """
                    INSERT INTO CANJE
                    (ID_CLIENTE, ID_RECOMPENSA,
                     FECHA, PUNTOS_UTILIZADOS, ESTADO)
                    VALUES (?, ?, SYSTIMESTAMP, ?, 'REALIZADO')
                    """;

                try (PreparedStatement ps =
                        cn.prepareStatement(sqlCanje)) {

                    ps.setInt(1, idCliente);
                    ps.setInt(2, idRecompensa);
                    ps.setInt(3, puntosNecesarios);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                            "No se pudo registrar el canje."
                        );
                    }
                }

                String sqlMovimiento = """
                    INSERT INTO MOVIMIENTO_PUNTOS
                    (ID_CLIENTE, FECHA, TIPO,
                     PUNTOS, REFERENCIA)
                    VALUES (?, SYSTIMESTAMP, 'CANJE', ?, ?)
                    """;

                try (PreparedStatement ps =
                        cn.prepareStatement(sqlMovimiento)) {

                    ps.setInt(1, idCliente);
                    ps.setInt(2, puntosNecesarios);
                    ps.setString(
                            3, "Recompensa " + idRecompensa
                    );

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                            "No se pudo registrar movimiento de puntos."
                        );
                    }
                }

                cn.commit();

                System.out.println(
                    "Canje realizado correctamente."
                );

                return true;

            } catch (SQLException | RuntimeException e) {

                try {
                    cn.rollback();
                } catch (SQLException ex) {
                    System.err.println(
                        "Error al revertir: " + ex.getMessage()
                    );
                }

                System.err.println(
                    "Error al realizar canje: " + e.getMessage()
                );

                return false;
            }

        } catch (SQLException e) {

            System.err.println(
                "Error de conexión Oracle: " + e.getMessage()
            );

            return false;
        }
    }

    private void descontarProducto(
            Connection cn,
            int idProducto,
            int cantidad,
            String referencia) throws SQLException {

        String sqlActualizar = """
            UPDATE PRODUCTO
            SET EXISTENCIA = EXISTENCIA - ?
            WHERE ID_PRODUCTO = ?
              AND EXISTENCIA >= ?
              AND ESTADO = 'A'
            """;

        try (PreparedStatement ps =
                cn.prepareStatement(sqlActualizar)) {

            ps.setInt(1, cantidad);
            ps.setInt(2, idProducto);
            ps.setInt(3, cantidad);

            if (ps.executeUpdate() != 1) {
                throw new SQLException(
                    "Producto inactivo o sin inventario: "
                    + idProducto
                );
            }
        }

        String sqlMovimiento = """
            INSERT INTO MOVIMIENTO_INVENTARIO
            (ID_PRODUCTO, FECHA, TIPO,
             CANTIDAD, REFERENCIA)
            VALUES (?, SYSTIMESTAMP, 'CANJE', ?, ?)
            """;

        try (PreparedStatement ps =
                cn.prepareStatement(sqlMovimiento)) {

            ps.setInt(1, idProducto);
            ps.setInt(2, cantidad);
            ps.setString(3, referencia);

            if (ps.executeUpdate() != 1) {
                throw new SQLException(
                    "No se pudo registrar movimiento de inventario."
                );
            }
        }
    }
}