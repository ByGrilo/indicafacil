package indicafacil.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/*
 * Painel arredondado reutilizavel pra cards e blocos da interface.
 * Ele evita repetir a mesma pintura personalizada em varias telas.
 */
public class RoundedPanel extends JPanel {
    private final int arc;

    public RoundedPanel() {
        this(28);
    }

    public RoundedPanel(int arc) {
        this.arc = arc;
        setOpaque(false);
        setBackground(UITheme.SURFACE);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(getBackground() == null ? Color.WHITE : getBackground());
        graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
        graphics2D.dispose();
        super.paintComponent(graphics);
    }
}
