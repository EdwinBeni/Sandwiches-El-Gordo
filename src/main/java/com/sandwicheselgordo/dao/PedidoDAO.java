/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import java.sql.Statement;

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
}