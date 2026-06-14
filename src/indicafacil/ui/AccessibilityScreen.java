package indicafacil.ui;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Tela secundaria com opcoes extras da conta.
 */
public class AccessibilityScreen extends AbstractScreenPanel {
    public AccessibilityScreen(IndicaFacilFrame app) {
        super(app);
        setScreenContent(buildContent());
    }

    @Override
    public AppScreenKey getScreenKey() {
        return AppScreenKey.ACCESSIBILITY;
    }

    @Override
    public boolean usesBottomNavigation() {
        return false;
    }

    private Component buildContent() {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(28, 24, 28, 24));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JButton backButton = createScreenBackButton("Voltar", () -> getShell().showSettings());

        JLabel title = new JLabel("Acessibilidade");
        title.setFont(AppTheme.titleFont(24));
        title.setForeground(AppTheme.PRIMARY_TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        AppCardPanel card = new AppCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(new SettingsOptionRow("\u263a", "Sua conta", "Veja informa\u00e7\u00f5es da sua conta", AppTheme.PRIMARY_TEXT, () -> getShell().showSettings()));
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(new SettingsOptionRow("\u25d4", "Privacidade", "Entenda nossas pol\u00edticas", AppTheme.PRIMARY_TEXT, () ->
            AppAlerts.showInfo(this, "Seus dados privados continuam protegidos no app.")));
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(new SettingsOptionRow("\u263d", "Apar\u00eancia", AppTheme.getAppearanceLabel(), AppTheme.PRIMARY_TEXT, () ->
            app.toggleAppearance(AppScreenKey.ACCESSIBILITY)));
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(new SettingsOptionRow("\u24d8", "Sobre", "Resumo do projeto", AppTheme.PRIMARY_TEXT, () ->
            AppAlerts.showInfo(this, "IndicaFacil conecta clientes e prestadores com busca, favoritos e avaliacoes.")));
        card.add(Box.createRigidArea(new Dimension(0, 18)));
        card.add(new javax.swing.JSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 18)));
        card.add(new SettingsOptionRow("\u21aa", "Sair", "", AppTheme.DANGER, app::logout, false));

        content.add(backButton);
        content.add(Box.createRigidArea(new Dimension(0, 12)));
        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 24)));
        content.add(card);

        return AppTheme.createScrollPane(content);
    }
}
