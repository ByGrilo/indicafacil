package indicafacil.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/*
 * Esse painel funciona como casco principal do app depois do login.
 * Ele segura a navegacao entre home, busca, atividade, perfil e configuracoes.
 */
public class MainMenuPanel extends JPanel {
    public static final String HOME = "HOME";
    public static final String BUSCA = "BUSCA";
    public static final String ATIVIDADE = "ATIVIDADE";
    public static final String PERFIL = "PERFIL";
    public static final String CONFIG = "CONFIG";

    private final CardLayout contentLayout;
    private final JPanel contentPanel;
    private final HomePanel homePanel;
    private final SearchPanel searchPanel;
    private final ActivityPanel activityPanel;
    private final ProfilePanel profilePanel;
    private final SettingsPanel settingsPanel;

    public MainMenuPanel(IndicaFacilFrame app) {
        this.contentLayout = new CardLayout();
        this.contentPanel = new JPanel(contentLayout);
        this.homePanel = new HomePanel(app, this);
        this.searchPanel = new SearchPanel(app, this);
        this.activityPanel = new ActivityPanel(app);
        this.profilePanel = new ProfilePanel(app, this);
        this.settingsPanel = new SettingsPanel(app, this);

        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);

        contentPanel.setOpaque(false);
        contentPanel.add(homePanel, HOME);
        contentPanel.add(searchPanel, BUSCA);
        contentPanel.add(activityPanel, ATIVIDADE);
        contentPanel.add(profilePanel, PERFIL);
        contentPanel.add(settingsPanel, CONFIG);

        add(contentPanel, BorderLayout.CENTER);
        add(criarBarraInferior(), BorderLayout.SOUTH);
    }

    private JPanel criarBarraInferior() {
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 14));
        navBar.setBackground(UITheme.SURFACE);
        navBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER));

        navBar.add(criarBotaoNavegacao("Home", event -> showHome()));
        navBar.add(criarBotaoNavegacao("Buscar", event -> showSection(BUSCA)));
        navBar.add(criarBotaoNavegacao("Atividade", event -> showSection(ATIVIDADE)));
        navBar.add(criarBotaoNavegacao("Perfil", event -> showSection(PERFIL)));
        navBar.add(criarBotaoNavegacao("Config", event -> showSection(CONFIG)));
        return navBar;
    }

    private JButton criarBotaoNavegacao(String texto, java.awt.event.ActionListener actionListener) {
        JButton button = new JButton(texto);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(UITheme.PRIMARY);
        button.setFont(UITheme.labelFont(14));
        button.addActionListener(actionListener);
        return button;
    }

    public void showHome() {
        showSection(HOME);
    }

    public void showSection(String section) {
        contentLayout.show(contentPanel, section);
        // Sempre que troca de aba eu recarrego os dados pra nao mostrar coisa velha.
        refreshAll();
    }

    public void openSearch(String termo) {
        searchPanel.setSearchTerm(termo);
        showSection(BUSCA);
        searchPanel.executeSearch();
    }

    public void refreshAll() {
        homePanel.refreshData();
        searchPanel.refreshData();
        activityPanel.refreshData();
        profilePanel.refreshData();
        settingsPanel.refreshData();
        revalidate();
        repaint();
    }
}
