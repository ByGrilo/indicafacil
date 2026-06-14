package indicafacil.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JTextField;

/*
 * Esse campo desenha o placeholder por cima quando ainda esta vazio.
 * Fiz isso porque no Swing nao vem pronto igual em app de celular.
 */
public class PlaceholderTextField extends JTextField {
    private final String placeholder;

    public PlaceholderTextField(String placeholder) {
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
        graphics2D.drawString(placeholder, getInsets().left, getHeight() / 2 + graphics2D.getFontMetrics().getAscent() / 2 - 2);
        graphics2D.dispose();
    }
}
