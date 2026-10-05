/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.sandwicheselgordo;

import com.sandwicheselgordo.dao.ClienteDAO;
import com.sandwicheselgordo.dao.ProductoDAO;
import com.sandwicheselgordo.modelo.Cliente;
import com.sandwicheselgordo.modelo.Producto;

public class SandwichesElGordo {

    public static void main(String[] args) {

        System.out.println("==============================");
        System.out.println("     SANDWICHES EL GORDO");
        System.out.println("==============================");

        ProductoDAO productoDAO = new ProductoDAO();

        System.out.println();
        System.out.println("PRODUCTOS REGISTRADOS:");
        System.out.println("------------------------------");

        for (Producto producto : productoDAO.listar()) {

            System.out.println(
                    producto.getCodigo()
                    + " | "
                    + producto.getNombre()
                    + " | Q"
                    + producto.getPrecio()
                    + " | Stock: "
                    + producto.getExistencia()
            );
        }

        ClienteDAO clienteDAO = new ClienteDAO();

        System.out.println();
        System.out.println("CLIENTES REGISTRADOS:");
        System.out.println("------------------------------");

        for (Cliente cliente : clienteDAO.listar()) {

            System.out.println(
                    cliente.getIdentificacion()
                    + " | "
                    + cliente.getNombre()
                    + " | Puntos: "
                    + cliente.getPuntos()
                    + " | Estado: "
                    + (cliente.isActivo()
                        ? "Activo"
                        : "Inactivo")
            );
        }

        System.out.println();
        System.out.println("==============================");
        System.out.println("       SISTEMA INICIADO");
        System.out.println("==============================");
    }
}