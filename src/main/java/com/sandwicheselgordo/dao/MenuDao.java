
package com.sandwicheselgordo.dao;

import com.sandwicheselgordo.modelo.MenuCompleto;
import com.sandwicheselgordo.modelo.Producto;
import com.sandwicheselgordo.util.ConexionOracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuDao {

    private static final String SQL_BASE = """
        SELECT
            M.ID_MENU,
            M.CODIGO,
            M.NOMBRE,
            M.PRECIO,
            M.ESTADO,
            M.ID_SANDWICH,
            M.ID_BEBIDA,
            M.ID_ACOMPANAMIENTO
        FROM MENU M
        """;

    public List<MenuCompleto> listar() throws SQLException {

        List<MenuCompleto> menus = new ArrayList<>();

        String sql = SQL_BASE + " ORDER BY M.CODIGO";

        try (Connection cn = ConexionOracle.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                menus.add(crearMenu(rs, cn));
            }
        }

        return menus;
    }

    public MenuCompleto buscarPorCodigo(String codigo)
            throws SQLException {

        String sql = SQL_BASE + " WHERE M.CODIGO = ?";

        try (Connection cn = ConexionOracle.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return crearMenu(rs, cn);
                }
            }
        }

        return null;
    }

    private MenuCompleto crearMenu(
            ResultSet rs, Connection cn) throws SQLException {

        Producto sandwich = buscarProducto(
                cn, rs.getInt("ID_SANDWICH"));

        Producto bebida = buscarProducto(
                cn, rs.getInt("ID_BEBIDA"));

        Producto acompanamiento = buscarProducto(
                cn, rs.getInt("ID_ACOMPANAMIENTO"));

        return new MenuCompleto(
                rs.getInt("ID_MENU"),
                rs.getString("CODIGO"),
                rs.getString("NOMBRE"),
                rs.getDouble("PRECIO"),
                "A".equalsIgnoreCase(rs.getString("ESTADO")),
                sandwich,
                bebida,
                acompanamiento
        );
    }

    private Producto buscarProducto(Connection cn, int idProducto)
            throws SQLException {

        String sql = """
            SELECT
                P.ID_PRODUCTO,
                P.CODIGO,
                P.NOMBRE,
                P.PRECIO,
                P.EXISTENCIA,
                P.ID_CATEGORIA,
                P.ESTADO,
                C.NOMBRE AS TIPO_PRODUCTO
            FROM PRODUCTO P
            INNER JOIN CATEGORIA C
                ON P.ID_CATEGORIA = C.ID_CATEGORIA
            WHERE P.ID_PRODUCTO = ?
            """;

        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    throw new SQLException(
                            "Producto del menu no encontrado: "
                            + idProducto
                    );
                }

                Producto producto = new Producto(
                        rs.getInt("ID_PRODUCTO"),
                        rs.getString("CODIGO"),
                        rs.getString("NOMBRE"),
                        null,
                        rs.getString("TIPO_PRODUCTO"),
                        null,
                        rs.getDouble("PRECIO"),
                        rs.getInt("EXISTENCIA"),
                        rs.getInt("ID_CATEGORIA"),
                        "A".equalsIgnoreCase(
                                rs.getString("ESTADO"))
                );

                return producto;
            }
        }
    }
}
