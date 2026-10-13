
package com.sandwicheselgordo.vista;

import com.sandwicheselgordo.dao.ProductoDAO;
import com.sandwicheselgordo.modelo.Producto;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FrmProductos extends JFrame {

    private static final Color FONDO = new Color(19, 18, 23);
    private static final Color PANEL = new Color(33, 31, 38);
    private static final Color DORADO = new Color(218, 174, 84);
    private static final Color VERDE = new Color(35, 139, 94);
    private static final Color MORADO = new Color(109, 65, 164);
    private static final Color BLANCO = new Color(245, 241, 232);

    private final ProductoDAO dao = new ProductoDAO();

    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtPrecio = new JTextField();
    private final JTextField txtCantidad = new JTextField("1");
    private final JTextField txtReferencia = new JTextField();

    private final JComboBox<String> cmbCategoria =
            new JComboBox<>();

    private final JCheckBox chkActivo =
            new JCheckBox("Producto activo", true);

    private final JLabel lblExistencia =
            new JLabel("Existencia actual: 0");

    private final JLabel lblEstado =
            new JLabel("Productos registrados: 0");

    private final DefaultTableModel modelo =
            new DefaultTableModel(
                new String[]{
                    "ID", "Código", "Nombre", "Categoría",
                    "Precio", "Existencia", "Estado"
                }, 0
            ) {
                @Override
                public boolean isCellEditable(int fila, int col) {
                    return false;
                }
            };

    private final JTable tabla = new JTable(modelo);

    private final List<Integer> idsCategorias =
            new ArrayList<>();

    private Producto seleccionado;

    public FrmProductos() {
        setTitle("Sandwiches El Gordo - Productos e Inventario");
        setSize(1120, 760);
        setMinimumSize(new Dimension(950, 650));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        construir();
        cargarCategorias();
        cargarProductos();
    }

    private JButton boton(String texto, Color color) {
        JButton b = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };

        b.setBackground(color);
        b.setForeground(color.equals(DORADO)
                ? Color.BLACK : Color.WHITE);
        b.setFont(new Font("Serif", Font.BOLD, 13));
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setBorder(BorderFactory.createLineBorder(DORADO));
        b.setCursor(Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR));
        return b;
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(BLANCO);
        return l;
    }

    private JPanel panel(String titulo) {
        JPanel p = new JPanel();
        p.setBackground(PANEL);
        p.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(DORADO),
                titulo, 0, 0,
                new Font("Serif", Font.BOLD, 15), DORADO
        ));
        return p;
    }

    private void construir() {
        JPanel raiz = new JPanel(new BorderLayout(12, 12));
        raiz.setBackground(FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(
                18, 20, 18, 20));

        JLabel titulo = new JLabel(
                "SANDWICHES EL GORDO",
                SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 30));
        titulo.setForeground(DORADO);

        JLabel subtitulo = new JLabel(
                "GESTIÓN DE PRODUCTOS E INVENTARIO",
                SwingConstants.CENTER);
        subtitulo.setFont(new Font("Serif", Font.BOLD, 19));
        subtitulo.setForeground(BLANCO);

        JPanel encabezado = new JPanel(
                new GridLayout(2, 1, 0, 5));
        encabezado.setOpaque(false);
        encabezado.add(titulo);
        encabezado.add(subtitulo);

        JPanel datos = panel("DATOS DEL PRODUCTO");
        datos.setLayout(new GridLayout(3, 4, 12, 12));

        datos.add(etiqueta("Código:"));
        datos.add(txtCodigo);
        datos.add(etiqueta("Nombre:"));
        datos.add(txtNombre);

        datos.add(etiqueta("Categoría:"));
        datos.add(cmbCategoria);
        datos.add(etiqueta("Precio (Q):"));
        datos.add(txtPrecio);

        datos.add(etiqueta("Estado:"));
        chkActivo.setBackground(PANEL);
        chkActivo.setForeground(BLANCO);
        datos.add(chkActivo);

        lblExistencia.setForeground(DORADO);
        datos.add(lblExistencia);
        datos.add(new JLabel());

        JPanel acciones = new JPanel(
                new GridLayout(1, 6, 9, 0));
        acciones.setOpaque(false);

        JButton btnRegistrar = boton("REGISTRAR", VERDE);
        JButton btnBuscar = boton("BUSCAR", DORADO);
        JButton btnActualizar = boton("ACTUALIZAR", MORADO);
        JButton btnEstado = boton("CAMBIAR ESTADO",
                new Color(153, 53, 67));
        JButton btnLimpiar = boton("LIMPIAR", DORADO);
        JButton btnRefrescar = boton("REFRESCAR", VERDE);

        acciones.add(btnRegistrar);
        acciones.add(btnBuscar);
        acciones.add(btnActualizar);
        acciones.add(btnEstado);
        acciones.add(btnLimpiar);
        acciones.add(btnRefrescar);

        JPanel abastecimiento =
                panel("ENTRADA DE INVENTARIO");
        abastecimiento.setLayout(
                new GridLayout(1, 6, 10, 8));

        abastecimiento.add(etiqueta("Cantidad:"));
        abastecimiento.add(txtCantidad);
        abastecimiento.add(etiqueta("Referencia:"));
        abastecimiento.add(txtReferencia);

        JButton btnEntrada = boton(
                "REGISTRAR ENTRADA", VERDE);
        abastecimiento.add(btnEntrada);
        abastecimiento.add(new JLabel());

        JPanel superior = new JPanel(
                new BorderLayout(10, 12));
        superior.setOpaque(false);
        superior.add(datos, BorderLayout.NORTH);
        superior.add(acciones, BorderLayout.CENTER);
        superior.add(abastecimiento, BorderLayout.SOUTH);

        tabla.setRowHeight(28);
        tabla.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        tabla.setBackground(PANEL);
        tabla.setForeground(BLANCO);
        tabla.setSelectionBackground(
                new Color(85, 65, 110));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setGridColor(DORADO);

        tabla.getTableHeader().setBackground(DORADO);
        tabla.getTableHeader().setForeground(Color.BLACK);
        tabla.getTableHeader().setFont(
                new Font("Serif", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(PANEL);
        scroll.setBorder(
                BorderFactory.createLineBorder(DORADO));

        JPanel centro = new JPanel(
                new BorderLayout(10, 12));
        centro.setOpaque(false);
        centro.add(superior, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);

        lblEstado.setForeground(DORADO);
        lblEstado.setHorizontalAlignment(
                SwingConstants.CENTER);

        raiz.add(encabezado, BorderLayout.NORTH);
        raiz.add(centro, BorderLayout.CENTER);
        raiz.add(lblEstado, BorderLayout.SOUTH);

        setContentPane(raiz);

        btnRegistrar.addActionListener(e -> registrar());
        btnBuscar.addActionListener(e -> buscar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEstado.addActionListener(e -> cambiarEstado());
        btnLimpiar.addActionListener(e -> limpiar());
        btnRefrescar.addActionListener(e -> cargarProductos());
        btnEntrada.addActionListener(e -> abastecer());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarFila();
            }
        });
    }

    private void cargarCategorias() {
        try {
            idsCategorias.clear();
            cmbCategoria.removeAllItems();

            for (Object[] c : dao.listarCategorias()) {
                idsCategorias.add((Integer) c[0]);
                cmbCategoria.addItem((String) c[1]);
            }
        } catch (RuntimeException e) {
            error(e.getMessage());
        }
    }

    private void cargarProductos() {
        modelo.setRowCount(0);

        List<Producto> productos = dao.listar();

        for (Producto p : productos) {
            modelo.addRow(new Object[]{
                p.getIdProducto(),
                p.getCodigo(),
                p.getNombre(),
                p.getTipoProducto(),
                String.format("Q%.2f", p.getPrecio()),
                p.getExistencia(),
                p.isActivo() ? "ACTIVO" : "INACTIVO"
            });
        }

        lblEstado.setText(
                "Productos registrados: " + productos.size()
                + "  |  Stock bajo: 5 unidades o menos"
        );
    }

    private void seleccionarFila() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;

        String codigo = modelo.getValueAt(
                tabla.convertRowIndexToModel(fila), 1
        ).toString();

        Producto p = dao.buscarPorCodigo(codigo);

        if (p != null) {
            mostrarProducto(p);
        }
    }

    private void mostrarProducto(Producto p) {
        seleccionado = p;
        txtCodigo.setText(p.getCodigo());
        txtCodigo.setEditable(false);
        txtNombre.setText(p.getNombre());
        txtPrecio.setText(
                String.valueOf(p.getPrecio()));
        chkActivo.setSelected(p.isActivo());

        int indice = idsCategorias.indexOf(
                p.getIdCategoria());

        if (indice >= 0) {
            cmbCategoria.setSelectedIndex(indice);
        }

        lblExistencia.setText(
                "Existencia actual: " + p.getExistencia()
                + (p.getExistencia() <= 5
                        ? "  |  STOCK BAJO" : "")
        );
    }

    private Producto leerFormulario() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty()) {
            throw new IllegalArgumentException(
                "El código y el nombre son obligatorios.");
        }

        if (cmbCategoria.getSelectedIndex() < 0) {
            throw new IllegalArgumentException(
                "Seleccione una categoría.");
        }

        double precio = Double.parseDouble(
                txtPrecio.getText().trim());

        if (!Double.isFinite(precio) || precio < 0) {
            throw new IllegalArgumentException(
                "El precio debe ser válido y no negativo.");
        }

        Producto p = new Producto();
        p.setCodigo(codigo);
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setIdCategoria(idsCategorias.get(
                cmbCategoria.getSelectedIndex()));
        p.setActivo(chkActivo.isSelected());

        return p;
    }

    private void registrar() {
        try {
            if (seleccionado != null) {
                throw new IllegalArgumentException(
                    "Presione LIMPIAR para registrar otro producto.");
            }

            Producto p = leerFormulario();
            p.setExistencia(0);

            if (dao.insertar(p)) {
                JOptionPane.showMessageDialog(this,
                    "Producto registrado correctamente.");
                limpiar();
                cargarProductos();
            } else {
                error("No se pudo registrar el producto.");
            }

        } catch (RuntimeException e) {
            error("Verifique los datos: " + e.getMessage());
        }
    }

    private void buscar() {
        String codigo = txtCodigo.getText().trim();

        if (codigo.isEmpty()) {
            error("Ingrese un código de producto.");
            return;
        }

        Producto p = dao.buscarPorCodigo(codigo);

        if (p == null) {
            error("Producto no encontrado.");
        } else {
            mostrarProducto(p);
        }
    }

    private void actualizar() {
        try {
            if (seleccionado == null) {
                error("Seleccione primero un producto.");
                return;
            }

            Producto p = leerFormulario();
            p.setIdProducto(seleccionado.getIdProducto());

            if (dao.actualizar(p)) {
                JOptionPane.showMessageDialog(this,
                    "Producto actualizado.");
                limpiar();
                cargarProductos();
            } else {
                error("No se pudo actualizar el producto.");
            }

        } catch (RuntimeException e) {
            error("Verifique los datos: " + e.getMessage());
        }
    }

    private void cambiarEstado() {
        if (seleccionado == null) {
            error("Seleccione primero un producto.");
            return;
        }

        try {
            Producto p = leerFormulario();
            p.setIdProducto(seleccionado.getIdProducto());
            p.setActivo(!seleccionado.isActivo());

            if (dao.actualizar(p)) {
                JOptionPane.showMessageDialog(this,
                    "Estado del producto actualizado.");
                limpiar();
                cargarProductos();
            } else {
                error("No se pudo cambiar el estado.");
            }

        } catch (RuntimeException e) {
            error(e.getMessage());
        }
    }

    private void abastecer() {
        if (seleccionado == null) {
            error("Seleccione un producto.");
            return;
        }

        try {
            int cantidad = Integer.parseInt(
                    txtCantidad.getText().trim());

            String referencia =
                    txtReferencia.getText().trim();

            if (referencia.isEmpty()) {
                error("Ingrese una referencia del abastecimiento.");
                return;
            }

            dao.registrarAbastecimiento(
                    seleccionado.getIdProducto(),
                    cantidad,
                    referencia
            );

            JOptionPane.showMessageDialog(this,
                "Abastecimiento registrado correctamente.");

            Producto actualizado = dao.buscarPorCodigo(
                    seleccionado.getCodigo());

            if (actualizado != null) {
                mostrarProducto(actualizado);
            }

            cargarProductos();

        } catch (RuntimeException e) {
            error("No se pudo registrar la entrada: "
                    + e.getMessage());
        }
    }

    private void limpiar() {
        seleccionado = null;
        tabla.clearSelection();
        txtCodigo.setEditable(true);
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("1");
        txtReferencia.setText("");
        chkActivo.setSelected(true);
        lblExistencia.setText("Existencia actual: 0");

        if (cmbCategoria.getItemCount() > 0) {
            cmbCategoria.setSelectedIndex(0);
        }
    }

    private void error(String mensaje) {
        JOptionPane.showMessageDialog(
                this, mensaje, "Aviso",
                JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new FrmProductos().setVisible(true));
    }
}
