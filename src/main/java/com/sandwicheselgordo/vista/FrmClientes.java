
package com.sandwicheselgordo.vista;

import com.sandwicheselgordo.dao.ClienteDAO;
import com.sandwicheselgordo.modelo.Cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

public class FrmClientes extends JFrame {

    private static final Color FONDO = new Color(19, 18, 23);
    private static final Color PANEL = new Color(33, 31, 38);
    private static final Color DORADO = new Color(218, 174, 84);
    private static final Color VERDE = new Color(35, 139, 94);
    private static final Color MORADO = new Color(109, 65, 164);
    private static final Color ROJO = new Color(157, 52, 65);
    private static final Color BLANCO = new Color(245, 241, 232);

    private final ClienteDAO clienteDAO = new ClienteDAO();

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtDireccion;
    private JCheckBox chkActivo;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JLabel lblEstado;
    private JLabel lblPuntos;

    private int idSeleccionado = 0;

    public FrmClientes() {
        setTitle("Sandwiches El Gordo - Gestion de Clientes");
        setSize(1100, 750);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        construirInterfaz();
        cargarClientes();
    }

    private void construirInterfaz() {

        JPanel principal = new JPanel(new BorderLayout(15, 15));
        principal.setBackground(FONDO);
        principal.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        JPanel encabezado = new JPanel(new GridLayout(2, 1, 0, 5));
        encabezado.setOpaque(false);

        JLabel titulo = new JLabel(
            "SANDWICHES EL GORDO",
            SwingConstants.CENTER
        );
        titulo.setFont(new Font("Serif", Font.BOLD, 30));
        titulo.setForeground(DORADO);

        JLabel subtitulo = new JLabel(
            "REGISTRO Y GESTION DE CLIENTES",
            SwingConstants.CENTER
        );
        subtitulo.setFont(new Font("Serif", Font.BOLD, 19));
        subtitulo.setForeground(BLANCO);

        encabezado.add(titulo);
        encabezado.add(subtitulo);

        JPanel formulario = new JPanel(new GridLayout(4, 4, 12, 12));
        formulario.setBackground(PANEL);
        formulario.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(DORADO),
                    "DATOS DEL CLIENTE",
                    0,
                    0,
                    new Font("Serif", Font.BOLD, 15),
                    DORADO
                ),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            )
        );

        txtCodigo = crearCampo();
        txtNombre = crearCampo();
        txtTelefono = crearCampo();
        txtCorreo = crearCampo();
        txtDireccion = crearCampo();

        chkActivo = new JCheckBox("Cliente activo", true);
        chkActivo.setBackground(PANEL);
        chkActivo.setForeground(BLANCO);

        lblPuntos = new JLabel("Puntos acumulados: 0");
        lblPuntos.setForeground(DORADO);

        formulario.add(crearEtiqueta("Codigo:"));
        formulario.add(txtCodigo);
        formulario.add(crearEtiqueta("Nombre:"));
        formulario.add(txtNombre);

        formulario.add(crearEtiqueta("Telefono:"));
        formulario.add(txtTelefono);
        formulario.add(crearEtiqueta("Correo:"));
        formulario.add(txtCorreo);

        formulario.add(crearEtiqueta("Direccion:"));
        formulario.add(txtDireccion);
        formulario.add(crearEtiqueta("Estado:"));
        formulario.add(chkActivo);

        formulario.add(crearEtiqueta("Fidelizacion:"));
        formulario.add(lblPuntos);
        formulario.add(new JLabel());
        formulario.add(new JLabel());

        JPanel botones = new JPanel(new FlowLayout(
            FlowLayout.CENTER, 12, 10
        ));
        botones.setBackground(FONDO);

        JButton btnRegistrar = crearBoton(
            "REGISTRAR", VERDE, Color.WHITE
        );

        JButton btnBuscar = crearBoton(
            "BUSCAR", DORADO, Color.BLACK
        );

        JButton btnActualizar = crearBoton(
            "ACTUALIZAR", MORADO, Color.WHITE
        );

        JButton btnDesactivar = crearBoton(
            "CAMBIAR ESTADO", ROJO, Color.WHITE
        );

        JButton btnLimpiar = crearBoton(
            "LIMPIAR", DORADO, Color.BLACK
        );

        JButton btnRefrescar = crearBoton(
            "REFRESCAR", VERDE, Color.WHITE
        );

        botones.add(btnRegistrar);
        botones.add(btnBuscar);
        botones.add(btnActualizar);
        botones.add(btnDesactivar);
        botones.add(btnLimpiar);
        botones.add(btnRefrescar);

        JPanel superior = new JPanel(new BorderLayout(10, 15));
        superior.setOpaque(false);
        superior.add(encabezado, BorderLayout.NORTH);
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);

        modeloTabla = new DefaultTableModel(
            new Object[]{
                "ID", "CODIGO", "NOMBRE", "TELEFONO",
                "CORREO", "DIRECCION", "PUNTOS", "ESTADO"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabla.setBackground(PANEL);
        tabla.setForeground(BLANCO);
        tabla.setSelectionBackground(new Color(90, 70, 40));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setGridColor(DORADO);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setBackground(DORADO);
        tabla.getTableHeader().setForeground(Color.BLACK);
        tabla.getTableHeader().setFont(
            new Font("Serif", Font.BOLD, 13)
        );

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(PANEL);
        scroll.setBorder(BorderFactory.createLineBorder(DORADO));

        lblEstado = new JLabel(
            "Seleccione un cliente o registre uno nuevo.",
            SwingConstants.CENTER
        );
        lblEstado.setForeground(DORADO);
        lblEstado.setFont(new Font("SansSerif", Font.PLAIN, 13));

        principal.add(superior, BorderLayout.NORTH);
        principal.add(scroll, BorderLayout.CENTER);
        principal.add(lblEstado, BorderLayout.SOUTH);

        setContentPane(principal);

        btnRegistrar.addActionListener(e -> registrarCliente());
        btnBuscar.addActionListener(e -> buscarCliente());
        btnActualizar.addActionListener(e -> actualizarCliente());
        btnDesactivar.addActionListener(e -> cambiarEstado());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnRefrescar.addActionListener(e -> cargarClientes());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarCliente();
            }
        });
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(BLANCO);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 12));
        return etiqueta;
    }

    private JTextField crearCampo() {
        JTextField campo = new JTextField();
        campo.setBackground(Color.WHITE);
        campo.setForeground(Color.BLACK);
        campo.setCaretColor(Color.BLACK);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return campo;
    }

    private JButton crearBoton(
            String texto, Color fondo, Color letra) {

        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 =
                    (java.awt.Graphics2D) g.create();

                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();

                super.paintComponent(g);
            }
        };

        boton.setBackground(fondo);
        boton.setForeground(letra);
        boton.setFont(new Font("Serif", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setBorder(
            BorderFactory.createLineBorder(DORADO, 1)
        );
        boton.setPreferredSize(new Dimension(145, 38));
        boton.setCursor(
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        return boton;
    }

    private boolean validarDatos() {

        if (txtCodigo.getText().trim().isEmpty()
                || txtNombre.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "El codigo y el nombre son obligatorios."
            );
            return false;
        }

        String correo = txtCorreo.getText().trim();

        if (!correo.isEmpty()
                && !correo.matches(
                    "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"
                )) {

            JOptionPane.showMessageDialog(
                this,
                "Ingrese un correo electronico valido."
            );
            return false;
        }

        return true;
    }

    private void registrarCliente() {

        if (!validarDatos()) {
            return;
        }

        String codigo = txtCodigo.getText().trim();

        if (clienteDAO.buscarPorCodigo(codigo) != null) {
            JOptionPane.showMessageDialog(
                this,
                "Ya existe un cliente con ese codigo."
            );
            return;
        }

        Cliente cliente = new Cliente();

        cliente.setIdentificacion(codigo);
        cliente.setNombre(txtNombre.getText().trim());
        cliente.setTelefono(txtTelefono.getText().trim());
        cliente.setCorreo(txtCorreo.getText().trim());
        cliente.setDireccion(txtDireccion.getText().trim());
        cliente.setPuntos(0);
        cliente.setActivo(chkActivo.isSelected());

        if (clienteDAO.insertar(cliente)) {

            JOptionPane.showMessageDialog(
                this,
                "Cliente registrado correctamente."
            );

            limpiarFormulario();
            cargarClientes();

        } else {
            JOptionPane.showMessageDialog(
                this,
                "No se pudo registrar el cliente. "
                + "Revise la consola y la conexion Oracle.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void buscarCliente() {

        String codigo = txtCodigo.getText().trim();

        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Ingrese el codigo del cliente."
            );
            return;
        }

        Cliente cliente = clienteDAO.buscarPorCodigo(codigo);

        if (cliente == null) {
            JOptionPane.showMessageDialog(
                this,
                "Cliente no encontrado."
            );
            return;
        }

        mostrarCliente(cliente);
        lblEstado.setText("Cliente encontrado: " + cliente.getNombre());
    }

    private void actualizarCliente() {

        if (idSeleccionado <= 0) {
            JOptionPane.showMessageDialog(
                this,
                "Primero busque o seleccione un cliente."
            );
            return;
        }

        if (!validarDatos()) {
            return;
        }

        Cliente cliente = new Cliente();

        cliente.setIdCliente(idSeleccionado);
        cliente.setNombre(txtNombre.getText().trim());
        cliente.setTelefono(txtTelefono.getText().trim());
        cliente.setCorreo(txtCorreo.getText().trim());
        cliente.setDireccion(txtDireccion.getText().trim());
        cliente.setActivo(chkActivo.isSelected());

        if (clienteDAO.actualizar(cliente)) {

            JOptionPane.showMessageDialog(
                this,
                "Cliente actualizado correctamente."
            );

            limpiarFormulario();
            cargarClientes();

        } else {
            JOptionPane.showMessageDialog(
                this,
                "No se pudo actualizar el cliente.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cambiarEstado() {

        if (idSeleccionado <= 0) {
            JOptionPane.showMessageDialog(
                this,
                "Seleccione primero un cliente."
            );
            return;
        }

       boolean nuevoEstado = chkActivo.isSelected();

        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Desea " + (nuevoEstado ? "activar" : "desactivar")
                + " este cliente?",
            "Confirmar cambio",
            JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        Cliente cliente = new Cliente();
        cliente.setIdCliente(idSeleccionado);
        cliente.setNombre(txtNombre.getText().trim());
        cliente.setTelefono(txtTelefono.getText().trim());
        cliente.setCorreo(txtCorreo.getText().trim());
        cliente.setDireccion(txtDireccion.getText().trim());
        cliente.setActivo(nuevoEstado);

        if (clienteDAO.actualizar(cliente)) {

            JOptionPane.showMessageDialog(
                this,
                "Estado actualizado correctamente."
            );

            limpiarFormulario();
            cargarClientes();

        } else {
            JOptionPane.showMessageDialog(
                this,
                "No se pudo cambiar el estado."
            );
        }
    }

    private void cargarClientes() {

        modeloTabla.setRowCount(0);

        List<Cliente> clientes = clienteDAO.listar();

        for (Cliente cliente : clientes) {

            modeloTabla.addRow(new Object[]{
                cliente.getIdCliente(),
                cliente.getIdentificacion(),
                cliente.getNombre(),
                cliente.getTelefono(),
                cliente.getCorreo(),
                cliente.getDireccion(),
                cliente.getPuntos(),
                cliente.isActivo() ? "ACTIVO" : "INACTIVO"
            });
        }

        lblEstado.setText(
            "Clientes registrados: " + clientes.size()
        );
    }

    private void seleccionarCliente() {

        int fila = tabla.getSelectedRow();

        if (fila < 0) {
            return;
        }

        int filaModelo = tabla.convertRowIndexToModel(fila);

        String codigo = String.valueOf(
            modeloTabla.getValueAt(filaModelo, 1)
        );

        Cliente cliente = clienteDAO.buscarPorCodigo(codigo);

        if (cliente != null) {
            mostrarCliente(cliente);
        }
    }

    private void mostrarCliente(Cliente cliente) {

        idSeleccionado = cliente.getIdCliente();

        txtCodigo.setText(cliente.getIdentificacion());
        txtCodigo.setEditable(false);
        txtNombre.setText(cliente.getNombre());
        txtTelefono.setText(cliente.getTelefono());
        txtCorreo.setText(cliente.getCorreo());
        txtDireccion.setText(cliente.getDireccion());
        chkActivo.setSelected(cliente.isActivo());

        lblPuntos.setText(
            "Puntos acumulados: " + cliente.getPuntos()
        );
    }

    private void limpiarFormulario() {

        idSeleccionado = 0;

        txtCodigo.setText("");
        txtCodigo.setEditable(true);
        txtNombre.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        chkActivo.setSelected(true);
        lblPuntos.setText("Puntos acumulados: 0");

        tabla.clearSelection();
        txtCodigo.requestFocusInWindow();

        lblEstado.setText("Formulario limpio.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new FrmClientes().setVisible(true)
        );
    }
}
