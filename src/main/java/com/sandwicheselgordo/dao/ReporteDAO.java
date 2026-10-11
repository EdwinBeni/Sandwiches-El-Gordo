
package com.sandwicheselgordo.dao;

import com.sandwicheselgordo.util.ConexionOracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

public class ReporteDAO {

    private void ejecutarReporte(String titulo, String sql) {

        System.out.println("\n================================");
        System.out.println(titulo);
        System.out.println("================================");

        try (Connection cn = ConexionOracle.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData metadata = rs.getMetaData();

            int columnas = metadata.getColumnCount();
            int filas = 0;

            while (rs.next()) {

                filas++;

                for (int i = 1; i <= columnas; i++) {

                    String columna = metadata.getColumnLabel(i);

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

    public DefaultTableModel obtenerReporte(String sql)
            throws SQLException {

        if (sql == null || sql.isBlank()) {
            throw new IllegalArgumentException(
                    "La consulta SQL es obligatoria."
            );
        }

        DefaultTableModel modelo = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        try (Connection cn = ConexionOracle.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData metadata = rs.getMetaData();

            int columnas = metadata.getColumnCount();

            for (int i = 1; i <= columnas; i++) {

                modelo.addColumn(
                        metadata.getColumnLabel(i)
                );
            }

            while (rs.next()) {

                Object[] fila = new Object[columnas];

                for (int i = 1; i <= columnas; i++) {

                    fila[i - 1] = rs.getObject(i);
                }

                modelo.addRow(fila);
            }
        }

        return modelo;
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
            ORDER BY EXISTENCIA ASC
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

    public DefaultTableModel obtenerPuntosClientes()
            throws SQLException {

        String sql = """
            SELECT CODIGO_CLIENTE AS CODIGO,
                   NOMBRE AS CLIENTE,
                   PUNTOS,
                   ESTADO
            FROM CLIENTE
            ORDER BY PUNTOS DESC
            """;

        return obtenerReporte(sql);
    }

    public DefaultTableModel obtenerHistorialCanjes()
            throws SQLException {

        String sql = """
            SELECT C.ID_CANJE,
                   CL.NOMBRE AS CLIENTE,
                   R.NOMBRE AS RECOMPENSA,
                   C.FECHA,
                   C.PUNTOS_UTILIZADOS AS PUNTOS,
                   C.ESTADO
            FROM CANJE C
            INNER JOIN CLIENTE CL
                ON C.ID_CLIENTE = CL.ID_CLIENTE
            INNER JOIN RECOMPENSA R
                ON C.ID_RECOMPENSA = R.ID_RECOMPENSA
            ORDER BY C.FECHA DESC
            """;

        return obtenerReporte(sql);
    }

    public DefaultTableModel obtenerRecompensas()
            throws SQLException {

        String sql = """
            SELECT CODIGO,
                   NOMBRE AS RECOMPENSA,
                   PUNTOS_NECESARIOS AS PUNTOS,
                   ESTADO
            FROM RECOMPENSA
            ORDER BY PUNTOS_NECESARIOS ASC
            """;

        return obtenerReporte(sql);
    }

    public DefaultTableModel obtenerInventarioBajo()
            throws SQLException {

        String sql = """
            SELECT CODIGO,
                   NOMBRE AS PRODUCTO,
                   EXISTENCIA,
                   ESTADO
            FROM PRODUCTO
            WHERE EXISTENCIA <= 5
              AND ESTADO = 'A'
            ORDER BY EXISTENCIA ASC
            """;

        return obtenerReporte(sql);
    }

    public DefaultTableModel obtenerVentas()
            throws SQLException {

        String sql = """
            SELECT P.ID_PEDIDO,
                   C.NOMBRE AS CLIENTE,
                   P.FECHA,
                   P.TOTAL,
                   P.ESTADO,
                   P.PUNTOS_GENERADOS AS PUNTOS
            FROM PEDIDO P
            INNER JOIN CLIENTE C
                ON P.ID_CLIENTE = C.ID_CLIENTE
            WHERE P.ESTADO IN ('PAGADO', 'ENTREGADO')
            ORDER BY P.FECHA DESC
            """;

        return obtenerReporte(sql);
    }

 
public DefaultTableModel obtenerVentasPorMetodoPago()
        throws SQLException {

    String sql = """
        SELECT P.ID_PEDIDO,
               C.NOMBRE AS CLIENTE,
               P.FECHA,
               P.TOTAL,
               PG.METODO,
               PG.REFERENCIA,
               PG.ESTADO AS ESTADO_PAGO
        FROM PEDIDO P
        INNER JOIN CLIENTE C
            ON P.ID_CLIENTE = C.ID_CLIENTE
        INNER JOIN PAGO PG
            ON P.ID_PEDIDO = PG.ID_PEDIDO
        WHERE P.ESTADO IN ('PAGADO', 'ENTREGADO')
          AND PG.ESTADO = 'APROBADO'
        ORDER BY P.FECHA DESC
        """;

    return obtenerReporte(sql);
}

    public DefaultTableModel obtenerProductosMasVendidos()
            throws SQLException {

        String sql = """
            SELECT PR.CODIGO,
                   PR.NOMBRE AS PRODUCTO,
                   SUM(D.CANTIDAD) AS UNIDADES_VENDIDAS,
                   SUM(D.SUBTOTAL) AS TOTAL_VENDIDO
            FROM DETALLE_PEDIDO D
            INNER JOIN PRODUCTO PR
                ON D.ID_PRODUCTO = PR.ID_PRODUCTO
            INNER JOIN PEDIDO P
                ON D.ID_PEDIDO = P.ID_PEDIDO
            WHERE P.ESTADO IN ('PAGADO', 'ENTREGADO')
              AND D.ID_PRODUCTO IS NOT NULL
            GROUP BY PR.CODIGO, PR.NOMBRE
            ORDER BY UNIDADES_VENDIDAS DESC
            """;

        return obtenerReporte(sql);
    }

    public DefaultTableModel obtenerMenusVendidos()
            throws SQLException {

        String sql = """
            SELECT M.CODIGO,
                   M.NOMBRE AS MENU,
                   SUM(D.CANTIDAD) AS UNIDADES_VENDIDAS,
                   SUM(D.SUBTOTAL) AS TOTAL_VENDIDO
            FROM DETALLE_PEDIDO D
            INNER JOIN MENU M
                ON D.ID_MENU = M.ID_MENU
            INNER JOIN PEDIDO P
                ON D.ID_PEDIDO = P.ID_PEDIDO
            WHERE P.ESTADO IN ('PAGADO', 'ENTREGADO')
              AND D.ID_MENU IS NOT NULL
            GROUP BY M.CODIGO, M.NOMBRE
            ORDER BY UNIDADES_VENDIDAS DESC
            """;

        return obtenerReporte(sql);
    }
}
