/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sandwicheselgordo;

import com.sandwicheselgordo.util.ConexionOracle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author PC
 */
public class SandwichesElGordo {

  
    public static void main(String[] args) {
        
          String sql = """
                     SELECT CODIGO,
                            NOMBRE,
                            PRECIO,
                            EXISTENCIA
                     FROM PRODUCTO
                     ORDER BY CODIGO
                     """;

        System.out.println("==============================");
        System.out.println("     SANDWICHES EL GORDO");
        System.out.println("==============================");

        try (
                Connection conexion =
                        ConexionOracle.getConexion();

                PreparedStatement ps =
                        conexion.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()) {

            System.out.println("Conexion con Oracle exitosa.");
             System.out.println();
            System.out.println("PRODUCTOS REGISTRADOS:");
            System.out.println("------------------------------");

            while (rs.next()) {

                System.out.println(
                        rs.getString("CODIGO")
                        + " | "
                        + rs.getString("NOMBRE")
                        + " | Q"
                        + rs.getDouble("PRECIO")
                        + " | Stock: "
                        + rs.getInt("EXISTENCIA")
                );
            }

        } catch (SQLException e) {

            System.out.println("ERROR AL CONECTAR CON ORACLE:");
            System.out.println(e.getMessage());
        }
    }
}
