package indicafacil.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JTextArea;

/*
 * Placeholder simples pro campo de comentario.
 */
public class PlaceholderTextArea extends JTextArea {
    private final String placeholder;

    public PlaceholderTextArea(String placeholder) {
        this.placeholder = placeholder == null ? "" : placeholder;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (!getText().isEmpty() || placeholder.isEmpty()) {
            return;
        }

        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setColor(AppTheme.SECONDARY_TEXT.brighter());
        graphics2D.setFont(getFont());
        graphics2D.drawString(placeholder, getInsets().left, getInsets().top + graphics2D.getFontMetrics().getAscent());
        graphics2D.dispose();
    }
}
