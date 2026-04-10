package indicafacil.app;

import indicafacil.ui.IndicaFacilFrame;
import indicafacil.ui.SwingDialogs;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/*
 * Essa classe e a entrada principal do projeto.
 * Aqui eu so subo a interface Swing e deixo o resto com as telas.
 */
public class Main {
    public static void main(String[] args) {
        // O Swing gosta de iniciar a interface dentro dessa thread propria dele.
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception exception) {
                // Mantem o look padrao se o sistema nao permitir alterar.
            }

            try {
                // A partir daqui o app visual comeca de verdade.
                new IndicaFacilFrame().setVisible(true);
            } catch (IllegalStateException exception) {
                SwingDialogs.showError(null, "Falha ao iniciar o aplicativo: " + exception.getMessage());
            }
        });
    }
}
