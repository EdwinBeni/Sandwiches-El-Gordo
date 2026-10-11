
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
import com.sandwicheselgordo.servicio.PagoService;
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
    private final PagoService pagoService;

    private Cliente clienteSeleccionado;
    private final List<DetallePedido> carrito;

    private Integer idPedidoPendientePago;
    private double totalPedidoPendiente;

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
    private JLabel lblEstadoPago;

    private JButton btnConfirmar;
    private JButton btnCobrarPendiente;

    private static final Color NEGRO =
            new Color(22, 22, 22);

    private static final Color DORADO =
            new Color(197, 160, 82);

    private static final Color BLANCO =
            new Color(245, 241, 232);

    private static final Color VERDE = new Color(25, 135, 84);
    private static final Color MORADO = new Color(117, 66, 168);
    private static final Color ROJO = new Color(150, 63, 80);

    public FrmPedidos() {

        clienteDAO = new ClienteDAO();
        productoDAO = new ProductoDAO();
        menuDao = new MenuDao();
        pedidoService = new PedidoService();
        pagoService = new PagoService();

        carrito = new ArrayList<>();

        idPedidoPendientePago = null;
        totalPedidoPendiente = 0;

        configurarVentana();
        crearComponentes();
        cargarArticulos();
        actualizarEstadoPago();
    }

    private void configurarVentana() {

        setTitle("Sandwiches El Gordo - Pedidos y Pagos");
        setSize(1150, 800);
        setMinimumSize(new Dimension(950, 680));
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
                new FlowLayout(FlowLayout.LEFT, 20, 0)
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
        tablaDetalles.setSelectionBackground(MORADO);
        tablaDetalles.setSelectionForeground(Color.WHITE);
        tablaDetalles.setGridColor(DORADO);
        tablaDetalles.getTableHeader().setBackground(DORADO);
        tablaDetalles.getTableHeader().setForeground(Color.BLACK);

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
                "RESUMEN DE COMPRA Y PAGO"
        );

        panel.setLayout(
                new GridLayout(3, 1, 5, 5)
        );

        JPanel totales = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 35, 5)
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

        btnConfirmar.setBackground(VERDE);
        btnConfirmar.setForeground(Color.WHITE);

        btnConfirmar.addActionListener(
                e -> confirmarPedido()
        );

        btnCobrarPendiente = crearBoton(
                "COBRAR PEDIDO PENDIENTE"
        );

        btnCobrarPendiente.setBackground(MORADO);
        btnCobrarPendiente.setForeground(Color.WHITE);

        btnCobrarPendiente.addActionListener(
                e -> cobrarPedidoPendiente()
        );

        JButton btnLimpiar = crearBoton(
                "Nueva compra"
        );

        btnLimpiar.setBackground(DORADO);
        btnLimpiar.setForeground(Color.BLACK);

        btnLimpiar.addActionListener(
                e -> nuevaCompra()
        );

        acciones.add(btnConfirmar);
        acciones.add(btnCobrarPendiente);
        acciones.add(btnLimpiar);

        JPanel estado = new JPanel(
                new FlowLayout(FlowLayout.CENTER)
        );
        estado.setBackground(NEGRO);

        lblEstadoPago = crearEtiqueta(
                "No hay pedidos pendientes de pago."
        );

        lblEstadoPago.setForeground(DORADO);
        estado.add(lblEstadoPago);

        panel.add(totales);
        panel.add(acciones);
        panel.add(estado);

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
        etiqueta.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );

        return etiqueta;
    }

    private JButton crearBoton(String texto) {

        JButton boton = EstiloBotones.crear(texto);
        boton.setBackground(DORADO);
        boton.setForeground(Color.BLACK);
        boton.setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );
        boton.setFocusPainted(false);

        return boton;
    }

    private void buscarCliente() {

        if (idPedidoPendientePago != null) {
            mostrarError(
                    "Primero complete el pago del pedido #"
                    + idPedidoPendientePago
                    + " o seleccione Nueva compra."
            );
            return;
        }

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

        if (cmbArticulos == null || cmbTipo == null) {
            return;
        }

        cmbArticulos.removeAllItems();

        boolean esMenu =
                cmbTipo.getSelectedIndex() == 1;

        try {

            if (esMenu) {

                for (MenuCompleto menu : menuDao.listar()) {

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
                                new ProductoIndividual(producto)
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

        if (idPedidoPendientePago != null) {
            mostrarError(
                    "Primero complete o posponga "
                    + "el pedido pendiente."
            );
            return;
        }

        ItemVenta item =
                (ItemVenta) cmbArticulos.getSelectedItem();

        if (item == null) {
            mostrarError("Seleccione un artículo.");
            return;
        }

        int cantidad =
                (Integer) spnCantidad.getValue();

        if (!item.isActivo()) {
            mostrarError("El artículo está inactivo.");
            return;
        }

        if (!item.hayExistencia(cantidad)) {
            mostrarError(
                    "No hay existencia suficiente."
            );
            return;
        }

        DetallePedido nuevo = new DetallePedido(
                0, item, cantidad
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
                id, cantidad, Integer::sum
        );

        productos.put(id, producto);
    }

    private void eliminarArticulo() {

        if (idPedidoPendientePago != null) {
            mostrarError(
                    "El pedido ya fue registrado. "
                    + "No puede modificar sus detalles."
            );
            return;
        }

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
                        "Subtotal: Q%.2f", subtotal
                )
        );

        lblTotal.setText(
                String.format(
                        "Total: Q%.2f", subtotal
                )
        );

        lblPuntos.setText(
                "Puntos potenciales: " + puntos
        );
    }

    private void confirmarPedido() {

        if (idPedidoPendientePago != null) {
            mostrarError(
                    "Ya existe un pedido pendiente de pago: #"
                    + idPedidoPendientePago
                    + "\nUtilice COBRAR PEDIDO PENDIENTE."
            );
            return;
        }

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

        int respuesta = JOptionPane.showConfirmDialog(
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
                    0, clienteSeleccionado
            );

            for (DetallePedido detalle : carrito) {
                pedido.agregarDetalle(detalle);
            }

            int idPedido =
                    pedidoService.registrarPedido(pedido);

            idPedidoPendientePago = idPedido;
            totalPedidoPendiente = pedido.calcularTotal();

            actualizarEstadoPago();

            JOptionPane.showMessageDialog(
                    this,
                    "PEDIDO REGISTRADO CORRECTAMENTE\n"
                    + "ID: " + idPedido + "\n"
                    + String.format(
                            "Total: Q%.2f\n",
                            totalPedidoPendiente
                    )
                    + "Puntos potenciales: "
                    + pedido.calcularPuntos()
                    + "\nEstado: PENDIENTE\n\n"
                    + "Los puntos se acreditarán "
                    + "después del pago.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cobrarPedidoPendiente();

        } catch (SQLException | RuntimeException e) {

            mostrarError(
                    "No se pudo registrar el pedido:\n"
                    + e.getMessage()
            );

        } finally {

            actualizarEstadoPago();
        }
    }

    private void cobrarPedidoPendiente() {

        if (idPedidoPendientePago == null) {

            String entrada = JOptionPane.showInputDialog(
                    this,
                    "Ingrese el ID de un pedido pendiente:"
            );

            if (entrada == null) {
                return;
            }

            int id;

            try {
                id = Integer.parseInt(entrada.trim());

                if (id <= 0) {
                    throw new NumberFormatException();
                }

            } catch (NumberFormatException e) {
                mostrarError("ID de pedido inválido.");
                return;
            }

            mostrarError(
                    "Para cobrar el pedido #" + id
                    + " desde otra sesión necesitamos "
                    + "consultar su total en Oracle.\n"
                    + "Por ahora, utilice esta opción "
                    + "para pedidos creados en esta ventana."
            );

            return;
        }

        cobrarPedido(
                idPedidoPendientePago,
                totalPedidoPendiente
        );
    }

    private void cobrarPedido(
            int idPedido,
            double total) {

        String[] opciones = {
            "Efectivo",
            "Tarjeta aprobada",
            "Tarjeta rechazada",
            "Pagar después"
        };

        int opcion = JOptionPane.showOptionDialog(
                this,
                String.format(
                        "Pedido #%d\nTotal: Q%.2f\n"
                        + "Seleccione el método de pago:",
                        idPedido, total
                ),
                "Cobrar pedido",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (opcion < 0 || opcion == 3) {
            actualizarEstadoPago();
            return;
        }

        try {

            boolean aprobado;

            if (opcion == 0) {

                String entrada =
                        JOptionPane.showInputDialog(
                                this,
                                String.format(
                                        "Total: Q%.2f\n"
                                        + "Ingrese el efectivo recibido:",
                                        total
                                )
                        );

                if (entrada == null) {
                    return;
                }

                double recibido =
                        Double.parseDouble(
                                entrada.trim()
                        );

                if (!Double.isFinite(recibido)
                        || recibido < total) {

                    mostrarError(
                            "El efectivo es insuficiente "
                            + "o inválido."
                    );
                    return;
                }

                aprobado = pagoService.pagarEfectivo(
                        idPedido, recibido
                );

                if (aprobado) {

                    JOptionPane.showMessageDialog(
                            this,
                            String.format(
                                    "PAGO APROBADO\n\n"
                                    + "Pedido: %d\n"
                                    + "Total: Q%.2f\n"
                                    + "Recibido: Q%.2f\n"
                                    + "Cambio: Q%.2f\n\n"
                                    + "Inventario y puntos actualizados.",
                                    idPedido,
                                    total,
                                    recibido,
                                    recibido - total
                            ),
                            "Pago exitoso",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }

            } else {

                String referencia =
                        JOptionPane.showInputDialog(
                                this,
                                "Ingrese la referencia de tarjeta:"
                        );

                if (referencia == null) {
                    return;
                }

                referencia = referencia.trim();

                if (referencia.isEmpty()) {
                    mostrarError(
                            "La referencia es obligatoria."
                    );
                    return;
                }

                aprobado = pagoService.pagarTarjeta(
                        idPedido,
                        referencia,
                        opcion == 1
                );

                if (aprobado) {

                    JOptionPane.showMessageDialog(
                            this,
                            "PAGO APROBADO CORRECTAMENTE\n\n"
                            + "Pedido: " + idPedido
                            + "\nReferencia: " + referencia
                            + "\nInventario y puntos actualizados.",
                            "Pago exitoso",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "PAGO RECHAZADO\n\n"
                            + "Pedido: " + idPedido
                            + "\nPuede reintentar el pago "
                            + "sin registrar otro pedido.",
                            "Pago rechazado",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }

            if (aprobado) {

                idPedidoPendientePago = null;
                totalPedidoPendiente = 0;

                limpiarFormulario();
            }

        } catch (NumberFormatException e) {

            mostrarError(
                    "Ingrese una cantidad numérica válida."
            );

        } catch (SQLException | IllegalArgumentException e) {

            mostrarError(
                    "Error al procesar el pago:\n"
                    + e.getMessage()
                    + "\n\nVerifique el estado del pedido "
                    + "en Oracle antes de reintentar."
            );

        } finally {

            actualizarEstadoPago();
        }
    }

    private void actualizarEstadoPago() {

        if (btnConfirmar == null
                || btnCobrarPendiente == null
                || lblEstadoPago == null) {
            return;
        }

        boolean pendiente =
                idPedidoPendientePago != null;

        btnConfirmar.setEnabled(!pendiente);
        btnCobrarPendiente.setEnabled(pendiente);

        if (pendiente) {

            lblEstadoPago.setText(
                    String.format(
                            "PEDIDO #%d PENDIENTE DE PAGO"
                            + " - TOTAL: Q%.2f",
                            idPedidoPendientePago,
                            totalPedidoPendiente
                    )
            );

        } else {

            lblEstadoPago.setText(
                    "No hay pedidos pendientes de pago."
            );
        }
    }

    private void nuevaCompra() {

        if (idPedidoPendientePago != null) {

            int respuesta =
                    JOptionPane.showConfirmDialog(
                            this,
                            "El pedido #"
                            + idPedidoPendientePago
                            + " continúa PENDIENTE en Oracle.\n"
                            + "Si inicia otra compra, podrá "
                            + "recuperarlo posteriormente "
                            + "desde la base de datos.\n\n"
                            + "¿Desea continuar?",
                            "Pedido sin pagar",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }

            idPedidoPendientePago = null;
            totalPedidoPendiente = 0;
        }

        limpiarFormulario();
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
        actualizarEstadoPago();
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
