/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sandwicheselgordo.dao;

import com.sandwicheselgordo.modelo.Cliente;
import com.sandwicheselgordo.util.ConexionOracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public List<Cliente> listar() {

        List<Cliente> clientes = new ArrayList<>();

        String sql = """
                SELECT ID_CLIENTE,
                       CODIGO_CLIENTE,
                       NOMBRE,
                       TELEFONO,
                       CORREO,
                       DIRECCION,
                       PUNTOS,
                       ESTADO
                FROM CLIENTE
                ORDER BY NOMBRE
                """;

        try (
                Connection cn = ConexionOracle.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                clientes.add(crearCliente(rs));
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar clientes: "
                    + e.getMessage()
            );
        }

        return clientes;
    }

    public Cliente buscarPorCodigo(String codigo) {

        String sql = """
                SELECT ID_CLIENTE,
                       CODIGO_CLIENTE,
                       NOMBRE,
                       TELEFONO,
                       CORREO,
                       DIRECCION,
                       PUNTOS,
                       ESTADO
                FROM CLIENTE
                WHERE CODIGO_CLIENTE = ?
                """;

        try (
                Connection cn = ConexionOracle.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return crearCliente(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar cliente: "
                    + e.getMessage()
            );
        }

        return null;
    }

    public boolean insertar(Cliente cliente) {

        String sql = """
                INSERT INTO CLIENTE
                (CODIGO_CLIENTE,
                 NOMBRE,
                 TELEFONO,
                 CORREO,
                 DIRECCION,
                 PUNTOS,
                 ESTADO)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection cn = ConexionOracle.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setString(1, cliente.getIdentificacion());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getCorreo());
            ps.setString(5, cliente.getDireccion());
            ps.setInt(6, cliente.getPuntos());

            ps.setString(
                    7,
                    cliente.isActivo() ? "A" : "I"
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al insertar cliente: "
                    + e.getMessage()
            );

            return false;
        }
    }

    public boolean actualizar(Cliente cliente) {

        String sql = """
                UPDATE CLIENTE
                SET NOMBRE = ?,
                    TELEFONO = ?,
                    CORREO = ?,
                    DIRECCION = ?,
                    ESTADO = ?
                WHERE ID_CLIENTE = ?
                """;

        try (
                Connection cn = ConexionOracle.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getDireccion());

            ps.setString(
                    5,
                    cliente.isActivo() ? "A" : "I"
            );

            ps.setInt(
                    6,
                    cliente.getIdCliente()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al actualizar cliente: "
                    + e.getMessage()
            );

            return false;
        }
    }

    public boolean desactivar(int idCliente) {

        String sql = """
                UPDATE CLIENTE
                SET ESTADO = 'I'
                WHERE ID_CLIENTE = ?
                """;

        try (
                Connection cn = ConexionOracle.getConexion();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setInt(1, idCliente);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al desactivar cliente: "
                    + e.getMessage()
            );

            return false;
        }
    }

    private Cliente crearCliente(ResultSet rs)
            throws SQLException {

        return new Cliente(
                rs.getInt("ID_CLIENTE"),
                rs.getString("CODIGO_CLIENTE"),
                rs.getString("NOMBRE"),
                rs.getString("TELEFONO"),
                rs.getString("CORREO"),
                rs.getString("DIRECCION"),
                rs.getInt("PUNTOS"),

                "A".equalsIgnoreCase(
                        rs.getString("ESTADO")
                )
        );
    }
}
