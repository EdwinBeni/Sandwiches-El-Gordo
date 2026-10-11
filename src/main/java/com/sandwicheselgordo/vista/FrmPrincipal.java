
package com.sandwicheselgordo.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class FrmPrincipal extends JFrame {

    private static final Color FONDO = new Color(19, 18, 23);
    private static final Color DORADO = new Color(218, 174, 84);
    private static final Color VERDE = new Color(35, 139, 94);
    private static final Color MORADO = new Color(109, 65, 164);
    private static final Color BLANCO = new Color(245, 241, 232);

    public FrmPrincipal() {
        setTitle("Sandwiches El Gordo - Sistema Principal");
        setSize(1050, 680);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        construirInterfaz();
    }

    private void construirInterfaz() {

        JPanel principal = new JPanel(new BorderLayout(20, 25));
        principal.setBackground(FONDO);
        principal.setBorder(
            BorderFactory.createEmptyBorder(35, 45, 35, 45)
        );

        JPanel encabezado = new JPanel(new GridLayout(3, 1, 0, 8));
        encabezado.setOpaque(false);

        JLabel titulo = new JLabel(
            "SANDWICHES EL GORDO",
            SwingConstants.CENTER
        );
        titulo.setFont(new Font("Serif", Font.BOLD, 34));
        titulo.setForeground(DORADO);

        JLabel subtitulo = new JLabel(
            "SISTEMA DE ADMINISTRACIÓN",
            SwingConstants.CENTER
        );
        subtitulo.setFont(new Font("Serif", Font.BOLD, 21));
        subtitulo.setForeground(BLANCO);

        JLabel descripcion = new JLabel(
            "Gestión de pedidos, pagos, recompensas y reportes",
            SwingConstants.CENTER
        );
        descripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descripcion.setForeground(DORADO);

        encabezado.add(titulo);
        encabezado.add(subtitulo);
        encabezado.add(descripcion);

        JPanel opciones = new JPanel(new GridLayout(2, 2, 24, 24));
        opciones.setOpaque(false);
        opciones.setBorder(
            BorderFactory.createEmptyBorder(35, 25, 35, 25)
        );

        JButton btnPedidos = crearBoton(
            "PEDIDOS Y PAGOS", VERDE
        );

        JButton btnEstados = crearBoton(
            "ESTADOS DE PEDIDOS", MORADO
        );

        JButton btnCanjes = crearBoton(
            "RECOMPENSAS Y CANJES", MORADO
        );

        JButton btnReportes = crearBoton(
            "REPORTES", VERDE
        );

        btnPedidos.addActionListener(e ->
            abrirVentana(new FrmPedidos())
        );

       btnEstados.addActionListener(e ->
    abrirVentana(new FrmGestionPedidos())
);
        btnCanjes.addActionListener(e ->
            abrirVentana(new FrmCanjes())
        );

        btnReportes.addActionListener(e ->
            abrirVentana(new FrmReportes())
        );

        opciones.add(btnPedidos);
        opciones.add(btnEstados);
        opciones.add(btnCanjes);
        opciones.add(btnReportes);

        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);

        JLabel estado = new JLabel(
            "SANDWICHES EL GORDO  |  PANEL PRINCIPAL",
            SwingConstants.CENTER
        );
        estado.setForeground(DORADO);
        estado.setFont(new Font("Serif", Font.BOLD, 14));

        JButton btnSalir = crearBoton("SALIR DEL SISTEMA", DORADO);
        btnSalir.setForeground(Color.BLACK);
        btnSalir.setPreferredSize(new Dimension(220, 45));

        btnSalir.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Desea salir del sistema?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        pie.add(estado, BorderLayout.CENTER);
        pie.add(btnSalir, BorderLayout.EAST);

        principal.add(encabezado, BorderLayout.NORTH);
        principal.add(opciones, BorderLayout.CENTER);
        principal.add(pie, BorderLayout.SOUTH);

        setContentPane(principal);
    }

   

private JButton crearBoton(String texto, Color fondo) {

    JButton boton = new JButton(texto);

    boton.setUI(
        new javax.swing.plaf.basic.BasicButtonUI()
    );

    boton.setBackground(fondo);

    boton.setForeground(
        fondo.equals(DORADO) ? Color.BLACK : Color.WHITE
    );

    boton.setFont(
        new Font("Serif", Font.BOLD, 17)
    );

    boton.setOpaque(true);
    boton.setContentAreaFilled(true);
    boton.setFocusPainted(false);
    boton.setBorderPainted(true);
    boton.setRolloverEnabled(false);

    boton.setBorder(
        BorderFactory.createLineBorder(DORADO, 2)
    );

    boton.setCursor(
        new java.awt.Cursor(
            java.awt.Cursor.HAND_CURSOR
        )
    );

    return boton;
}


    private void abrirVentana(JFrame ventana) {
        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new FrmPrincipal().setVisible(true)
        );
    }
}
