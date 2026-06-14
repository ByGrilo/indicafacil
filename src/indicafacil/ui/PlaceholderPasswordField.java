package indicafacil.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPasswordField;

/*
 * Mesma ideia do campo de texto, so que pra senha.
 */
public class PlaceholderPasswordField extends JPasswordField {
    private final String placeholder;

    public PlaceholderPasswordField(String placeholder) {
        this.placeholder = placeholder == null ? "" : placeholder;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (getPassword().length > 0 || placeholder.isEmpty()) {
            return;
        }

        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setColor(AppTheme.SECONDARY_TEXT.brighter());
        graphics2D.setFont(getFont());
        graphics2D.drawString(placeholder, getInsets().left, getHeight() / 2 + graphics2D.getFontMetrics().getAscent() / 2 - 2);
        graphics2D.dispose();
    }
}
