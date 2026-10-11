
package com.sandwicheselgordo.dao;

import com.sandwicheselgordo.util.ConexionOracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecompensaDAO {

    private static final String SQL_BASE = """
        SELECT
            R.ID_RECOMPENSA,
            R.CODIGO,
            R.NOMBRE,
            R.PUNTOS_NECESARIOS,
            R.ID_PRODUCTO,
            R.ID_MENU,
            R.ESTADO
        FROM RECOMPENSA R
        """;

    public List<String[]> listar() throws SQLException {

        List<String[]> recompensas = new ArrayList<>();

        String sql = SQL_BASE
                + " ORDER BY R.PUNTOS_NECESARIOS";

        try (Connection cn = ConexionOracle.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                recompensas.add(convertirFila(rs));
            }
        }

        return recompensas;
    }

    public List<String[]> listarActivas()
            throws SQLException {

        List<String[]> recompensas = new ArrayList<>();

        String sql = SQL_BASE
                + " WHERE R.ESTADO = 'A'"
                + " ORDER BY R.PUNTOS_NECESARIOS";

        try (Connection cn = ConexionOracle.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                recompensas.add(convertirFila(rs));
            }
        }

        return recompensas;
    }

    public String[] buscarPorCodigo(String codigo)
            throws SQLException {

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "El codigo es obligatorio.");
        }

        String sql = SQL_BASE
                + " WHERE R.CODIGO = ?";

        try (Connection cn = ConexionOracle.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, codigo.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return convertirFila(rs);
                }
            }
        }

        return null;
    }

    public String[] buscarPorId(int idRecompensa)
            throws SQLException {

        if (idRecompensa <= 0) {
            throw new IllegalArgumentException(
                    "ID de recompensa invalido.");
        }

        String sql = SQL_BASE
                + " WHERE R.ID_RECOMPENSA = ?";

        try (Connection cn = ConexionOracle.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idRecompensa);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return convertirFila(rs);
                }
            }
        }

        return null;
    }

    private String[] convertirFila(ResultSet rs)
            throws SQLException {

        Object idProducto = rs.getObject("ID_PRODUCTO");
        Object idMenu = rs.getObject("ID_MENU");

        return new String[]{
            String.valueOf(rs.getInt("ID_RECOMPENSA")),
            rs.getString("CODIGO"),
            rs.getString("NOMBRE"),
            String.valueOf(rs.getInt("PUNTOS_NECESARIOS")),
            idProducto == null ? "" : idProducto.toString(),
            idMenu == null ? "" : idMenu.toString(),
            rs.getString("ESTADO")
        };
    }
}
