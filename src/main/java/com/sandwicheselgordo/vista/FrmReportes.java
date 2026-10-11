
package com.sandwicheselgordo.vista;

import com.sandwicheselgordo.dao.ReporteDAO;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class FrmReportes extends JFrame {

    private static final Color FONDO =
            new Color(20, 19, 24);

    private static final Color PANEL =
            new Color(32, 30, 38);

    private static final Color DORADO =
            new Color(218, 174, 82);

    private static final Color VERDE =
            new Color(35, 139, 89);

    private static final Color MORADO =
            new Color(112, 66, 166);

    private static final Color BLANCO =
            new Color(245, 241, 232);

    private final ReporteDAO reporteDAO;

    private JComboBox<String> cmbReportes;
    private JTable tablaReportes;
    private DefaultTableModel modeloTabla;
    private JLabel lblEstado;
    private JLabel lblTotal;

    private JButton btnConsultar;
    private JButton btnActualizar;
    private JButton btnLimpiar;

    public FrmReportes() {

        reporteDAO = new ReporteDAO();

        setTitle("Sandwiches El Gordo - Reportes");
        setSize(1150, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        JPanel principal = new JPanel(new BorderLayout(15, 15));
        principal.setBackground(FONDO);
        principal.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        setContentPane(principal);

        JPanel encabezado = new JPanel(new GridLayout(2, 1, 0, 8));
        encabezado.setBackground(FONDO);

        JLabel titulo = new JLabel(
                "SANDWICHES EL GORDO",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font("Georgia", Font.BOLD, 30)
        );
        titulo.setForeground(DORADO);

        JLabel subtitulo = new JLabel(
                "SISTEMA DE REPORTES",
                SwingConstants.CENTER
        );

        subtitulo.setFont(
                new Font("Georgia", Font.BOLD, 19)
        );
        subtitulo.setForeground(BLANCO);

        encabezado.add(titulo);
        encabezado.add(subtitulo);

        principal.add(encabezado, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(10, 15));
        centro.setBackground(FONDO);

        JPanel seleccion = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 12, 12)
        );

        seleccion.setBackground(PANEL);
        seleccion.setBorder(
                BorderFactory.createLineBorder(DORADO)
        );

        JLabel lblSeleccion = new JLabel(
                "SELECCIONE UN REPORTE:"
        );

        lblSeleccion.setForeground(DORADO);
        lblSeleccion.setFont(
                new Font("Georgia", Font.BOLD, 14)
        );

        cmbReportes = new JComboBox<>(new String[]{
            "Ventas realizadas",
            "Ventas por método de pago",
            "Productos más vendidos",
            "Menús vendidos",
            "Inventario bajo",
            "Puntos de clientes",
            "Historial de canjes",
            "Recompensas disponibles"
        });

        cmbReportes.setPreferredSize(
                new Dimension(290, 34)
        );

        cmbReportes.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );

        btnConsultar = crearBoton(
                "CONSULTAR",
                VERDE
        );

        btnConsultar.addActionListener(
                e -> consultarReporte()
        );

        btnActualizar = crearBoton(
                "ACTUALIZAR",
                MORADO
        );

        btnActualizar.addActionListener(
                e -> consultarReporte()
        );

        btnLimpiar = crearBoton(
                "LIMPIAR",
                DORADO
        );

        btnLimpiar.setForeground(Color.BLACK);

        btnLimpiar.addActionListener(
                e -> limpiarTabla()
        );

        seleccion.add(lblSeleccion);
        seleccion.add(cmbReportes);
        seleccion.add(btnConsultar);
        seleccion.add(btnActualizar);
        seleccion.add(btnLimpiar);

        centro.add(seleccion, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaReportes = new JTable(modeloTabla);

        tablaReportes.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );

        tablaReportes.setRowHeight(32);
        tablaReportes.setBackground(PANEL);
        tablaReportes.setForeground(BLANCO);
        tablaReportes.setGridColor(DORADO);

        tablaReportes.setSelectionBackground(MORADO);
        tablaReportes.setSelectionForeground(Color.WHITE);

        tablaReportes.setFillsViewportHeight(true);
        tablaReportes.setAutoCreateRowSorter(true);

        JTableHeader encabezadoTabla =
                tablaReportes.getTableHeader();

        encabezadoTabla.setBackground(DORADO);
        encabezadoTabla.setForeground(Color.BLACK);

        encabezadoTabla.setFont(
                new Font("Georgia", Font.BOLD, 13)
        );

        DefaultTableCellRenderer centrado =
                new DefaultTableCellRenderer();

        centrado.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        tablaReportes.setDefaultRenderer(
                Object.class,
                centrado
        );

        JScrollPane scroll = new JScrollPane(tablaReportes);

        scroll.getViewport().setBackground(PANEL);

        scroll.setBorder(
                BorderFactory.createLineBorder(DORADO, 2)
        );

        centro.add(scroll, BorderLayout.CENTER);

        principal.add(centro, BorderLayout.CENTER);

        JPanel inferior = new JPanel(
                new GridLayout(2, 1, 0, 8)
        );

        inferior.setBackground(FONDO);

        lblTotal = new JLabel(
                "Registros encontrados: 0",
                SwingConstants.CENTER
        );

        lblTotal.setForeground(DORADO);
        lblTotal.setFont(
                new Font("Arial", Font.BOLD, 15)
        );

        lblEstado = new JLabel(
                "Seleccione un reporte y presione CONSULTAR.",
                SwingConstants.CENTER
        );

        lblEstado.setForeground(BLANCO);
        lblEstado.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );

        inferior.add(lblTotal);
        inferior.add(lblEstado);

        principal.add(inferior, BorderLayout.SOUTH);
    }

    private JButton crearBoton(
            String texto,
            Color fondo) {

        JButton boton = new JButton(texto);

        boton.setBackground(fondo);
        boton.setForeground(Color.WHITE);

        boton.setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        boton.setFocusPainted(false);

        boton.setPreferredSize(
                new Dimension(145, 36)
        );

        return boton;
    }

    private void consultarReporte() {

        int seleccion = cmbReportes.getSelectedIndex();

        btnConsultar.setEnabled(false);
        btnActualizar.setEnabled(false);

        lblEstado.setText("Consultando Oracle...");

        javax.swing.SwingWorker<DefaultTableModel, Void> tarea =
                new javax.swing.SwingWorker<>() {

            @Override
            protected DefaultTableModel doInBackground()
                    throws SQLException {

                return switch (seleccion) {

                    case 0 ->
                        reporteDAO.obtenerVentas();

                    case 1 ->
                        reporteDAO.obtenerVentasPorMetodoPago();

                    case 2 ->
                        reporteDAO.obtenerProductosMasVendidos();

                    case 3 ->
                        reporteDAO.obtenerMenusVendidos();

                    case 4 ->
                        reporteDAO.obtenerInventarioBajo();

                    case 5 ->
                        reporteDAO.obtenerPuntosClientes();

                    case 6 ->
                        reporteDAO.obtenerHistorialCanjes();

                    case 7 ->
                        reporteDAO.obtenerRecompensas();

                    default ->
                        throw new IllegalArgumentException(
                                "Reporte no válido."
                        );
                };
            }

            @Override
            protected void done() {

                btnConsultar.setEnabled(true);
                btnActualizar.setEnabled(true);

                try {

                    modeloTabla = get();

                    tablaReportes.setModel(modeloTabla);

                    lblTotal.setText(
                            "Registros encontrados: "
                            + modeloTabla.getRowCount()
                    );

                    lblEstado.setText(
                            "Reporte actualizado correctamente."
                    );

                    if (modeloTabla.getRowCount() == 0) {

                        lblEstado.setText(
                                "Consulta realizada: no hay registros."
                        );
                    }

                } catch (Exception e) {

                    Throwable causa = e.getCause() != null
                            ? e.getCause() : e;

                    lblEstado.setText(
                            "Error al consultar el reporte."
                    );

                    JOptionPane.showMessageDialog(
                            FrmReportes.this,
                            causa.getMessage(),
                            "Error de reportes",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };

        tarea.execute();
    }

    private void limpiarTabla() {

        modeloTabla = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaReportes.setModel(modeloTabla);

        lblTotal.setText(
                "Registros encontrados: 0"
        );

        lblEstado.setText(
                "Tabla limpiada correctamente."
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            FrmReportes ventana = new FrmReportes();
            ventana.setVisible(true);
        });
    }
}
