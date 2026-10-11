
package com.sandwicheselgordo;

import com.sandwicheselgordo.servicio.PedidoService;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class PruebaEstadosPedido {

    public static void main(String[] args) {

        PedidoService servicio = new PedidoService();
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("================================");
            System.out.println("GESTION DE ESTADOS DE PEDIDOS");
            System.out.println("SANDWICHES EL GORDO");
            System.out.println("================================");

            mostrarPedidos(servicio);

            System.out.println("\n1. Entregar pedido PAGADO");
            System.out.println("2. Anular pedido PENDIENTE");
            System.out.println("3. Solo consultar pedidos");
            System.out.print("Seleccione una opcion: ");

            int opcion = Integer.parseInt(
                    scanner.nextLine().trim()
            );

            if (opcion == 3) {
                System.out.println("Consulta finalizada.");
                return;
            }

            if (opcion != 1 && opcion != 2) {
                System.out.println("Opcion invalida.");
                return;
            }

            System.out.print("Ingrese ID del pedido: ");

            int idPedido = Integer.parseInt(
                    scanner.nextLine().trim()
            );

            String estadoRequerido =
                    opcion == 1 ? "PAGADO" : "PENDIENTE";

            String estadoActual = null;

            for (String[] pedido : servicio.listarPedidos()) {

                if (Integer.parseInt(pedido[0]) == idPedido) {
                    estadoActual = pedido[4];
                    break;
                }
            }

            if (estadoActual == null) {
                System.out.println("Pedido no encontrado.");
                return;
            }

            if (!estadoRequerido.equals(estadoActual)) {
                System.out.println(
                        "Operacion no permitida. Estado actual: "
                        + estadoActual
                );
                return;
            }

            System.out.print(
                    "Confirma el cambio de estado? (S/N): "
            );

            String confirmacion =
                    scanner.nextLine().trim();

            if (!confirmacion.equalsIgnoreCase("S")) {
                System.out.println("Operacion cancelada.");
                return;
            }

            boolean actualizado;

            if (opcion == 1) {
                actualizado = servicio.entregarPedido(idPedido);
            } else {
                actualizado = servicio.anularPedido(idPedido);
            }

            if (actualizado) {
                System.out.println(
                        "Estado actualizado correctamente."
                );
            } else {
                System.out.println(
                        "No se actualizo el pedido. "
                        + "Verifique su estado actual."
                );
            }

            mostrarPedidos(servicio);

        } catch (NumberFormatException e) {

            System.out.println(
                    "Debe ingresar un numero valido."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error de base de datos: " + e.getMessage()
            );
        }
    }

    private static void mostrarPedidos(
            PedidoService servicio) throws SQLException {

        List<String[]> pedidos = servicio.listarPedidos();

        System.out.println("\nPEDIDOS REGISTRADOS");
        System.out.println("------------------------------");

        for (String[] pedido : pedidos) {

            System.out.println(
                    "ID: " + pedido[0]
                    + " | Cliente: " + pedido[1]
                    + " | Total: Q" + pedido[3]
                    + " | Estado: " + pedido[4]
                    + " | Puntos: " + pedido[5]
            );
        }

        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
        }
    }
}
