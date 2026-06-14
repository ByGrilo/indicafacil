package indicafacil.ui;

import indicafacil.model.CategoriaServico;
import indicafacil.model.PerfilTrabalhador;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.JPanel;

/*
 * Esse shell cuida da navegacao principal depois do login.
 * Ele usa CardLayout por baixo, mas as telas conversam por enum e nao por string solta.
 */
public class AppShellPanel extends JPanel {
    private final CardLayout contentLayout;
    private final JPanel contentPanel;
    private final BottomNavigationBar bottomNavigationBar;
    private final Map<AppScreenKey, AbstractScreenPanel> screens;
    private AppScreenKey currentScreenKey;

    public AppShellPanel(IndicaFacilFrame app) {
        this.contentLayout = new CardLayout();
        this.contentPanel = new JPanel(contentLayout);
        this.bottomNavigationBar = new BottomNavigationBar();
        this.screens = new EnumMap<>(AppScreenKey.class);

        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        registerScreen(new HomeScreen(app));
        registerScreen(new ProfessionalsScreen(app));
        registerScreen(new ProfessionalReviewScreen(app));
        registerScreen(new ActivityScreen(app));
        registerScreen(new SettingsScreen(app));
        registerScreen(new AccessibilityScreen(app));

        contentPanel.setOpaque(false);
        add(contentPanel, BorderLayout.CENTER);
        add(buildBottomNavigation(), BorderLayout.SOUTH);

        showScreen(AppScreenKey.HOME);
    }

    public void showHome() {
        showScreen(AppScreenKey.HOME);
    }

    public void showSettings() {
        showScreen(AppScreenKey.SETTINGS);
    }

    public void showAccessibility() {
        showScreen(AppScreenKey.ACCESSIBILITY);
    }

    public void showProfessionals(CategoriaServico categoria) {
        showScreen(AppScreenKey.PROFESSIONALS, categoria);
    }

    public void showProfessionals(String termo) {
        showScreen(AppScreenKey.PROFESSIONALS, termo);
    }

    public void showReview(PerfilTrabalhador perfil) {
        showScreen(AppScreenKey.REVIEW, perfil);
    }

    public void refreshAll() {
        for (AbstractScreenPanel screen : screens.values()) {
            screen.refreshData();
        }
    }

    public void refreshCurrentScreen() {
        if (currentScreenKey != null) {
            screens.get(currentScreenKey).refreshData();
        }
    }

    public void showScreen(AppScreenKey target) {
        showScreen(target, null);
    }

    public void showScreen(AppScreenKey target, Object context) {
        AbstractScreenPanel screen = screens.get(target);
        if (screen == null) {
            return;
        }

        currentScreenKey = target;
        screen.onScreenOpened(context);
        contentLayout.show(contentPanel, target.name());
        bottomNavigationBar.setSelected(target);
        bottomNavigationBar.setVisible(screen.usesBottomNavigation());
        revalidate();
        repaint();
    }

    private void registerScreen(AbstractScreenPanel screen) {
        screen.attachShell(this);
        screens.put(screen.getScreenKey(), screen);
        contentPanel.add(screen, screen.getScreenKey().name());
    }

    private BottomNavigationBar buildBottomNavigation() {
        bottomNavigationBar.addItem(createItem("\u2302", "In\u00edcio", AppScreenKey.HOME));
        bottomNavigationBar.addItem(createItem("\u2630", "Atividade", AppScreenKey.ACTIVITY));
        bottomNavigationBar.addItem(createItem("\u263b", "Conta", AppScreenKey.SETTINGS));
        return bottomNavigationBar;
    }

    private BottomNavigationItem createItem(String icon, String tooltip, AppScreenKey target) {
        BottomNavigationItem item = new BottomNavigationItem(icon, target);
        item.setToolTipText(tooltip);
        item.addActionListener(event -> showScreen(target));
        return item;
    }
}
