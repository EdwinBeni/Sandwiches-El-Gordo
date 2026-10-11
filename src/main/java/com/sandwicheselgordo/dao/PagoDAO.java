
package com.sandwicheselgordo.dao;

import com.sandwicheselgordo.util.ConexionOracle;
import java.sql.*;

public class PagoDAO {

    public boolean registrarPago(
            int idPedido,
            String metodo,
            double dineroRecibido,
            String referencia,
            boolean tarjetaAprobada) throws SQLException {

        if (!"EFECTIVO".equals(metodo)
                && !"TARJETA".equals(metodo)) {
            throw new IllegalArgumentException(
                    "Metodo de pago no valido.");
        }

        try (Connection cn = ConexionOracle.getConexion()) {

            cn.setAutoCommit(false);

            try {
                double total;
                int idCliente;
                int puntosGenerados;

                String sqlPedido = """
                    SELECT ID_CLIENTE, TOTAL,
                           PUNTOS_GENERADOS, ESTADO
                    FROM PEDIDO
                    WHERE ID_PEDIDO = ?
                    FOR UPDATE
                    """;

                try (PreparedStatement ps =
                        cn.prepareStatement(sqlPedido)) {

                    ps.setInt(1, idPedido);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "Pedido no encontrado.");
                        }

                        if (!"PENDIENTE".equals(
                                rs.getString("ESTADO"))) {
                            throw new SQLException(
                                    "El pedido no esta pendiente.");
                        }

                        total = rs.getDouble("TOTAL");
                        idCliente = rs.getInt("ID_CLIENTE");
                        puntosGenerados =
                                rs.getInt("PUNTOS_GENERADOS");
                    }
                }

                boolean aprobado;

                if ("EFECTIVO".equals(metodo)) {
                    if (dineroRecibido < total) {
                        throw new IllegalArgumentException(
                                "Efectivo insuficiente.");
                    }
                    aprobado = true;
                } else {
                    aprobado = tarjetaAprobada;
                    if (referencia == null
                            || referencia.isBlank()) {
                        throw new IllegalArgumentException(
                                "La tarjeta requiere referencia.");
                    }
                }

                String estadoPago =
                        aprobado ? "APROBADO" : "RECHAZADO";

                double cambio = "EFECTIVO".equals(metodo)
                        ? dineroRecibido - total : 0;

                String sqlPago = """
                    MERGE INTO PAGO P
                    USING (SELECT ? ID_PEDIDO FROM DUAL) X
                    ON (P.ID_PEDIDO = X.ID_PEDIDO)
                    WHEN MATCHED THEN UPDATE SET
                        P.FECHA = SYSTIMESTAMP,
                        P.METODO = ?,
                        P.MONTO = ?,
                        P.DINERO_RECIBIDO = ?,
                        P.CAMBIO = ?,
                        P.REFERENCIA = ?,
                        P.ESTADO = ?
                    WHEN NOT MATCHED THEN INSERT
                        (ID_PEDIDO, FECHA, METODO,
                         MONTO, DINERO_RECIBIDO,
                         CAMBIO, REFERENCIA, ESTADO)
                    VALUES
                        (?, SYSTIMESTAMP, ?, ?, ?, ?, ?, ?)
                    """;

                try (PreparedStatement ps =
                        cn.prepareStatement(sqlPago)) {

                    ps.setInt(1, idPedido);
                    ps.setString(2, metodo);
                    ps.setDouble(3, total);

                    if ("EFECTIVO".equals(metodo)) {
                        ps.setDouble(4, dineroRecibido);
                        ps.setDouble(5, cambio);
                    } else {
                        ps.setNull(4, Types.NUMERIC);
                        ps.setNull(5, Types.NUMERIC);
                    }

                    ps.setString(6, referencia);
                    ps.setString(7, estadoPago);

                    ps.setInt(8, idPedido);
                    ps.setString(9, metodo);
                    ps.setDouble(10, total);

                    if ("EFECTIVO".equals(metodo)) {
                        ps.setDouble(11, dineroRecibido);
                        ps.setDouble(12, cambio);
                    } else {
                        ps.setNull(11, Types.NUMERIC);
                        ps.setNull(12, Types.NUMERIC);
                    }

                    ps.setString(13, referencia);
                    ps.setString(14, estadoPago);
                    ps.executeUpdate();
                }

                if (!aprobado) {
                    cn.commit();
                    return false;
                }

                descontarInventario(cn, idPedido);

                String sqlActualizarPedido = """
                    UPDATE PEDIDO
                    SET ESTADO = 'PAGADO'
                    WHERE ID_PEDIDO = ?
                      AND ESTADO = 'PENDIENTE'
                    """;

