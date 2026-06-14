package indicafacil.app;

import indicafacil.ui.IndicaFacilFrame;
import indicafacil.ui.AppAlerts;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/*
 * Essa classe e a entrada principal do projeto.
 * Aqui eu so subo a interface Swing e deixo o resto com as telas.
 */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exception) {
            // Mantem o look padrao se o sistema nao permitir alterar.
        }

        // Esse startup roda fora da interface porque a preparacao do banco pode demorar um pouco.
        Thread startupThread = new Thread(() -> {
            try {
                IndicaFacilApplicationContext applicationContext = new IndicaFacilApplicationContext();
                SwingUtilities.invokeLater(() -> new IndicaFacilFrame(applicationContext).setVisible(true));
            } catch (IllegalStateException exception) {
                SwingUtilities.invokeLater(() ->
                    AppAlerts.showError(null, "Falha ao iniciar o aplicativo: " + exception.getMessage())
                );
            }
        }, "indicafacil-startup");

        startupThread.setDaemon(true);
        startupThread.start();
    }
}
