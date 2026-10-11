
package com.sandwicheselgordo.dao;

import com.sandwicheselgordo.modelo.DetallePedido;
import com.sandwicheselgordo.modelo.ItemVenta;
import com.sandwicheselgordo.modelo.MenuCompleto;
import com.sandwicheselgordo.modelo.Pedido;
import com.sandwicheselgordo.modelo.ProductoIndividual;
import com.sandwicheselgordo.util.ConexionOracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public int guardar(Pedido pedido) throws SQLException {

        if (pedido == null || pedido.getDetalles().isEmpty()) {
            throw new IllegalArgumentException(
                    "El pedido debe contener detalles."
            );
        }

        String sqlPedido = """
                INSERT INTO PEDIDO
                (ID_CLIENTE, FECHA, ESTADO, TOTAL, PUNTOS_GENERADOS)
                VALUES (?, SYSTIMESTAMP, ?, ?, ?)
                """;

        String sqlDetalle = """
                INSERT INTO DETALLE_PEDIDO
                (ID_PEDIDO, ID_PRODUCTO, ID_MENU,
                 CANTIDAD, PRECIO_UNITARIO, SUBTOTAL, PUNTOS)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection cn = ConexionOracle.getConexion()) {

            cn.setAutoCommit(false);

            try {

                int idPedido;

                try (PreparedStatement ps = cn.prepareStatement(
                        sqlPedido,
                        new String[]{"ID_PEDIDO"})) {

                    ps.setInt(1, pedido.getCliente().getIdCliente());
                    ps.setString(2, pedido.getEstado());
                    ps.setDouble(3, pedido.calcularTotal());
                    ps.setInt(4, pedido.calcularPuntos());

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "No se pudo insertar el pedido."
                        );
                    }

                    try (ResultSet rs = ps.getGeneratedKeys()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                    "Oracle no devolvio el ID del pedido."
                            );
                        }

                        idPedido = rs.getInt(1);
                    }
                }

                for (DetallePedido detalle : pedido.getDetalles()) {

                    ItemVenta item = detalle.getItem();

                    try (PreparedStatement ps =
                            cn.prepareStatement(sqlDetalle)) {

                        ps.setInt(1, idPedido);

                        if (item instanceof ProductoIndividual individual) {

                            ps.setInt(
                                    2,
                                    individual.getProducto().getIdProducto()
                            );

                            ps.setNull(3, java.sql.Types.NUMERIC);

                        } else if (item instanceof MenuCompleto menu) {

                            ps.setNull(2, java.sql.Types.NUMERIC);
                            ps.setInt(3, menu.getIdMenu());

                        } else {

                            throw new IllegalArgumentException(
                                    "Tipo de item no soportado."
                            );
                        }

                        ps.setInt(4, detalle.getCantidad());
                        ps.setDouble(5, detalle.getPrecioUnitario());
                        ps.setDouble(6, detalle.calcularSubtotal());
                        ps.setInt(7, detalle.calcularPuntos());

                        if (ps.executeUpdate() != 1) {
                            throw new SQLException(
                                    "No se pudo insertar un detalle."
                            );
                        }
                    }
                }

                cn.commit();

                return idPedido;

            } catch (SQLException | RuntimeException e) {

                try {
                    cn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;
            }
        }
    }

    public List<String[]> listarPedidos() throws SQLException {

        List<String[]> pedidos = new ArrayList<>();

        String sql = """
                SELECT P.ID_PEDIDO,
                       C.NOMBRE AS CLIENTE,
                       P.FECHA,
                       P.TOTAL,
                       P.ESTADO,
                       P.PUNTOS_GENERADOS
                FROM PEDIDO P
                INNER JOIN CLIENTE C
                    ON P.ID_CLIENTE = C.ID_CLIENTE
                ORDER BY P.ID_PEDIDO DESC
                """;

        try (Connection cn = ConexionOracle.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                pedidos.add(new String[]{
                    String.valueOf(rs.getInt("ID_PEDIDO")),
                    rs.getString("CLIENTE"),
                    rs.getTimestamp("FECHA").toString(),
                    String.valueOf(rs.getDouble("TOTAL")),
                    rs.getString("ESTADO"),
                    String.valueOf(rs.getInt("PUNTOS_GENERADOS"))
                });
            }
        }

        return pedidos;
    }

    public boolean marcarEntregado(int idPedido)
            throws SQLException {

        if (idPedido <= 0) {
            throw new IllegalArgumentException(
                    "ID de pedido invalido."
            );
        }

        String sql = """
                UPDATE PEDIDO
                SET ESTADO = 'ENTREGADO'
                WHERE ID_PEDIDO = ?
                  AND ESTADO = 'PAGADO'
                """;

        try (Connection cn = ConexionOracle.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            return ps.executeUpdate() == 1;
        }
    }

    public boolean anularPendiente(int idPedido)
            throws SQLException {

        if (idPedido <= 0) {
            throw new IllegalArgumentException(
                    "ID de pedido invalido."
            );
        }

        String sql = """
                UPDATE PEDIDO
                SET ESTADO = 'ANULADO'
                WHERE ID_PEDIDO = ?
                  AND ESTADO = 'PENDIENTE'
                """;

        try (Connection cn = ConexionOracle.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            return ps.executeUpdate() == 1;
        }
    }
}
