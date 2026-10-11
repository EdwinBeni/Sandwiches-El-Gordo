
package com.sandwicheselgordo.vista;

import com.sandwicheselgordo.dao.ClienteDAO;
import com.sandwicheselgordo.dao.MenuDao;
import com.sandwicheselgordo.dao.ProductoDAO;
import com.sandwicheselgordo.modelo.Cliente;
import com.sandwicheselgordo.modelo.DetallePedido;
import com.sandwicheselgordo.modelo.ItemVenta;
import com.sandwicheselgordo.modelo.MenuCompleto;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final MenuDao menuDao;
    private final PedidoService pedidoService;

    private Cliente clienteSeleccionado;
    private final List<DetallePedido> carrito;

    private JTextField txtCodigoCliente;
    private JLabel lblCliente;
    private JLabel lblPuntosCliente;

    private JComboBox<String> cmbTipo;
    private JComboBox<ItemVenta> cmbArticulos;
    private JSpinner spnCantidad;

    private JTable tablaDetalles;
    private DefaultTableModel modeloTabla;

    private JLabel lblSubtotal;
    private JLabel lblTotal;
    private JLabel lblPuntos;
    private JLabel lblDescripcion;

    private JButton btnConfirmar;

    private static final Color NEGRO =
            new Color(22, 22, 22);

    private static final Color DORADO =
            new Color(197, 160, 82);

    private static final Color BLANCO =
            new Color(245, 241, 232);

    public FrmPedidos() {

        clienteDAO = new ClienteDAO();
        productoDAO = new ProductoDAO();
        menuDao = new MenuDao();
        pedidoService = new PedidoService();

        carrito = new ArrayList<>();

        configurarVentana();
        crearComponentes();
        cargarArticulos();
    }

    private void configurarVentana() {

        setTitle("Sandwiches El Gordo - Pedidos");
        setSize(1100, 750);
        setMinimumSize(new Dimension(900, 620));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void crearComponentes() {

        JPanel principal = new JPanel(
                new BorderLayout(12, 12)
        );

        principal.setBackground(NEGRO);
        principal.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 18, 18, 18
                )
        );

        JLabel titulo = new JLabel(
                "SANDWICHES EL GORDO - PEDIDOS",
                JLabel.CENTER
        );

        titulo.setFont(
                new Font("Serif", Font.BOLD, 25)
        );

        titulo.setForeground(DORADO);

        principal.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(
                new BorderLayout(12, 12)
        );

        centro.setBackground(NEGRO);

        JPanel formularios = new JPanel(
                new GridLayout(2, 1, 10, 10)
        );

        formularios.setBackground(NEGRO);

        formularios.add(crearPanelCliente());
        formularios.add(crearPanelArticulos());

        centro.add(formularios, BorderLayout.NORTH);
        centro.add(crearPanelTabla(), BorderLayout.CENTER);

        principal.add(centro, BorderLayout.CENTER);
        principal.add(crearPanelResumen(), BorderLayout.SOUTH);

        setContentPane(principal);
    }

    private JPanel crearPanelCliente() {

        JPanel panel = crearPanelSeccion(
                "DATOS DEL CLIENTE"
        );

        panel.setLayout(
                new GridLayout(2, 1, 8, 8)
        );

        JPanel busqueda = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        busqueda.setBackground(NEGRO);

        txtCodigoCliente = new JTextField(15);

        JButton btnBuscar = crearBoton(
                "Buscar cliente"
        );

        btnBuscar.addActionListener(
                e -> buscarCliente()
        );

        busqueda.add(crearEtiqueta("Código:"));
        busqueda.add(txtCodigoCliente);
        busqueda.add(btnBuscar);

        JPanel informacion = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT, 20, 0
                )
        );

        informacion.setBackground(NEGRO);

        lblCliente = crearEtiqueta(
                "Cliente: No seleccionado"
        );

        lblPuntosCliente = crearEtiqueta(
                "Puntos disponibles: 0"
        );

        informacion.add(lblCliente);
        informacion.add(lblPuntosCliente);

        panel.add(busqueda);
        panel.add(informacion);

        return panel;
    }

    private JPanel crearPanelArticulos() {

        JPanel panel = crearPanelSeccion(
                "SELECCIÓN DE PRODUCTOS Y MENÚS"
        );

        panel.setLayout(
                new GridLayout(2, 1, 5, 5)
        );

        JPanel controles = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 8)
        );

        controles.setBackground(NEGRO);

        cmbTipo = new JComboBox<>(
                new String[]{
                    "Producto individual",
                    "Menú completo"
                }
        );

        cmbArticulos = new JComboBox<>();

        cmbArticulos.setPreferredSize(
                new Dimension(330, 30)
        );

        spnCantidad = new JSpinner(
                new SpinnerNumberModel(1, 1, 999, 1)
        );

        JButton btnAgregar = crearBoton(
                "Agregar al pedido"
        );

        JButton btnRecargar = crearBoton(
                "Actualizar catálogo"
        );

        cmbTipo.addActionListener(
                e -> cargarArticulos()
        );

        cmbArticulos.addActionListener(
                e -> actualizarDescripcion()
        );

        btnAgregar.addActionListener(
                e -> agregarArticulo()
        );

        btnRecargar.addActionListener(
                e -> cargarArticulos()
        );

        controles.add(crearEtiqueta("Tipo:"));
        controles.add(cmbTipo);
        controles.add(crearEtiqueta("Artículo:"));
        controles.add(cmbArticulos);
        controles.add(crearEtiqueta("Cantidad:"));
        controles.add(spnCantidad);
        controles.add(btnAgregar);
        controles.add(btnRecargar);

        JPanel informacion = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        informacion.setBackground(NEGRO);

        lblDescripcion = crearEtiqueta(
                "Seleccione un artículo."
        );

        informacion.add(lblDescripcion);

        panel.add(controles);
        panel.add(informacion);

        return panel;
    }

    private JPanel crearPanelTabla() {

        JPanel panel = crearPanelSeccion(
                "DETALLE DEL PEDIDO"
        );

        panel.setLayout(
                new BorderLayout(8, 8)
        );

        modeloTabla = new DefaultTableModel(
                new Object[]{
                    "Tipo",
                    "Código",
                    "Artículo",
                    "Cantidad",
                    "Precio",
                    "Subtotal",
                    "Puntos"
                },
                0
        ) {

            @Override
            public boolean isCellEditable(
                    int fila, int columna) {

                return false;
            }
        };

        tablaDetalles = new JTable(modeloTabla);
        tablaDetalles.setRowHeight(28);
        tablaDetalles.setSelectionBackground(DORADO);
        tablaDetalles.setSelectionForeground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(
                tablaDetalles
        );

        JButton btnEliminar = crearBoton(
                "Eliminar seleccionado"
        );

        btnEliminar.addActionListener(
                e -> eliminarArticulo()
        );

        JPanel acciones = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );

        acciones.setBackground(NEGRO);
        acciones.add(btnEliminar);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(acciones, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelResumen() {

        JPanel panel = crearPanelSeccion(
                "RESUMEN DE COMPRA"
        );

        panel.setLayout(
                new GridLayout(2, 1, 8, 8)
        );

        JPanel totales = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER, 35, 5
                )
        );

        totales.setBackground(NEGRO);

        lblSubtotal = crearEtiqueta(
                "Subtotal: Q0.00"
        );

        lblTotal = crearEtiqueta(
                "Total: Q0.00"
        );

        lblPuntos = crearEtiqueta(
                "Puntos potenciales: 0"
        );

        totales.add(lblSubtotal);
        totales.add(lblTotal);
        totales.add(lblPuntos);

        JPanel acciones = new JPanel(
                new FlowLayout(FlowLayout.CENTER)
        );

        acciones.setBackground(NEGRO);

        btnConfirmar = crearBoton(
                "CONFIRMAR PEDIDO"
        );

        btnConfirmar.addActionListener(
                e -> confirmarPedido()
        );

        JButton btnLimpiar = crearBoton(
                "Nueva compra"
        );

        btnLimpiar.addActionListener(
                e -> limpiarFormulario()
        );

        acciones.add(btnConfirmar);
        acciones.add(btnLimpiar);

        panel.add(totales);
        panel.add(acciones);

        return panel;
    }

    private JPanel crearPanelSeccion(
            String titulo) {

        JPanel panel = new JPanel();
        panel.setBackground(NEGRO);

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                DORADO
                        ),
                        titulo,
                        0,
                        0,
                        new Font(
                                "Serif", Font.BOLD, 15
                        ),
                        DORADO
                )
        );

        return panel;
    }

    private JLabel crearEtiqueta(String texto) {

        JLabel etiqueta = new JLabel(texto);

        etiqueta.setForeground(BLANCO);
        etiqueta.setFont(
                new Font(
                        "SansSerif", Font.PLAIN, 14
                )
        );

        return etiqueta;
    }

    private JButton crearBoton(String texto) {

        JButton boton = new JButton(texto);

        boton.setBackground(DORADO);
        boton.setForeground(Color.BLACK);
        boton.setFont(
                new Font(
                        "SansSerif", Font.BOLD, 13
                )
        );

        boton.setFocusPainted(false);

        return boton;
    }

    private void buscarCliente() {

        String codigo =
                txtCodigoCliente.getText().trim();

        if (codigo.isEmpty()) {
            mostrarError(
                    "Ingrese el código del cliente."
            );
            return;
        }

        Cliente cliente =
                clienteDAO.buscarPorCodigo(codigo);

        if (cliente == null || !cliente.isActivo()) {

            clienteSeleccionado = null;

            lblCliente.setText(
                    "Cliente: No seleccionado"
            );

            lblPuntosCliente.setText(
                    "Puntos disponibles: 0"
            );

            mostrarError(
                    "Cliente no encontrado o inactivo."
            );

            return;
        }

        clienteSeleccionado = cliente;

        lblCliente.setText(
                "Cliente: " + cliente.getNombre()
        );

        lblPuntosCliente.setText(
                "Puntos disponibles: "
                + cliente.getPuntos()
        );
    }

    private void cargarArticulos() {

        if (cmbArticulos == null
                || cmbTipo == null) {
            return;
        }

        cmbArticulos.removeAllItems();

        boolean esMenu =
                cmbTipo.getSelectedIndex() == 1;

        try {

            if (esMenu) {

                for (MenuCompleto menu :
                        menuDao.listar()) {

                    if (menu.isActivo()
                            && menu.hayExistencia(1)) {

                        cmbArticulos.addItem(menu);
                    }
                }

            } else {

                for (Producto producto :
                        productoDAO.listar()) {

                    if (producto.isActivo()
                            && producto.hayExistencia(1)) {

                        cmbArticulos.addItem(
                                new ProductoIndividual(
                                        producto
                                )
                        );
                    }
                }
            }

        } catch (SQLException e) {

            mostrarError(
                    "Error al cargar menús:\n"
                    + e.getMessage()
            );
        }

        actualizarDescripcion();
    }

    private void actualizarDescripcion() {

        if (lblDescripcion == null
                || cmbArticulos == null) {
            return;
        }

        ItemVenta item =
                (ItemVenta) cmbArticulos.getSelectedItem();

        if (item == null) {

            lblDescripcion.setText(
                    "No hay artículos disponibles."
            );

            return;
        }

        if (item instanceof MenuCompleto menu) {

            lblDescripcion.setText(
                    "Menú: "
                    + menu.getSandwich().getNombre()
                    + " + "
                    + menu.getBebida().getNombre()
                    + " + "
                    + menu.getAcompanamiento().getNombre()
                    + " | Q"
                    + String.format("%.2f", menu.getPrecio())
                    + " | 8 puntos"
            );

        } else {

            lblDescripcion.setText(
                    "Producto: "
                    + item.getNombre()
                    + " | Q"
                    + String.format("%.2f", item.getPrecio())
                    + " | "
                    + item.calcularPuntos()
                    + " puntos"
            );
        }
    }

    private void agregarArticulo() {

        ItemVenta item =
                (ItemVenta) cmbArticulos.getSelectedItem();

        if (item == null) {

            mostrarError(
                    "Seleccione un artículo."
            );

            return;
        }

        int cantidad =
                (Integer) spnCantidad.getValue();

        if (!item.isActivo()) {

            mostrarError(
                    "El artículo está inactivo."
            );

            return;
        }

        if (!item.hayExistencia(cantidad)) {

            mostrarError(
                    "No hay existencia suficiente."
            );

            return;
        }

        DetallePedido nuevo = new DetallePedido(
                0,
                item,
                cantidad
        );

        carrito.add(nuevo);

        String error = validarInventarioCarrito();

        if (error != null) {

            carrito.remove(carrito.size() - 1);

            mostrarError(error);

            return;
        }

        actualizarTabla();
    }

    private String validarInventarioCarrito() {

        Map<Integer, Integer> cantidades =
                new HashMap<>();

        Map<Integer, Producto> productos =
                new HashMap<>();

        for (DetallePedido detalle : carrito) {

            ItemVenta item = detalle.getItem();
            int cantidad = detalle.getCantidad();

            if (item instanceof ProductoIndividual individual) {

                acumularProducto(
                        individual.getProducto(),
                        cantidad,
                        cantidades,
                        productos
                );

            } else if (item instanceof MenuCompleto menu) {

                acumularProducto(
                        menu.getSandwich(),
                        cantidad,
                        cantidades,
                        productos
                );

                acumularProducto(
                        menu.getBebida(),
                        cantidad,
                        cantidades,
                        productos
                );

                acumularProducto(
                        menu.getAcompanamiento(),
                        cantidad,
                        cantidades,
                        productos
                );
            }
        }

        for (Map.Entry<Integer, Integer> entrada :
                cantidades.entrySet()) {

            Producto producto =
                    productos.get(entrada.getKey());

            int solicitado = entrada.getValue();

            if (!producto.isActivo()) {

                return "Producto inactivo: "
                        + producto.getNombre();
            }

            if (!producto.hayExistencia(solicitado)) {

                return "Inventario insuficiente para "
                        + producto.getNombre()
                        + "\nSolicitado: "
                        + solicitado
                        + "\nDisponible: "
                        + producto.getExistencia();
            }
        }

        return null;
    }

    private void acumularProducto(
            Producto producto,
            int cantidad,
            Map<Integer, Integer> cantidades,
            Map<Integer, Producto> productos) {

        if (producto == null) {

            throw new IllegalArgumentException(
                    "El artículo tiene un componente nulo."
            );
        }

        int id = producto.getIdProducto();

        cantidades.merge(
                id,
                cantidad,
                Integer::sum
        );

        productos.put(id, producto);
    }

    private void eliminarArticulo() {

        int fila = tablaDetalles.getSelectedRow();

        if (fila < 0) {

            mostrarError(
                    "Seleccione un artículo de la tabla."
            );

            return;
        }

        int posicion =
                tablaDetalles.convertRowIndexToModel(fila);

        carrito.remove(posicion);

        actualizarTabla();
    }

    private void actualizarTabla() {

        modeloTabla.setRowCount(0);

        double subtotal = 0;
        int puntos = 0;

        for (DetallePedido detalle : carrito) {

            ItemVenta item = detalle.getItem();

            String tipo =
                    item instanceof MenuCompleto
                    ? "MENÚ"
                    : "PRODUCTO";

            modeloTabla.addRow(
                    new Object[]{
                        tipo,
                        item.getCodigo(),
                        item.getNombre(),
                        detalle.getCantidad(),
                        String.format(
                                "Q%.2f",
                                detalle.getPrecioUnitario()
                        ),
                        String.format(
                                "Q%.2f",
                                detalle.calcularSubtotal()
                        ),
                        detalle.calcularPuntos()
                    }
            );

            subtotal += detalle.calcularSubtotal();
            puntos += detalle.calcularPuntos();
        }

        lblSubtotal.setText(
                String.format(
                        "Subtotal: Q%.2f",
                        subtotal
                )
        );

        lblTotal.setText(
                String.format(
                        "Total: Q%.2f",
                        subtotal
                )
        );

        lblPuntos.setText(
                "Puntos potenciales: " + puntos
        );
    }

    private void confirmarPedido() {

        if (clienteSeleccionado == null) {

            mostrarError(
                    "Primero seleccione un cliente."
            );

            return;
        }

        if (carrito.isEmpty()) {

            mostrarError(
                    "Agregue al menos un artículo."
            );

            return;
        }

        String error = validarInventarioCarrito();

        if (error != null) {

            mostrarError(error);

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Registrar el pedido como PENDIENTE?",
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

            int idPedido =
                    pedidoService.registrarPedido(pedido);

            JOptionPane.showMessageDialog(
                    this,
                    "PEDIDO REGISTRADO CORRECTAMENTE\n"
                    + "ID: " + idPedido + "\n"
                    + String.format(
                            "Total: Q%.2f\n",
                            pedido.calcularTotal()
                    )
                    + "Puntos potenciales: "
                    + pedido.calcularPuntos()
                    + "\nEstado: PENDIENTE\n\n"
                    + "Los puntos se acreditarán "
                    + "después del pago.",
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

        lblCliente.setText(
                "Cliente: No seleccionado"
        );

        lblPuntosCliente.setText(
                "Puntos disponibles: 0"
        );

        spnCantidad.setValue(1);

        actualizarTabla();
        cargarArticulos();
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
