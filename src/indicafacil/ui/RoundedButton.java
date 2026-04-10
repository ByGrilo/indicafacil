package indicafacil.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

/*
 * Botao arredondado basico usado nas telas.
 * Eu deixei essa classe separada pra manter o visual padrao do app inteiro.
 */
public class RoundedButton extends JButton {
    private final Color backgroundColor;
    private final Color foregroundColor;
    private final Color borderColor;

    public RoundedButton(String text, Color backgroundColor, Color foregroundColor, Color borderColor) {
        super(text);
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.borderColor = borderColor;
        super.setForeground(foregroundColor);
        setFocusPainted(false);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFont(UITheme.labelFont(16));
        setPreferredSize(new Dimension(320, 56));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Escurece um pouco quando aperta so pra dar resposta visual.
        graphics2D.setColor(getModel().isPressed() ? backgroundColor.darker() : backgroundColor);
        graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
        graphics2D.setColor(borderColor);
        graphics2D.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 28, 28);
        graphics2D.dispose();
        super.paintComponent(graphics);
    }
}
