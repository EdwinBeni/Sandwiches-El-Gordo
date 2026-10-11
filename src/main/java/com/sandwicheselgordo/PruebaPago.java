
package com.sandwicheselgordo;

import com.sandwicheselgordo.servicio.PagoService;
import java.sql.SQLException;
import java.util.Scanner;

public class PruebaPago {

    public static void main(String[] args) {

        PagoService servicio = new PagoService();

        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("=================================");
            System.out.println(" PRUEBA DE PAGOS - SANDWICHES EL GORDO");
            System.out.println("=================================");

            System.out.print("Ingrese ID del pedido: ");
            int idPedido = Integer.parseInt(scanner.nextLine());

            System.out.println("\n1. Pago en efectivo");
            System.out.println("2. Tarjeta aprobada");
            System.out.println("3. Tarjeta rechazada");

            System.out.print("Seleccione una opcion: ");
            int opcion = Integer.parseInt(scanner.nextLine());

            boolean resultado;

            switch (opcion) {

                case 1 -> {
                    System.out.print("Dinero recibido: Q");
                    double recibido =
                            Double.parseDouble(scanner.nextLine());

                    resultado = servicio.pagarEfectivo(
                            idPedido, recibido);
                }

                case 2 -> {
                    System.out.print("Referencia de tarjeta: ");
                    String referencia = scanner.nextLine();

                    resultado = servicio.pagarTarjeta(
                            idPedido, referencia, true);
                }

                case 3 -> {
                    System.out.print("Referencia de tarjeta: ");
                    String referencia = scanner.nextLine();

                    resultado = servicio.pagarTarjeta(
                            idPedido, referencia, false);
                }

                default -> {
                    System.out.println("Opcion no valida.");
                    return;
                }
            }

            if (resultado) {
                System.out.println(
                        "\nPAGO APROBADO CORRECTAMENTE");
                System.out.println(
                        "Inventario y puntos actualizados.");
            } else {
                System.out.println(
                        "\nPAGO RECHAZADO");
                System.out.println(
                        "Puede intentar nuevamente.");
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error de base de datos: "
                    + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println(
                    "Error: " + e.getMessage());
        }
    }
}
