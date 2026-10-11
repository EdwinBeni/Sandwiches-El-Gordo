
package com.sandwicheselgordo;

import com.sandwicheselgordo.vista.FrmPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class SandwichesElGordo {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception e) {
                System.err.println(
                    "No se pudo aplicar el estilo del sistema: "
                    + e.getMessage()
                );
            }

            FrmPrincipal ventana = new FrmPrincipal();
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }
}
