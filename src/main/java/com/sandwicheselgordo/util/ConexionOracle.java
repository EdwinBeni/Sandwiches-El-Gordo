/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sandwicheselgordo.util;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
/**
 *
 * @author PC
 */
public class ConexionOracle {

    private static final Properties PROPIEDADES = new Properties();

    static {
        try (FileInputStream archivo =
                new FileInputStream("config.properties")) {

            PROPIEDADES.load(archivo);

        } catch (IOException e) {
            throw new ExceptionInInitializerError(
                    "No se pudo cargar config.properties: "
                    + e.getMessage()
            );
        }
    }

    private ConexionOracle() {
    }

    public static Connection getConexion() throws SQLException {

        String url = PROPIEDADES.getProperty("db.url");
        String usuario = PROPIEDADES.getProperty("db.user");
        String password = PROPIEDADES.getProperty("db.password");

        return DriverManager.getConnection(
                url,
                usuario,
                password
        );
    }
}