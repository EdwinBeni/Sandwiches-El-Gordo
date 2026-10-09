/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.sandwicheselgordo;

import com.sandwicheselgordo.dao.ClienteDAO;
import com.sandwicheselgordo.dao.ProductoDAO;
import com.sandwicheselgordo.modelo.Cliente;
import com.sandwicheselgordo.modelo.Producto;
import com.sandwicheselgordo.modelo.ProductoIndividual;
import com.sandwicheselgordo.modelo.DetallePedido;
import com.sandwicheselgordo.modelo.Pedido;
import com.sandwicheselgordo.servicio.PedidoService;

public class PruebaPedido {

    // Cambiar a true solamente cuando todo este correcto.
    private static final boolean CONFIRMAR_INSERCION = false;

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println("    PRUEBA DE PEDIDOS - BRANDO");
        System.out.println("==================================");

        try {
            ClienteDAO clienteDAO = new ClienteDAO();
            ProductoDAO productoDAO = new ProductoDAO();

            Cliente cliente =
                    clienteDAO.buscarPorCodigo("CLI001");

            Producto producto =
                    productoDAO.buscarPorCodigo("SAN001");

            if (cliente == null) {
                System.out.println("ERROR: Cliente CLI001 no encontrado.");
                return;
            }

            if (producto == null) {
                System.out.println("ERROR: Producto SAN001 no encontrado.");
                return;
            }

            System.out.println("\nDATOS DEL CLIENTE");
            System.out.println("----------------------------");
            System.out.println("ID: " + cliente.getIdCliente());
            System.out.println("Nombre: " + cliente.getNombre());

            System.out.println("\nDATOS DEL PRODUCTO");
            System.out.println("----------------------------");
            System.out.println("ID: " + producto.getIdProducto());
            System.out.println("Codigo: " + producto.getCodigo());
            System.out.println("Nombre: " + producto.getNombre());
            System.out.println("Precio: Q" + producto.getPrecio());
            System.out.println("Existencia: " + producto.getExistencia());
            System.out.println("Activo: " + producto.isActivo());

            String tipo = producto.getTipoProducto();

            System.out.println("Tipo de producto: [" + tipo + "]");

            ProductoIndividual item =
                    new ProductoIndividual(producto);

            int puntosPorUnidad = item.calcularPuntos();

            System.out.println("Puntos por unidad: " + puntosPorUnidad);

            DetallePedido detalle =
                    new DetallePedido(0, item, 1);

            Pedido pedido =
                    new Pedido(0, cliente);

            pedido.agregarDetalle(detalle);

            System.out.println("\nRESUMEN DEL PEDIDO");
            System.out.println("----------------------------");
            System.out.println("Cliente: " + cliente.getNombre());
            System.out.println("Producto: " + producto.getNombre());
            System.out.println("Cantidad: " + detalle.getCantidad());
            System.out.println("Subtotal: Q" + pedido.calcularSubtotal());
            System.out.println("Total: Q" + pedido.calcularTotal());
            System.out.println("Puntos potenciales: "
                    + pedido.calcularPuntos());
            System.out.println("Estado: " + pedido.getEstado());

            if (!item.hayExistencia(detalle.getCantidad())) {
                System.out.println(
                        "\nERROR: Inventario insuficiente."
                );
                return;
            }

            // Verificar puntos antes de guardar.
            if (producto.getCodigo().equalsIgnoreCase("SAN001")
                    && pedido.calcularPuntos() != 2) {

                System.out.println("\nADVERTENCIA:");
                System.out.println(
                        "El sandwich SAN001 deberia generar 2 puntos."
                );
                System.out.println(
                        "Actualmente genera: " + pedido.calcularPuntos()
                );
                System.out.println(
                        "Revisar getTipoProducto() y ProductoIndividual."
                );
                System.out.println(
                        "No se registrara el pedido hasta corregirlo."
                );
                return;
            }

            if (!CONFIRMAR_INSERCION) {
                System.out.println("\nSIMULACION CORRECTA.");
                System.out.println("No se guardo ningun pedido.");
                System.out.println(
                        "Cambiar CONFIRMAR_INSERCION a true "
                        + "para registrar el pedido."
                );
                return;
            }

            PedidoService servicio = new PedidoService();

            int idPedido = servicio.registrarPedido(pedido);

            System.out.println("\n==================================");
            System.out.println(" PEDIDO REGISTRADO CORRECTAMENTE");
            System.out.println("==================================");
            System.out.println("ID Pedido: " + idPedido);
            System.out.println("Total: Q" + pedido.calcularTotal());
            System.out.println("Puntos potenciales: "
                    + pedido.calcularPuntos());
            System.out.println("Estado: " + pedido.getEstado());

        } catch (Exception e) {
            System.err.println("\nERROR EN LA PRUEBA:");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
