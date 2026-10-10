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

public class ReporteDAO {

    private void ejecutarReporte(String titulo, String sql) {

        System.out.println("\n================================");
        System.out.println(titulo);
        System.out.println("================================");

        try (Connection cn = ConexionOracle.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int columnas = rs.getMetaData().getColumnCount();
            int filas = 0;

            while (rs.next()) {
                filas++;

                for (int i = 1; i <= columnas; i++) {
                    String columna =
                            rs.getMetaData().getColumnLabel(i);

                    Object valor = rs.getObject(i);

                    System.out.print(
                            columna + ": " + valor + " | "
                    );
                }

                System.out.println();
            }

            if (filas == 0) {
                System.out.println("Sin registros.");
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error en reporte: " + e.getMessage()
            );
        }
    }

    public void reportePuntosClientes() {

        String sql = """
            SELECT CODIGO_CLIENTE,
                   NOMBRE,
                   PUNTOS,
                   ESTADO
            FROM CLIENTE
            ORDER BY PUNTOS DESC
            """;

        ejecutarReporte("PUNTOS DE CLIENTES", sql);
    }

    public void reporteHistorialCanjes() {

        String sql = """
            SELECT C.ID_CANJE,
                   CL.NOMBRE AS CLIENTE,
                   R.NOMBRE AS RECOMPENSA,
                   C.FECHA,
                   C.PUNTOS_UTILIZADOS,
                   C.ESTADO
            FROM CANJE C
            JOIN CLIENTE CL
              ON C.ID_CLIENTE = CL.ID_CLIENTE
            JOIN RECOMPENSA R
              ON C.ID_RECOMPENSA = R.ID_RECOMPENSA
            ORDER BY C.FECHA DESC
            """;

        ejecutarReporte("HISTORIAL DE CANJES", sql);
    }

    public void reporteRecompensas() {

        String sql = """
            SELECT CODIGO,
                   NOMBRE,
                   PUNTOS_NECESARIOS
            FROM RECOMPENSA
            WHERE ESTADO = 'A'
            ORDER BY PUNTOS_NECESARIOS
            """;

        ejecutarReporte("RECOMPENSAS DISPONIBLES", sql);
    }

    public void reporteInventarioBajo() {

        String sql = """
            SELECT CODIGO,
                   NOMBRE,
                   EXISTENCIA
            FROM PRODUCTO
            WHERE EXISTENCIA <= 5
              AND ESTADO = 'A'
            ORDER BY EXISTENCIA
            """;

        ejecutarReporte("INVENTARIO BAJO", sql);
    }

    public void reporteVentas() {

        String sql = """
            SELECT ID_PEDIDO,
                   ID_CLIENTE,
                   FECHA,
                   TOTAL,
                   ESTADO
            FROM PEDIDO
            WHERE ESTADO IN ('PAGADO', 'ENTREGADO')
            ORDER BY FECHA DESC
            """;

        ejecutarReporte("REPORTE DE VENTAS", sql);
    }
}
