package indicafacil.ui;

import indicafacil.model.PerfilTrabalhador;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/*
 * Essa e a home depois do login.
 * Ela mostra saudacao, busca rapida, mais buscados e os favoritos do usuario.
 */
public class HomePanel extends JPanel {
    private final IndicaFacilFrame app;
    private final MainMenuPanel menuPanel;
    private final JLabel welcomeLabel;
    private final JTextField buscaField;
    private final JPanel maisBuscadosContainer;
    private final JPanel favoritosContainer;

    public HomePanel(IndicaFacilFrame app, MainMenuPanel menuPanel) {
        this.app = app;
        this.menuPanel = menuPanel;
        this.welcomeLabel = new JLabel();
        this.buscaField = UITheme.createTextField();
        this.maisBuscadosContainer = createVerticalContainer();
        this.favoritosContainer = createVerticalContainer();

        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);
        add(criarConteudo(), BorderLayout.CENTER);
    }

    private Component criarConteudo() {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(28, 22, 28, 22));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Bem-vindo");
        heading.setFont(UITheme.subtitleFont(20));
        heading.setForeground(UITheme.MUTED);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        welcomeLabel.setFont(UITheme.titleFont(32));
        welcomeLabel.setForeground(UITheme.PRIMARY);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedButton buscarButton = UITheme.createPrimaryButton("Buscar");
        buscarButton.addActionListener(event -> menuPanel.openSearch(buscaField.getText()));

        content.add(heading);
        content.add(Box.createRigidArea(new Dimension(0, 2)));
        content.add(welcomeLabel);
        content.add(Box.createRigidArea(new Dimension(0, 24)));
        content.add(buscaField);
        content.add(Box.createRigidArea(new Dimension(0, 12)));
        content.add(buscarButton);
        content.add(Box.createRigidArea(new Dimension(0, 24)));
        content.add(createSectionTitle("Mais buscados"));
        content.add(Box.createRigidArea(new Dimension(0, 12)));
        content.add(maisBuscadosContainer);
        content.add(Box.createRigidArea(new Dimension(0, 24)));
        content.add(createSectionTitle("Seus favoritos"));
        content.add(Box.createRigidArea(new Dimension(0, 12)));
        content.add(favoritosContainer);

        JScrollPane scrollPane = UITheme.createScrollPane(content);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        return scrollPane;
    }

    private JLabel createSectionTitle(String title) {
        JLabel label = new JLabel(title);
        label.setFont(UITheme.titleFont(18));
        label.setForeground(UITheme.PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createVerticalContainer() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    public void refreshData() {
        if (app.getUsuarioLogado() == null) {
            return;
        }

        // Atualiza os blocos principais sempre com o usuario atual.
        welcomeLabel.setText(app.getUsuarioLogado().getNome() + "!");
        rebuildList(maisBuscadosContainer, app.getIndicaFacilService().listarMaisBuscados());
        rebuildList(favoritosContainer, app.getIndicaFacilService().listarFavoritos(app.getUsuarioLogado().getId()));
    }

    private void rebuildList(JPanel container, List<PerfilTrabalhador> perfis) {
        container.removeAll();

        if (perfis.isEmpty()) {
            JLabel emptyLabel = new JLabel("Nenhum profissional para mostrar por enquanto.");
            emptyLabel.setFont(UITheme.subtitleFont(14));
            emptyLabel.setForeground(UITheme.MUTED);
            emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            container.add(emptyLabel);
        } else {
            for (PerfilTrabalhador perfil : perfis) {
                ProfessionalCardPanel card = new ProfessionalCardPanel(app, perfil, this::refreshData);
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                container.add(card);
                container.add(Box.createRigidArea(new Dimension(0, 14)));
            }
        }

        container.revalidate();
        container.repaint();
    }
}
