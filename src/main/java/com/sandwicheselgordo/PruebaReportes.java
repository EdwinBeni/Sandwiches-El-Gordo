/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sandwicheselgordo;

import com.sandwicheselgordo.dao.ReporteDAO;

public class PruebaReportes {

    public static void main(String[] args) {

        ReporteDAO reportes = new ReporteDAO();

        System.out.println("==============================");
        System.out.println("SANDWICHES EL GORDO");
        System.out.println("PRUEBA DE REPORTES - DILAN");
        System.out.println("==============================");

        reportes.reportePuntosClientes();

        reportes.reporteHistorialCanjes();

        reportes.reporteRecompensas();

        reportes.reporteInventarioBajo();

        reportes.reporteVentas();

        System.out.println("\nPRUEBAS FINALIZADAS");
    }
}
