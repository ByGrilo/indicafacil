package indicafacil.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.concurrent.atomic.AtomicLong;
import javax.swing.JButton;
import javax.swing.JPanel;

/*
 * Base pras telas internas do app depois do login.
 * Cada tela especializada sobrescreve o que precisa e o shell navega em cima dessa abstracao.
 */
public abstract class AbstractScreenPanel extends JPanel {
    protected final IndicaFacilFrame app;
    private final AtomicLong requestSequence;
    private AppShellPanel shell;

    protected AbstractScreenPanel(IndicaFacilFrame app) {
        this.app = app;
        this.requestSequence = new AtomicLong();
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);
    }

    public abstract AppScreenKey getScreenKey();

    public boolean usesBottomNavigation() {
        return true;
    }

    public void onScreenOpened() {
        refreshData();
    }

    public void onScreenOpened(Object context) {
        onScreenOpened();
    }

    public void refreshData() {
    }

    protected final void setScreenContent(Component component) {
        removeAll();
        add(component, BorderLayout.CENTER);
    }

    protected AppShellPanel getShell() {
        return shell;
    }

    protected JButton createScreenBackButton(String text, Runnable action) {
        JButton button = AppTheme.createBackButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.addActionListener(event -> {
            if (action != null) {
                action.run();
            }
        });
        return button;
    }

    protected long nextRequestToken() {
        return requestSequence.incrementAndGet();
    }

    protected boolean isCurrentRequest(long token) {
        return isDisplayable() && requestSequence.get() == token;
    }

    void attachShell(AppShellPanel shell) {
        this.shell = shell;
    }
}
