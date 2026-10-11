
package com.sandwicheselgordo.vista;

import com.sandwicheselgordo.dao.ClienteDAO;
import com.sandwicheselgordo.modelo.Cliente;
import com.sandwicheselgordo.servicio.CanjeService;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class FrmCanjes extends JFrame {

    private static final Color FONDO =
            new Color(20, 19, 23);

    private static final Color PANEL =
            new Color(31, 29, 36);

    private static final Color DORADO =
            new Color(211, 169, 85);

    private static final Color BLANCO =
            new Color(245, 242, 238);

    private static final Color VERDE =
            new Color(35, 135, 87);

    private static final Color MORADO =
            new Color(105, 62, 157);

    private final ClienteDAO clienteDAO;
    private final CanjeService canjeService;

    private Cliente clienteActual;

    private JTextField txtCodigo;
    private JLabel lblCliente;
    private JLabel lblPuntos;
    private JLabel lblEstado;

    private JTable tablaRecompensas;
    private DefaultTableModel modeloTabla;

    public FrmCanjes() {

        clienteDAO = new ClienteDAO();
        canjeService = new CanjeService();

        configurarVentana();
        construirInterfaz();
        cargarRecompensas();
    }

    private void configurarVentana() {

        setTitle("Sandwiches El Gordo - Recompensas y Canjes");
        setSize(1050, 700);
        setMinimumSize(new Dimension(850, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void construirInterfaz() {

        JPanel principal = new JPanel(new BorderLayout(15, 15));
        principal.setBackground(FONDO);
        principal.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        setContentPane(principal);

        JPanel superior = new JPanel(new BorderLayout(10, 15));
        superior.setOpaque(false);

        JLabel titulo = new JLabel(
                "SANDWICHES EL GORDO",
                SwingConstants.CENTER
        );

        titulo.setForeground(DORADO);
        titulo.setFont(
                new Font("Georgia", Font.BOLD, 28)
        );

        JLabel subtitulo = new JLabel(
                "RECOMPENSAS Y CANJES",
                SwingConstants.CENTER
        );

        subtitulo.setForeground(BLANCO);
        subtitulo.setFont(
                new Font("Georgia", Font.BOLD, 18)
        );

        JPanel encabezado = new JPanel(new GridLayout(2, 1, 5, 5));
        encabezado.setOpaque(false);
        encabezado.add(titulo);
        encabezado.add(subtitulo);

        superior.add(encabezado, BorderLayout.NORTH);
        superior.add(crearPanelCliente(), BorderLayout.CENTER);

        principal.add(superior, BorderLayout.NORTH);
        principal.add(crearPanelRecompensas(), BorderLayout.CENTER);
        principal.add(crearPanelInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelCliente() {

        JPanel panel = new JPanel(new GridLayout(2, 1, 8, 8));
        panel.setBackground(PANEL);
        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                BorderFactory.createLineBorder(DORADO),
                                "DATOS DEL CLIENTE",
                                0, 0,
                                new Font("Georgia", Font.BOLD, 15),
                                DORADO
                        ),
                        BorderFactory.createEmptyBorder(12, 12, 12, 12)
                )
        );

        JPanel busqueda = new JPanel();
        busqueda.setOpaque(false);

        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setForeground(BLANCO);

        txtCodigo = new JTextField(16);

        JButton btnBuscar = crearBoton(
                "BUSCAR CLIENTE", DORADO, Color.BLACK
        );

        btnBuscar.addActionListener(e -> buscarCliente());

        txtCodigo.addActionListener(e -> buscarCliente());

        busqueda.add(lblCodigo);
        busqueda.add(txtCodigo);
        busqueda.add(btnBuscar);

        JPanel informacion = new JPanel();
        informacion.setOpaque(false);

        lblCliente = new JLabel("Cliente: No seleccionado");
        lblCliente.setForeground(BLANCO);

        lblPuntos = new JLabel("Puntos disponibles: 0");
        lblPuntos.setForeground(DORADO);

        informacion.add(lblCliente);
        informacion.add(new JLabel("        "));
        informacion.add(lblPuntos);

        panel.add(busqueda);
        panel.add(informacion);

        return panel;
    }

    private JPanel crearPanelRecompensas() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(PANEL);

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(DORADO),
                        "CATÁLOGO DE RECOMPENSAS",
                        0, 0,
                        new Font("Georgia", Font.BOLD, 15),
                        DORADO
                )
        );

        String[] columnas = {
            "ID", "Código", "Recompensa",
            "Puntos necesarios", "Tipo", "Estado"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaRecompensas = new JTable(modeloTabla);

        tablaRecompensas.setRowHeight(34);
        tablaRecompensas.setBackground(PANEL);
        tablaRecompensas.setForeground(BLANCO);
        tablaRecompensas.setSelectionBackground(MORADO);
        tablaRecompensas.setSelectionForeground(BLANCO);
        tablaRecompensas.setGridColor(DORADO);

        tablaRecompensas.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaRecompensas.getTableHeader().setBackground(DORADO);
        tablaRecompensas.getTableHeader().setForeground(Color.BLACK);
        tablaRecompensas.getTableHeader().setFont(
                new Font("Georgia", Font.BOLD, 13)
        );

        DefaultTableCellRenderer centrado =
                new DefaultTableCellRenderer();

        centrado.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tablaRecompensas.getColumnCount(); i++) {
            tablaRecompensas.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(centrado);
        }

        JScrollPane scroll = new JScrollPane(tablaRecompensas);
        scroll.getViewport().setBackground(PANEL);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelInferior() {

        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBackground(FONDO);

        JPanel botones = new JPanel();
        botones.setOpaque(false);

        JButton btnCanjear = crearBoton(
                "REALIZAR CANJE", VERDE, BLANCO
        );

        JButton btnActualizar = crearBoton(
                "ACTUALIZAR RECOMPENSAS", MORADO, BLANCO
        );

        JButton btnLimpiar = crearBoton(
                "NUEVO CANJE", DORADO, Color.BLACK
        );

        btnCanjear.addActionListener(e -> realizarCanje());

        btnActualizar.addActionListener(
                e -> cargarRecompensas()
        );

        btnLimpiar.addActionListener(e -> limpiarFormulario());

        botones.add(btnCanjear);
        botones.add(btnActualizar);
        botones.add(btnLimpiar);

        lblEstado = new JLabel(
                "Seleccione un cliente y una recompensa.",
                SwingConstants.CENTER
        );

        lblEstado.setForeground(DORADO);

        panel.add(botones, BorderLayout.NORTH);
        panel.add(lblEstado, BorderLayout.SOUTH);

        return panel;
    }

    private JButton crearBoton(
            String texto, Color fondo, Color letra) {

        JButton boton = new JButton(texto);

        boton.setBackground(fondo);
        boton.setForeground(letra);
        boton.setFont(
                new Font("Georgia", Font.BOLD, 12)
        );

        boton.setFocusPainted(false);
        boton.setPreferredSize(new Dimension(210, 42));

        return boton;
    }

    private void buscarCliente() {

        String codigo = txtCodigo.getText().trim();

        if (codigo.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese el código del cliente."
            );

            return;
        }

        clienteActual = null;
        lblCliente.setText("Cliente: No seleccionado");
        lblPuntos.setText("Puntos disponibles: 0");

        Cliente cliente = clienteDAO.buscarPorCodigo(codigo);

        if (cliente == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente no encontrado.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!cliente.isActivo()) {

            JOptionPane.showMessageDialog(
                    this,
                    "El cliente está inactivo.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            int puntos = canjeService.consultarPuntos(
                    cliente.getIdCliente()
            );

            clienteActual = cliente;

            lblCliente.setText(
                    "Cliente: " + cliente.getNombre()
            );

            lblPuntos.setText(
                    "Puntos disponibles: " + puntos
            );

            lblEstado.setText(
                    "Cliente encontrado correctamente."
            );

        } catch (SQLException ex) {

            mostrarError(ex.getMessage());
        }
    }

    private void cargarRecompensas() {

        modeloTabla.setRowCount(0);

        try {

            List<String[]> recompensas =
                    canjeService.listarRecompensas();

            for (String[] recompensa : recompensas) {

                String tipo = !recompensa[4].isBlank()
                        ? "PRODUCTO" : "MENÚ";

                modeloTabla.addRow(new Object[]{
                    recompensa[0],
                    recompensa[1],
                    recompensa[2],
                    recompensa[3],
                    tipo,
                    recompensa[6]
                });
            }

            lblEstado.setText(
                    "Recompensas disponibles: "
                    + recompensas.size()
            );

        } catch (SQLException ex) {

            mostrarError(ex.getMessage());
        }
    }

    private void realizarCanje() {

        if (clienteActual == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero debe seleccionar un cliente."
            );

            return;
        }

        int fila = tablaRecompensas.getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una recompensa."
            );

            return;
        }

        int filaModelo = tablaRecompensas.convertRowIndexToModel(fila);

        int idRecompensa = Integer.parseInt(
                modeloTabla.getValueAt(filaModelo, 0).toString()
        );

        String nombre = modeloTabla.getValueAt(
                filaModelo, 2
        ).toString();

        int puntos = Integer.parseInt(
                modeloTabla.getValueAt(filaModelo, 3).toString()
        );

        int confirmar = JOptionPane.showConfirmDialog(
                this,
                "Cliente: " + clienteActual.getNombre()
                + "\nRecompensa: " + nombre
                + "\nCosto: " + puntos + " puntos"
                + "\n\n¿Desea confirmar el canje?",
                "Confirmar canje",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            boolean realizado = canjeService.realizarCanje(
                    clienteActual.getIdCliente(),
                    idRecompensa
            );

            if (realizado) {

                JOptionPane.showMessageDialog(
                        this,
                        "CANJE REALIZADO CORRECTAMENTE"
                        + "\nRecompensa: " + nombre,
                        "Canje exitoso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                int puntosActualizados =
                        canjeService.consultarPuntos(
                                clienteActual.getIdCliente()
                        );

                lblPuntos.setText(
                        "Puntos disponibles: "
                        + puntosActualizados
                );

                lblEstado.setText(
                        "Canje registrado correctamente."
                );

                cargarRecompensas();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo realizar el canje."
                        + "\nVerifique los puntos y el inventario.",
                        "Canje rechazado",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (SQLException | IllegalArgumentException ex) {

            mostrarError(ex.getMessage());
        }
    }

    private void limpiarFormulario() {

        clienteActual = null;

        txtCodigo.setText("");
        lblCliente.setText("Cliente: No seleccionado");
        lblPuntos.setText("Puntos disponibles: 0");

        tablaRecompensas.clearSelection();

        lblEstado.setText(
                "Formulario listo para un nuevo canje."
        );
    }

    private void mostrarError(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new FrmCanjes().setVisible(true);
        });
    }
}
