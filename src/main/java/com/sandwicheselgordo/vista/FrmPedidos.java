
package com.sandwicheselgordo.vista;

import com.sandwicheselgordo.dao.ClienteDAO;
import com.sandwicheselgordo.dao.ProductoDAO;
import com.sandwicheselgordo.modelo.Cliente;
import com.sandwicheselgordo.modelo.DetallePedido;
import com.sandwicheselgordo.modelo.Pedido;
import com.sandwicheselgordo.modelo.Producto;
import com.sandwicheselgordo.modelo.ProductoIndividual;
import com.sandwicheselgordo.servicio.PedidoService;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class FrmPedidos extends JFrame {

    private final ClienteDAO clienteDAO;
    private final ProductoDAO productoDAO;
    private final PedidoService pedidoService;

    private Cliente clienteSeleccionado;
    private final List<DetallePedido> carrito;

    private JTextField txtCodigoCliente;
    private JLabel lblCliente;
    private JLabel lblPuntosCliente;

    private JComboBox<Producto> cmbProductos;
    private JSpinner spnCantidad;

    private JTable tablaDetalles;
    private DefaultTableModel modeloTabla;

    private JLabel lblSubtotal;
    private JLabel lblTotal;
    private JLabel lblPuntos;

    private JButton btnConfirmar;

    private static final Color NEGRO = new Color(22, 22, 22);
    private static final Color DORADO = new Color(197, 160, 82);
    private static final Color BLANCO = new Color(245, 241, 232);

    public FrmPedidos() {

        clienteDAO = new ClienteDAO();
        productoDAO = new ProductoDAO();
        pedidoService = new PedidoService();

        carrito = new ArrayList<>();

        configurarVentana();
        crearComponentes();
        cargarProductos();
    }

    private void configurarVentana() {

        setTitle("Sandwiches El Gordo - Registro de pedidos");
        setSize(1050, 720);
        setMinimumSize(new Dimension(850, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void crearComponentes() {

        JPanel principal = new JPanel(new BorderLayout(12, 12));
        principal.setBackground(NEGRO);
        principal.setBorder(
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        );

        JLabel titulo = new JLabel(
                "SANDWICHES EL GORDO - REGISTRO DE PEDIDOS",
                JLabel.CENTER
        );

        titulo.setFont(new Font("Serif", Font.BOLD, 25));
        titulo.setForeground(DORADO);

        principal.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(12, 12));
        centro.setBackground(NEGRO);

        JPanel formularios = new JPanel(new GridLayout(2, 1, 10, 10));
        formularios.setBackground(NEGRO);

        formularios.add(crearPanelCliente());
        formularios.add(crearPanelProductos());

        centro.add(formularios, BorderLayout.NORTH);
        centro.add(crearPanelTabla(), BorderLayout.CENTER);

        principal.add(centro, BorderLayout.CENTER);
        principal.add(crearPanelResumen(), BorderLayout.SOUTH);

        setContentPane(principal);
    }

    private JPanel crearPanelCliente() {

        JPanel panel = crearPanelSeccion("DATOS DEL CLIENTE");
        panel.setLayout(new GridLayout(2, 1, 8, 8));

        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        busqueda.setBackground(NEGRO);

        txtCodigoCliente = new JTextField(15);

        JButton btnBuscar = crearBoton("Buscar cliente");
        btnBuscar.addActionListener(e -> buscarCliente());

        busqueda.add(crearEtiqueta("Código:"));
        busqueda.add(txtCodigoCliente);
        busqueda.add(btnBuscar);

        JPanel informacion = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 20, 0)
        );

        informacion.setBackground(NEGRO);

        lblCliente = crearEtiqueta("Cliente: No seleccionado");
        lblPuntosCliente = crearEtiqueta("Puntos disponibles: 0");

        informacion.add(lblCliente);
        informacion.add(lblPuntosCliente);

        panel.add(busqueda);
        panel.add(informacion);

        return panel;
    }

    private JPanel crearPanelProductos() {

        JPanel panel = crearPanelSeccion("SELECCIÓN DE PRODUCTOS");
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 15));

        cmbProductos = new JComboBox<>();
        cmbProductos.setPreferredSize(new Dimension(340, 30));

        spnCantidad = new JSpinner(
                new SpinnerNumberModel(1, 1, 999, 1)
        );

        JButton btnAgregar = crearBoton("Agregar producto");
        btnAgregar.addActionListener(e -> agregarProducto());

        panel.add(crearEtiqueta("Producto:"));
        panel.add(cmbProductos);
        panel.add(crearEtiqueta("Cantidad:"));
        panel.add(spnCantidad);
        panel.add(btnAgregar);

        return panel;
    }

    private JPanel crearPanelTabla() {

        JPanel panel = crearPanelSeccion("DETALLE DEL PEDIDO");
        panel.setLayout(new BorderLayout(8, 8));

        modeloTabla = new DefaultTableModel(
                new Object[]{
                    "Código",
                    "Producto",
                    "Cantidad",
                    "Precio",
                    "Subtotal",
                    "Puntos"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaDetalles = new JTable(modeloTabla);
        tablaDetalles.setRowHeight(28);
        tablaDetalles.setSelectionBackground(DORADO);
        tablaDetalles.setSelectionForeground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(tablaDetalles);

        JButton btnEliminar = crearBoton("Eliminar seleccionado");
        btnEliminar.addActionListener(e -> eliminarProducto());

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.setBackground(NEGRO);
        acciones.add(btnEliminar);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(acciones, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelResumen() {

        JPanel panel = crearPanelSeccion("RESUMEN DE COMPRA");
        panel.setLayout(new GridLayout(2, 1, 8, 8));

        JPanel totales = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 35, 5)
        );

        totales.setBackground(NEGRO);

        lblSubtotal = crearEtiqueta("Subtotal: Q0.00");
        lblTotal = crearEtiqueta("Total: Q0.00");
        lblPuntos = crearEtiqueta("Puntos potenciales: 0");

        totales.add(lblSubtotal);
        totales.add(lblTotal);
        totales.add(lblPuntos);

        JPanel acciones = new JPanel(
                new FlowLayout(FlowLayout.CENTER)
        );

        acciones.setBackground(NEGRO);

        btnConfirmar = crearBoton("CONFIRMAR PEDIDO");
        btnConfirmar.addActionListener(e -> confirmarPedido());

        JButton btnLimpiar = crearBoton("Nueva compra");
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        acciones.add(btnConfirmar);
        acciones.add(btnLimpiar);

        panel.add(totales);
        panel.add(acciones);

        return panel;
    }

    private JPanel crearPanelSeccion(String titulo) {

        JPanel panel = new JPanel();
        panel.setBackground(NEGRO);

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(DORADO),
                        titulo,
                        0,
                        0,
                        new Font("Serif", Font.BOLD, 15),
                        DORADO
                )
        );

        return panel;
    }

    private JLabel crearEtiqueta(String texto) {

        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(BLANCO);
        etiqueta.setFont(new Font("SansSerif", Font.PLAIN, 14));

        return etiqueta;
    }

    private JButton crearBoton(String texto) {

        JButton boton = new JButton(texto);
        boton.setBackground(DORADO);
        boton.setForeground(Color.BLACK);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setFocusPainted(false);

        return boton;
    }

    private void buscarCliente() {

        String codigo = txtCodigoCliente.getText().trim();

        if (codigo.isEmpty()) {
            mostrarError("Ingrese el código del cliente.");
            return;
        }

        Cliente cliente = clienteDAO.buscarPorCodigo(codigo);

        if (cliente == null) {
            clienteSeleccionado = null;
            lblCliente.setText("Cliente: No seleccionado");
            lblPuntosCliente.setText("Puntos disponibles: 0");

            mostrarError("No se encontró el cliente.");
            return;
        }

        if (!cliente.isActivo()) {
            clienteSeleccionado = null;
            lblCliente.setText("Cliente: No seleccionado");
            lblPuntosCliente.setText("Puntos disponibles: 0");

            mostrarError("El cliente está inactivo.");
            return;
        }

        clienteSeleccionado = cliente;

        lblCliente.setText("Cliente: " + cliente.getNombre());
        lblPuntosCliente.setText(
                "Puntos disponibles: " + cliente.getPuntos()
        );
    }

    private void cargarProductos() {

        cmbProductos.removeAllItems();

        List<Producto> productos = productoDAO.listar();

        for (Producto producto : productos) {

            if (producto.isActivo()
                    && producto.getExistencia() > 0) {

                cmbProductos.addItem(producto);
            }
        }

        if (cmbProductos.getItemCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "No hay productos disponibles o no se pudieron cargar.",
                    "Catálogo",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void agregarProducto() {

        Producto producto = (Producto) cmbProductos.getSelectedItem();

        if (producto == null) {
            mostrarError("Seleccione un producto.");
            return;
        }

        int cantidad = (Integer) spnCantidad.getValue();

        int cantidadAcumulada = cantidad;

        for (DetallePedido detalle : carrito) {

            if (detalle.getItem() instanceof ProductoIndividual individual
                    && individual.getProducto().getIdProducto()
                    == producto.getIdProducto()) {

                cantidadAcumulada += detalle.getCantidad();
            }
        }

        if (!producto.hayExistencia(cantidadAcumulada)) {

            mostrarError(
                    "Inventario insuficiente. Disponible: "
                    + producto.getExistencia()
            );

            return;
        }

        ProductoIndividual item = new ProductoIndividual(producto);

        DetallePedido detalle = new DetallePedido(
                0,
                item,
                cantidad
        );

        carrito.add(detalle);

        actualizarTabla();
    }

    private void eliminarProducto() {

        int fila = tablaDetalles.getSelectedRow();

        if (fila < 0) {
            mostrarError("Seleccione un producto de la tabla.");
            return;
        }

        int posicion = tablaDetalles.convertRowIndexToModel(fila);

        carrito.remove(posicion);

        actualizarTabla();
    }

    private void actualizarTabla() {

        modeloTabla.setRowCount(0);

        double subtotal = 0;
        int puntos = 0;

        for (DetallePedido detalle : carrito) {

            modeloTabla.addRow(new Object[]{
                detalle.getItem().getCodigo(),
                detalle.getItem().getNombre(),
                detalle.getCantidad(),
                String.format("Q%.2f", detalle.getPrecioUnitario()),
                String.format("Q%.2f", detalle.calcularSubtotal()),
                detalle.calcularPuntos()
            });

            subtotal += detalle.calcularSubtotal();
            puntos += detalle.calcularPuntos();
        }

        lblSubtotal.setText(
                String.format("Subtotal: Q%.2f", subtotal)
        );

        lblTotal.setText(
                String.format("Total: Q%.2f", subtotal)
        );

        lblPuntos.setText(
                "Puntos potenciales: " + puntos
        );
    }

    private void confirmarPedido() {

        if (clienteSeleccionado == null) {
            mostrarError("Primero seleccione un cliente.");
            return;
        }

        if (carrito.isEmpty()) {
            mostrarError("Agregue al menos un producto.");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Desea registrar el pedido como PENDIENTE?",
                "Confirmar pedido",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        btnConfirmar.setEnabled(false);

        try {

            Pedido pedido = new Pedido(
                    0,
                    clienteSeleccionado
            );

            for (DetallePedido detalle : carrito) {
                pedido.agregarDetalle(detalle);
            }

            int idPedido = pedidoService.registrarPedido(pedido);

            JOptionPane.showMessageDialog(
                    this,
                    "PEDIDO REGISTRADO CORRECTAMENTE\n"
                    + "ID del pedido: " + idPedido + "\n"
                    + String.format(
                            "Total: Q%.2f\n",
                            pedido.calcularTotal()
                    )
                    + "Puntos potenciales: "
                    + pedido.calcularPuntos() + "\n"
                    + "Estado: PENDIENTE\n\n"
                    + "Los puntos se acreditarán después del pago.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();

        } catch (SQLException | RuntimeException e) {

            mostrarError(
                    "No se pudo registrar el pedido:\n"
                    + e.getMessage()
            );

        } finally {

            btnConfirmar.setEnabled(true);
        }
    }

    private void limpiarFormulario() {

        clienteSeleccionado = null;
        carrito.clear();

        txtCodigoCliente.setText("");
        lblCliente.setText("Cliente: No seleccionado");
        lblPuntosCliente.setText("Puntos disponibles: 0");

        spnCantidad.setValue(1);

        actualizarTabla();
        cargarProductos();
    }

    private void mostrarError(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Aviso",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            FrmPedidos ventana = new FrmPedidos();
            ventana.setVisible(true);

        });
    }
}
