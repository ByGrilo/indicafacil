package indicafacil.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/*
 * Base pros cards brancos da interface.
 */
public class AppCardPanel extends JPanel {
    private final int arc;

    public AppCardPanel() {
        this(28);
    }

    public AppCardPanel(int arc) {
        this.arc = arc;
        setOpaque(false);
        setBackground(AppTheme.SURFACE);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(AppTheme.isDarkMode() ? new Color(0, 0, 0, 28) : new Color(0, 0, 0, 12));
        graphics2D.fillRoundRect(0, 4, getWidth(), getHeight() - 4, arc, arc);
        graphics2D.setColor(AppTheme.SURFACE);
        graphics2D.fillRoundRect(0, 0, getWidth(), getHeight() - 4, arc, arc);
        graphics2D.dispose();
        super.paintComponent(graphics);
    }
}
