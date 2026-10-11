
package com.sandwicheselgordo.vista;

import com.sandwicheselgordo.servicio.PedidoService;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class FrmGestionPedidos extends JFrame {

    private static final Color FONDO = new Color(20, 20, 24);
    private static final Color PANEL = new Color(31, 31, 38);
    private static final Color DORADO = new Color(212, 175, 95);
    private static final Color VERDE = new Color(35, 135, 85);
    private static final Color MORADO = new Color(105, 65, 160);
    private static final Color ROJO = new Color(160, 55, 65);
    private static final Color BLANCO = new Color(245, 240, 230);

    private final PedidoService pedidoService;

    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;

    private JLabel lblResumen;
    private JLabel lblEstado;

    private JButton btnEntregar;
    private JButton btnAnular;
    private JButton btnActualizar;

    public FrmGestionPedidos() {

        pedidoService = new PedidoService();

        configurarVentana();
        construirInterfaz();
        cargarPedidos();
    }

    private void configurarVentana() {

        setTitle("Sandwiches El Gordo - Gestion de Pedidos");
        setSize(1120, 680);
        setMinimumSize(new Dimension(850, 520));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        getContentPane().setBackground(FONDO);
        setLayout(new BorderLayout(15, 15));
    }

    private void construirInterfaz() {

        JPanel principal = new JPanel(new BorderLayout(15, 15));
        principal.setBackground(FONDO);
        principal.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        principal.add(crearEncabezado(), BorderLayout.NORTH);
        principal.add(crearPanelTabla(), BorderLayout.CENTER);
        principal.add(crearPanelInferior(), BorderLayout.SOUTH);

        add(principal);
    }

    private JPanel crearEncabezado() {

        JPanel encabezado = new JPanel(new GridLayout(3, 1, 0, 8));
        encabezado.setBackground(FONDO);

        JLabel titulo = new JLabel(
                "SANDWICHES EL GORDO",
                SwingConstants.CENTER
        );

        titulo.setForeground(DORADO);
        titulo.setFont(new Font("Serif", Font.BOLD, 29));

        JLabel subtitulo = new JLabel(
                "GESTION DE ESTADOS DE PEDIDOS",
                SwingConstants.CENTER
        );

        subtitulo.setForeground(BLANCO);
        subtitulo.setFont(new Font("Serif", Font.BOLD, 19));

        lblResumen = new JLabel(
                "Cargando pedidos...",
                SwingConstants.CENTER
        );

        lblResumen.setForeground(DORADO);
        lblResumen.setFont(new Font("SansSerif", Font.PLAIN, 14));

        encabezado.add(titulo);
        encabezado.add(subtitulo);
        encabezado.add(lblResumen);

        return encabezado;
    }

    private JPanel crearPanelTabla() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(PANEL);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(DORADO, 2),
                        BorderFactory.createEmptyBorder(12, 12, 12, 12)
                )
        );

        String[] columnas = {
            "ID Pedido",
            "Cliente",
            "Fecha",
            "Total",
            "Estado",
            "Puntos"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaPedidos = new JTable(modeloTabla);

        tablaPedidos.setRowHeight(34);
        tablaPedidos.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaPedidos.setForeground(BLANCO);
        tablaPedidos.setBackground(PANEL);
        tablaPedidos.setSelectionBackground(MORADO);
        tablaPedidos.setSelectionForeground(Color.WHITE);
        tablaPedidos.setGridColor(new Color(65, 65, 75));

        tablaPedidos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaPedidos.setAutoCreateRowSorter(true);
        tablaPedidos.setFillsViewportHeight(true);

        JTableHeader encabezado = tablaPedidos.getTableHeader();

        encabezado.setBackground(DORADO);
        encabezado.setForeground(Color.BLACK);
        encabezado.setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        tablaPedidos.setDefaultRenderer(
                Object.class,
                new DefaultTableCellRenderer() {

                    @Override
                    public java.awt.Component getTableCellRendererComponent(
                            JTable tabla,
                            Object valor,
                            boolean seleccionado,
                            boolean tieneFoco,
                            int fila,
                            int columna) {

                        super.getTableCellRendererComponent(
                                tabla,
                                valor,
                                seleccionado,
                                tieneFoco,
                                fila,
                                columna
                        );

                        setHorizontalAlignment(SwingConstants.CENTER);
                        setOpaque(true);

                        if (seleccionado) {
                            setBackground(MORADO);
                            setForeground(Color.WHITE);
                        } else {

                            setBackground(
                                    fila % 2 == 0
                                    ? PANEL
                                    : new Color(39, 39, 47)
                            );

                            setForeground(BLANCO);

                            if (columna == 4) {

                                String estado = String.valueOf(valor);

                                switch (estado) {

                                    case "PAGADO", "ENTREGADO" ->
                                        setForeground(
                                                new Color(90, 225, 145)
                                        );

                                    case "PENDIENTE" ->
                                        setForeground(DORADO);

                                    case "ANULADO" ->
                                        setForeground(
                                                new Color(245, 110, 120)
                                        );

                                    default -> setForeground(BLANCO);
                                }
                            }
                        }

                        return this;
                    }
                }
        );

        JScrollPane scroll = new JScrollPane(tablaPedidos);
        scroll.getViewport().setBackground(PANEL);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelInferior() {

        JPanel inferior = new JPanel(new BorderLayout(10, 15));
        inferior.setBackground(FONDO);

        JPanel botones = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 18, 8)
        );

        botones.setBackground(FONDO);

        btnEntregar = crearBoton(
                "ENTREGAR PEDIDO",
                VERDE
        );

        btnAnular = crearBoton(
                "ANULAR PEDIDO",
                ROJO
        );

        btnActualizar = crearBoton(
                "ACTUALIZAR PEDIDOS",
                MORADO
        );

        btnEntregar.addActionListener(e -> entregarPedido());
        btnAnular.addActionListener(e -> anularPedido());
        btnActualizar.addActionListener(e -> cargarPedidos());

        botones.add(btnEntregar);
        botones.add(btnAnular);
        botones.add(btnActualizar);

        lblEstado = new JLabel(
                "Seleccione un pedido para administrar su estado.",
                SwingConstants.CENTER
        );

        lblEstado.setForeground(DORADO);
        lblEstado.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );

        inferior.add(botones, BorderLayout.CENTER);
        inferior.add(lblEstado, BorderLayout.SOUTH);

        return inferior;
    }

    private JButton crearBoton(String texto, Color color) {

        JButton boton = EstiloBotones.crear(texto);

        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(205, 42));
        boton.setCursor(
                new java.awt.Cursor(
                        java.awt.Cursor.HAND_CURSOR
                )
        );

        return boton;
    }

    private void cargarPedidos() {

        try {

            List<String[]> pedidos = pedidoService.listarPedidos();

            modeloTabla.setRowCount(0);

            int pendientes = 0;
            int pagados = 0;
            int entregados = 0;
            int anulados = 0;

            for (String[] pedido : pedidos) {

                modeloTabla.addRow(new Object[]{
                    pedido[0],
                    pedido[1],
                    pedido[2],
                    "Q" + pedido[3],
                    pedido[4],
                    pedido[5]
                });

                switch (pedido[4]) {

                    case "PENDIENTE" -> pendientes++;
                    case "PAGADO" -> pagados++;
                    case "ENTREGADO" -> entregados++;
                    case "ANULADO" -> anulados++;
                }
            }

            lblResumen.setText(
                    "Total: " + pedidos.size()
                    + "     |     Pendientes: " + pendientes
                    + "     |     Pagados: " + pagados
                    + "     |     Entregados: " + entregados
                    + "     |     Anulados: " + anulados
            );

            lblEstado.setText(
                    "Pedidos actualizados correctamente."
            );

        } catch (SQLException ex) {

            mostrarError(
                    "No se pudieron cargar los pedidos:\n"
                    + ex.getMessage()
            );
        }
    }

    private int obtenerIdSeleccionado() {

        int filaVista = tablaPedidos.getSelectedRow();

        if (filaVista == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione primero un pedido de la tabla.",
                    "Pedido no seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );

            return -1;
        }

        int filaModelo = tablaPedidos.convertRowIndexToModel(
                filaVista
        );

        return Integer.parseInt(
                modeloTabla.getValueAt(filaModelo, 0).toString()
        );
    }

    private String obtenerEstadoSeleccionado() {

        int filaVista = tablaPedidos.getSelectedRow();

        if (filaVista == -1) {
            return null;
        }

        int filaModelo = tablaPedidos.convertRowIndexToModel(
                filaVista
        );

        return modeloTabla.getValueAt(
                filaModelo, 4
        ).toString();
    }

    private void entregarPedido() {

        int idPedido = obtenerIdSeleccionado();

        if (idPedido == -1) {
            return;
        }

        String estado = obtenerEstadoSeleccionado();

        if (!"PAGADO".equals(estado)) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se puede entregar el pedido "
                    + idPedido
                    + ".\nEstado actual: " + estado
                    + "\nSolo se pueden entregar pedidos PAGADOS.",
                    "Operacion no permitida",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Confirmar entrega del pedido "
                + idPedido + "?",
                "Confirmar entrega",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            boolean actualizado =
                    pedidoService.entregarPedido(idPedido);

            if (actualizado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Pedido " + idPedido
                        + " marcado como ENTREGADO.",
                        "Entrega exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "El pedido ya no se encuentra en estado PAGADO."
                        + "\nActualice la tabla.",
                        "No se pudo entregar",
                        JOptionPane.WARNING_MESSAGE
                );
            }

            cargarPedidos();

        } catch (SQLException ex) {

            mostrarError(
                    "Error al entregar pedido:\n"
                    + ex.getMessage()
            );
        }
    }

    private void anularPedido() {

        int idPedido = obtenerIdSeleccionado();

        if (idPedido == -1) {
            return;
        }

        String estado = obtenerEstadoSeleccionado();

        if (!"PENDIENTE".equals(estado)) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se puede anular el pedido "
                    + idPedido
                    + ".\nEstado actual: " + estado
                    + "\nSolo se pueden anular pedidos PENDIENTES.",
                    "Operacion no permitida",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que desea ANULAR el pedido "
                + idPedido + "?\n"
                + "Esta accion no se puede revertir.",
                "Confirmar anulacion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            boolean actualizado =
                    pedidoService.anularPedido(idPedido);

            if (actualizado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Pedido " + idPedido
                        + " ANULADO correctamente.",
                        "Anulacion exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "El pedido ya no se encuentra PENDIENTE."
                        + "\nActualice la tabla.",
                        "No se pudo anular",
                        JOptionPane.WARNING_MESSAGE
                );
            }

            cargarPedidos();

        } catch (SQLException ex) {

            mostrarError(
                    "Error al anular pedido:\n"
                    + ex.getMessage()
            );
        }
    }

    private void mostrarError(String mensaje) {

        lblEstado.setText("Error de operacion.");

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new FrmGestionPedidos().setVisible(true);

        });
    }
}
