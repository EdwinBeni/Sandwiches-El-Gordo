
package com.sandwicheselgordo;

import com.sandwicheselgordo.dao.MenuDao;
import com.sandwicheselgordo.modelo.MenuCompleto;

import java.sql.SQLException;
import java.util.List;

public class PruebaMenu {

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println(" PRUEBA DE MENUS - SANDWICHES EL GORDO");
        System.out.println("==================================");

        MenuDao menuDAO = new MenuDao();

        try {

            List<MenuCompleto> menus = menuDAO.listar();

            if (menus.isEmpty()) {
                System.out.println(
                        "No se encontraron menus en Oracle."
                );
                return;
            }

            for (MenuCompleto menu : menus) {

                System.out.println("------------------------------");
                System.out.println("Codigo: " + menu.getCodigo());
                System.out.println("Nombre: " + menu.getNombre());
                System.out.println("Precio: Q" + menu.getPrecio());
                System.out.println("Activo: " + menu.isActivo());

                System.out.println(
                        "Sandwich: " + menu.getSandwich().getNombre()
                );

                System.out.println(
                        "Bebida: " + menu.getBebida().getNombre()
                );

                System.out.println(
                        "Acompanamiento: "
                        + menu.getAcompanamiento().getNombre()
                );

                System.out.println(
                        "Puntos por menu: " + menu.calcularPuntos()
                );

                System.out.println(
                        "Disponible para 1 menu: "
                        + menu.hayExistencia(1)
                );
            }

            System.out.println("------------------------------");
            System.out.println("TOTAL DE MENUS: " + menus.size());
            System.out.println("PRUEBA FINALIZADA");

        } catch (SQLException e) {

            System.err.println(
                    "Error al consultar Oracle: " + e.getMessage()
            );
            e.printStackTrace();
        }
    }
}
