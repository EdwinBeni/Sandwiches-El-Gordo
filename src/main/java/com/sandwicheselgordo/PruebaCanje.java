/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sandwicheselgordo;

import com.sandwicheselgordo.dao.CanjeDAO;

public class PruebaCanje {

    public static void main(String[] args) {

        System.out.println("==========================");
        System.out.println("PRUEBA DE CANJE - DILAN");
        System.out.println("==========================");

        int idCliente = 1;
        int idRecompensa = 1;

        CanjeDAO dao = new CanjeDAO();

        System.out.println("Cliente ID: " + idCliente);
        System.out.println("Recompensa ID: " + idRecompensa);

        boolean resultado = dao.registrarCanje(
                idCliente,
                idRecompensa
        );

        if (resultado) {
            System.out.println(
                "CANJE REGISTRADO CORRECTAMENTE"
            );
        } else {
            System.out.println(
                "NO SE PUDO REALIZAR EL CANJE"
            );
        }
    }
}
