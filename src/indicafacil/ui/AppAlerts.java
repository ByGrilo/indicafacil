package indicafacil.ui;

import java.awt.Component;
import javax.swing.JOptionPane;

/*
 * Centralizei os avisos aqui so pra nao repetir JOptionPane em toda tela.
 */
public final class AppAlerts {
    private AppAlerts() {
    }

    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "IndicaFacil", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "IndicaFacil", JOptionPane.ERROR_MESSAGE);
    }
}
