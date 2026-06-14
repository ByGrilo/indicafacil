package indicafacil.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

/*
 * Botao base do app. Fiz com estilos porque a interface nova usa muito o mesmo formato.
 */
public class AppButton extends JButton {
    public enum Style {
        PRIMARY,
        SECONDARY,
        DANGER,
        LINK
    }

    private final Style style;
    private final int arc;

    public AppButton(String text) {
        this(text, Style.PRIMARY);
    }

    public AppButton(String text, Style style) {
        this(text, style, 50);
    }

    public AppButton(String text, Style style, int height) {
        super(text);
        this.style = style;
        this.arc = 22;
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setFont(AppTheme.labelFont(16));
        setForeground(resolveForeground());
        setPreferredSize(new Dimension(320, height));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if (style == Style.LINK) {
            super.paintComponent(graphics);
            return;
        }

        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(getModel().isPressed() ? resolveBackground().darker() : resolveBackground());
        graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
        graphics2D.setColor(resolveBorder());
        graphics2D.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
        graphics2D.dispose();
        super.paintComponent(graphics);
    }

    private Color resolveBackground() {
        if (style == Style.SECONDARY) {
            return AppTheme.SURFACE;
        }
        if (style == Style.DANGER) {
            return AppTheme.DANGER;
        }
        return AppTheme.ACCENT;
    }

    private Color resolveForeground() {
        if (style == Style.SECONDARY) {
            return AppTheme.PRIMARY_TEXT;
        }
        if (style == Style.LINK) {
            return AppTheme.SECONDARY_TEXT;
        }
        return Color.WHITE;
    }

    private Color resolveBorder() {
        if (style == Style.SECONDARY) {
            return AppTheme.BORDER;
        }
        return resolveBackground();
    }
}
