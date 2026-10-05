/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sandwicheselgordo.dao;

import com.sandwicheselgordo.modelo.Producto;
import com.sandwicheselgordo.util.ConexionOracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> listar() {

        List<Producto> productos = new ArrayList<>();

        String sql = """
                SELECT ID_PRODUCTO,
                       CODIGO,
                       ID_CATEGORIA,
                       NOMBRE,
                       PRECIO,
                       EXISTENCIA,
                       ESTADO
                FROM PRODUCTO
                ORDER BY CODIGO
                """;

        try (
                Connection cn = ConexionOracle.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                productos.add(crearProducto(rs));
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar productos: "
                    + e.getMessage()
            );
        }

        return productos;
    }

    public Producto buscarPorCodigo(String codigo) {

        String sql = """
                SELECT ID_PRODUCTO,
                       CODIGO,
                       ID_CATEGORIA,
                       NOMBRE,
                       PRECIO,
                       EXISTENCIA,
                       ESTADO
                FROM PRODUCTO
                WHERE CODIGO = ?
                """;

        try (
                Connection cn = ConexionOracle.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return crearProducto(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar producto: "
                    + e.getMessage()
            );
        }

        return null;
    }

    public boolean insertar(Producto producto) {

        String sql = """
                INSERT INTO PRODUCTO
                (CODIGO,
                 ID_CATEGORIA,
                 NOMBRE,
                 PRECIO,
                 EXISTENCIA,
                 ESTADO)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection cn = ConexionOracle.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setString(1, producto.getCodigo());
            ps.setInt(2, producto.getIdCategoria());
            ps.setString(3, producto.getNombre());
            ps.setDouble(4, producto.getPrecio());
            ps.setInt(5, producto.getExistencia());
            ps.setString(
                    6,
                    producto.isActivo() ? "A" : "I"
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al insertar producto: "
                    + e.getMessage()
            );

            return false;
        }
    }

    public boolean actualizar(Producto producto) {

        String sql = """
                UPDATE PRODUCTO
                SET ID_CATEGORIA = ?,
                    NOMBRE = ?,
                    PRECIO = ?,
                    ESTADO = ?
                WHERE ID_PRODUCTO = ?
                """;

        try (
                Connection cn = ConexionOracle.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setInt(1, producto.getIdCategoria());
            ps.setString(2, producto.getNombre());
            ps.setDouble(3, producto.getPrecio());

            ps.setString(
                    4,
                    producto.isActivo() ? "A" : "I"
            );

            ps.setInt(
                    5,
                    producto.getIdProducto()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al actualizar producto: "
                    + e.getMessage()
            );

            return false;
        }
    }

    public boolean desactivar(int idProducto) {

        String sql = """
                UPDATE PRODUCTO
                SET ESTADO = 'I'
                WHERE ID_PRODUCTO = ?
                """;

        try (
                Connection cn = ConexionOracle.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setInt(1, idProducto);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al desactivar producto: "
                    + e.getMessage()
            );

            return false;
        }
    }

    private Producto crearProducto(ResultSet rs)
            throws SQLException {

        return new Producto(
                rs.getInt("ID_PRODUCTO"),
                rs.getString("CODIGO"),
                rs.getString("NOMBRE"),

                null,
                null,
                null,

                rs.getDouble("PRECIO"),
                rs.getInt("EXISTENCIA"),
                rs.getInt("ID_CATEGORIA"),

                "A".equalsIgnoreCase(
                        rs.getString("ESTADO")
                )
        );
    }
}