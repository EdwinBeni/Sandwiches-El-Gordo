package com.sandwicheselgordo.vista;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

/** Botones de fondo solido, independientes del Look and Feel. */
public final class EstiloBotones {
    private EstiloBotones() { }

    public static JButton crear(String texto) {
        return new BotonSolido(texto);
    }

    private static final class BotonSolido extends JButton {
        private static final long serialVersionUID = 1L;

        BotonSolido(String texto) {
            super(texto);
        }

        @Override
        public void updateUI() {
            // Se reinstala tambien al llamar a updateComponentTreeUI.
            setUI(new UIPlano());
            setOpaque(true);
            setContentAreaFilled(true);
            setRolloverEnabled(true);
        }
    }

    private static final class UIPlano extends BasicButtonUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton boton = (AbstractButton) c;
            g.setColor(fondo(boton));
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
            // BasicButtonUI conserva iconos, alineacion y comportamiento del modelo.
            super.paint(g, c);
        }

        @Override
        protected void paintButtonPressed(Graphics g, AbstractButton boton) {
            // El fondo ya se pinta; no se permite un relleno del tema.
        }

        @Override
        protected void paintText(Graphics g, AbstractButton boton,
                java.awt.Rectangle rect, String texto) {
            Color fondo = fondo(boton);
            Color preferido = boton.getForeground();
            g.setColor(contraste(preferido, fondo) >= 4.5
                    ? preferido : textoLegible(fondo));
            java.awt.FontMetrics fm = g.getFontMetrics();
            g.drawString(texto, rect.x, rect.y + fm.getAscent());
            int indice = boton.getDisplayedMnemonicIndex();
            if (indice >= 0 && indice < texto.length()) {
                int x = rect.x + fm.stringWidth(texto.substring(0, indice));
                int ancho = fm.charWidth(texto.charAt(indice));
                int y = rect.y + fm.getAscent() + 1;
                g.drawLine(x, y, x + ancho - 1, y);
            }
        }

        private Color fondo(AbstractButton boton) {
            Color base = boton.getBackground();
            if (base == null) { base = Color.LIGHT_GRAY; }
            base = new Color(base.getRed(), base.getGreen(), base.getBlue());
            if (!boton.isEnabled()) {
                int gris = (base.getRed() + base.getGreen() + base.getBlue()) / 3;
                return new Color(gris, gris, gris);
            }
            if (boton.getModel().isArmed() && boton.getModel().isPressed()) {
                return base.darker();
            }
            return base;
        }
    }

    private static Color textoLegible(Color fondo) {
        return contraste(Color.BLACK, fondo) >= contraste(Color.WHITE, fondo)
                ? Color.BLACK : Color.WHITE;
    }

    private static double contraste(Color a, Color b) {
        if (a == null) { return 0; }
        double la = luminancia(a);
        double lb = luminancia(b);
        return (Math.max(la, lb) + 0.05) / (Math.min(la, lb) + 0.05);
    }

    private static double luminancia(Color color) {
        return 0.2126 * canal(color.getRed())
                + 0.7152 * canal(color.getGreen())
                + 0.0722 * canal(color.getBlue());
    }

    private static double canal(int valor) {
        double c = valor / 255.0;
        return c <= 0.04045 ? c / 12.92 : Math.pow((c + 0.055) / 1.055, 2.4);
    }
}
