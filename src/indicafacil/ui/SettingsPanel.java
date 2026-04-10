package indicafacil.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Essa tela guarda as opcoes mais gerais do app.
 * Por enquanto ela tem atalhos simples e o botao de sair da conta.
 */
public class SettingsPanel extends JPanel {
    private final IndicaFacilFrame app;
    private final MainMenuPanel menuPanel;

    public SettingsPanel(IndicaFacilFrame app, MainMenuPanel menuPanel) {
        this.app = app;
        this.menuPanel = menuPanel;

        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);
        add(criarConteudo(), BorderLayout.CENTER);
    }

    private Component criarConteudo() {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(28, 22, 28, 22));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Configuracoes");
        title.setFont(UITheme.titleFont(30));
        title.setForeground(UITheme.PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedPanel card = new RoundedPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(criarLinha("Sua conta", event -> menuPanel.showSection(MainMenuPanel.PERFIL)));
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(criarLinha("Privacidade", event -> SwingDialogs.showInfo(this,
            "CPF mascarado, dados privados e avaliacoes anonimas seguem as regras atuais do sistema.")));
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(criarLinha("Aparencia", event -> SwingDialogs.showInfo(this,
            "A interface foi adaptada para Swing com base nos wireframes recebidos.")));
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(criarLinha("Sobre", event -> SwingDialogs.showInfo(this,
            "IndicaFacil conecta clientes e prestadores de servico por meio de busca, favoritos e avaliacoes.")));
        card.add(Box.createRigidArea(new Dimension(0, 18)));

        RoundedButton sairButton = UITheme.createPrimaryButton("Sair da conta");
        sairButton.addActionListener(event -> app.logout());
        card.add(sairButton);

        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(card);

        return content;
    }

    private RoundedButton criarLinha(String titulo, java.awt.event.ActionListener actionListener) {
        RoundedButton button = UITheme.createSecondaryButton(titulo);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.addActionListener(actionListener);
        return button;
    }

    public void refreshData() {
        repaint();
    }
}
