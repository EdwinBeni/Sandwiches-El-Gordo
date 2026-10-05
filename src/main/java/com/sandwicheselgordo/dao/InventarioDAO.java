/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sandwicheselgordo.dao;

import com.sandwicheselgordo.util.ConexionOracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventarioDAO {

    public boolean abastecer(
            int idProducto,
            int cantidad,
            String referencia) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        Connection cn = null;

        try {

            cn = ConexionOracle.getConexion();

            // Iniciamos la transacción
            cn.setAutoCommit(false);

            // 1. Verificar que el producto exista
            // y bloquearlo durante la operación
            String sqlBuscar = """
                    SELECT EXISTENCIA
                    FROM PRODUCTO
                    WHERE ID_PRODUCTO = ?
                    FOR UPDATE
                    """;

            int existenciaActual;

            try (PreparedStatement ps =
                         cn.prepareStatement(sqlBuscar)) {

                ps.setInt(1, idProducto);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {

                        cn.rollback();

                        System.err.println(
                                "Producto no encontrado."
                        );

                        return false;
                    }

                    existenciaActual =
                            rs.getInt("EXISTENCIA");
                }
            }

            // 2. Calcular nueva existencia
            int nuevaExistencia =
                    existenciaActual + cantidad;

            // 3. Actualizar PRODUCTO
            String sqlActualizar = """
                    UPDATE PRODUCTO
                    SET EXISTENCIA = ?
                    WHERE ID_PRODUCTO = ?
                    """;

            try (PreparedStatement ps =
                         cn.prepareStatement(sqlActualizar)) {

                ps.setInt(1, nuevaExistencia);
                ps.setInt(2, idProducto);

                ps.executeUpdate();
            }

            // 4. Registrar movimiento
            String sqlMovimiento = """
                    INSERT INTO MOVIMIENTO_INVENTARIO
                    (
                        ID_PRODUCTO,
                        FECHA,
                        TIPO,
                        CANTIDAD,
                        REFERENCIA
                    )
                    VALUES (?, SYSTIMESTAMP, ?, ?, ?)
                    """;

            try (PreparedStatement ps =
                         cn.prepareStatement(sqlMovimiento)) {

                ps.setInt(1, idProducto);
                ps.setString(2, "ABASTECIMIENTO");
                ps.setInt(3, cantidad);
                ps.setString(4, referencia);

                ps.executeUpdate();
            }

            // 5. Confirmar ambas operaciones
            cn.commit();

            System.out.println(
                    "Inventario actualizado correctamente."
            );

            System.out.println(
                    "Existencia anterior: "
                    + existenciaActual
            );

            System.out.println(
                    "Cantidad agregada: "
                    + cantidad
            );

            System.out.println(
                    "Nueva existencia: "
                    + nuevaExistencia
            );

            return true;

        } catch (SQLException e) {

            // Si algo falla, deshacer todo
            if (cn != null) {

                try {
                    cn.rollback();

                    System.err.println(
                            "Transaccion revertida."
                    );

                } catch (SQLException ex) {

                    System.err.println(
                            "Error en rollback: "
                            + ex.getMessage()
                    );
                }
            }

            System.err.println(
                    "Error al abastecer inventario: "
                    + e.getMessage()
            );

            return false;

        } finally {

            if (cn != null) {

                try {

                    cn.setAutoCommit(true);
                    cn.close();

                } catch (SQLException e) {

                    System.err.println(
                            "Error al cerrar conexion: "
                            + e.getMessage()
                    );
                }
            }
        }
    }
}