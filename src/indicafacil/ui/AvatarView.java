package indicafacil.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

/*
 * Como eu nao tenho fotos reais no projeto, esse avatar usa iniciais e cor fixa.
 * Fica limpo e ainda ajuda a dar cara visual pras telas.
 */
public class AvatarView extends JComponent {
    private final String initials;
    private final int size;
    private final Color backgroundColor;

    public AvatarView(String name) {
        this(name, 64);
    }

    public AvatarView(String name, int size) {
        this(name, size, pickColor(name));
    }

    public AvatarView(String name, int size, Color backgroundColor) {
        this.initials = buildInitials(name);
        this.size = size;
        this.backgroundColor = backgroundColor;
        setPreferredSize(new Dimension(size, size));
        setMinimumSize(new Dimension(size, size));
        setMaximumSize(new Dimension(size, size));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillOval(0, 0, size, size);
        graphics2D.setColor(backgroundColor);
        graphics2D.fillOval(4, 4, size - 8, size - 8);
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(AppTheme.labelFont(size / 3f));
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(initials);
        int textX = (size - textWidth) / 2;
        int textY = ((size - fontMetrics.getHeight()) / 2) + fontMetrics.getAscent();
        graphics2D.drawString(initials, textX, textY);
        graphics2D.dispose();
    }

    private static String buildInitials(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "?";
        }

        String[] partes = name.trim().split(" ");
        if (partes.length == 1) {
            return partes[0].substring(0, 1).toUpperCase();
        }

        return ("" + partes[0].charAt(0) + partes[1].charAt(0)).toUpperCase();
    }

    private static Color pickColor(String key) {
        int hash = Math.abs((key == null ? "indica" : key).hashCode());
        Color[] palette = {
            new Color(248, 156, 55),
            new Color(84, 141, 255),
            new Color(89, 187, 123),
            new Color(177, 120, 255),
            new Color(246, 104, 140)
        };
        return palette[hash % palette.length];
    }
}