                try (PreparedStatement ps =
                        cn.prepareStatement(sqlActualizarPedido)) {
                    ps.setInt(1, idPedido);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "No se pudo actualizar el pedido.");
                    }
                }

                if (puntosGenerados > 0) {
                    String sqlPuntos = """
                        UPDATE CLIENTE
                        SET PUNTOS = PUNTOS + ?
                        WHERE ID_CLIENTE = ?
                          AND ESTADO = 'A'
                        """;

                    try (PreparedStatement ps =
                            cn.prepareStatement(sqlPuntos)) {
                        ps.setInt(1, puntosGenerados);
                        ps.setInt(2, idCliente);

                        if (ps.executeUpdate() != 1) {
                            throw new SQLException(
                                    "Cliente no activo o inexistente.");
                        }
                    }

                    String sqlMovimiento = """
                        INSERT INTO MOVIMIENTO_PUNTOS
                        (ID_CLIENTE, FECHA, TIPO,
                         PUNTOS, REFERENCIA)
                        VALUES
                        (?, SYSTIMESTAMP, 'ACUMULACION',
                         ?, ?)
                        """;

                    try (PreparedStatement ps =
                            cn.prepareStatement(sqlMovimiento)) {
                        ps.setInt(1, idCliente);
                        ps.setInt(2, puntosGenerados);
                        ps.setString(3, "PEDIDO " + idPedido);
                        ps.executeUpdate();
                    }
                }

                cn.commit();
                return true;

            } catch (SQLException | RuntimeException e) {
                try {
                    cn.rollback();
                } catch (SQLException ex) {
                    e.addSuppressed(ex);
                }
                throw e;
            }
        }
    }

    private void descontarInventario(
            Connection cn, int idPedido) throws SQLException {

        String sqlComponentes = """
            SELECT ID_PRODUCTO, SUM(CANTIDAD) AS CANTIDAD
            FROM (
                SELECT D.ID_PRODUCTO, D.CANTIDAD
                FROM DETALLE_PEDIDO D
                WHERE D.ID_PEDIDO = ?
                  AND D.ID_PRODUCTO IS NOT NULL

                UNION ALL

                SELECT M.ID_SANDWICH, D.CANTIDAD
                FROM DETALLE_PEDIDO D
                JOIN MENU M ON M.ID_MENU = D.ID_MENU
                WHERE D.ID_PEDIDO = ?

                UNION ALL

                SELECT M.ID_BEBIDA, D.CANTIDAD
                FROM DETALLE_PEDIDO D
                JOIN MENU M ON M.ID_MENU = D.ID_MENU
                WHERE D.ID_PEDIDO = ?

                UNION ALL

                SELECT M.ID_ACOMPANAMIENTO, D.CANTIDAD
                FROM DETALLE_PEDIDO D
                JOIN MENU M ON M.ID_MENU = D.ID_MENU
                WHERE D.ID_PEDIDO = ?
            )
            GROUP BY ID_PRODUCTO
            ORDER BY ID_PRODUCTO
            """;

        try (PreparedStatement ps =
                cn.prepareStatement(sqlComponentes)) {

            for (int i = 1; i <= 4; i++) {
                ps.setInt(i, idPedido);
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    int idProducto =
                            rs.getInt("ID_PRODUCTO");
                    int cantidad =
                            rs.getInt("CANTIDAD");

                    String sqlActualizar = """
                        UPDATE PRODUCTO
                        SET EXISTENCIA = EXISTENCIA - ?
                        WHERE ID_PRODUCTO = ?
                          AND ESTADO = 'A'
                          AND EXISTENCIA >= ?
                        """;

                    try (PreparedStatement actualizar =
                            cn.prepareStatement(sqlActualizar)) {

                        actualizar.setInt(1, cantidad);
                        actualizar.setInt(2, idProducto);
                        actualizar.setInt(3, cantidad);

                        if (actualizar.executeUpdate() != 1) {
                            throw new SQLException(
                                    "Inventario insuficiente para producto "
                                    + idProducto);
                        }
                    }

                    String sqlMovimiento = """
                        INSERT INTO MOVIMIENTO_INVENTARIO
                        (ID_PRODUCTO, FECHA, TIPO,
                         CANTIDAD, REFERENCIA)
                        VALUES
                        (?, SYSTIMESTAMP, 'VENTA', ?, ?)
                        """;

                    try (PreparedStatement movimiento =
                            cn.prepareStatement(sqlMovimiento)) {

                        movimiento.setInt(1, idProducto);
                        movimiento.setInt(2, cantidad);
                        movimiento.setString(
                                3, "PEDIDO " + idPedido);
                        movimiento.executeUpdate();
                    }
                }
            }
        }
    }
}
