package indicafacil.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/*
 * Essa classe centraliza as cores, fontes e componentes basicos da interface.
 * Fiz assim pra nao ter configuracao visual espalhada por todo lado.
 */
public final class UITheme {
    public static final Color BACKGROUND = new Color(243, 241, 238);
    public static final Color SURFACE = Color.WHITE;
    public static final Color PRIMARY = new Color(17, 17, 17);
    public static final Color MUTED = new Color(120, 120, 120);
    public static final Color BORDER = new Color(223, 223, 223);
    public static final Color SHADOW = new Color(0, 0, 0, 15);
    public static final Color SUCCESS = new Color(35, 132, 76);

    private UITheme() {
    }

    public static Font titleFont(float size) {
        return new Font("SansSerif", Font.BOLD, Math.round(size));
    }

    public static Font subtitleFont(float size) {
        return new Font("SansSerif", Font.PLAIN, Math.round(size));
    }

    public static Font labelFont(float size) {
        return new Font("SansSerif", Font.BOLD, Math.round(size));
    }

    public static Font bodyFont(float size) {
        return new Font("SansSerif", Font.PLAIN, Math.round(size));
    }

    public static JTextField createTextField() {
        JTextField field = new JTextField();
        styleInput(field);
        return field;
    }

    public static JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        styleInput(field);
        return field;
    }

    public static RoundedButton createPrimaryButton(String text) {
        return new RoundedButton(text, PRIMARY, SURFACE, PRIMARY);
    }

    public static RoundedButton createSecondaryButton(String text) {
        return new RoundedButton(text, SURFACE, PRIMARY, BORDER);
    }

    public static JButton createLinkButton(String text) {
        JButton button = new JButton(text);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(MUTED);
        button.setFont(bodyFont(14));
        button.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        return button;
    }

    public static JScrollPane createScrollPane(JComponent component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BACKGROUND);
        scrollPane.setBackground(BACKGROUND);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private static void styleInput(JTextField field) {
        // Todo input passa por aqui pra manter o mesmo tamanho e o mesmo visual.
        field.setFont(bodyFont(15));
        field.setForeground(PRIMARY);
        field.setBackground(SURFACE);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        field.setPreferredSize(new Dimension(320, 52));
        field.setBorder(createInputBorder());
        field.setCaretColor(PRIMARY);
    }

    private static Border createInputBorder() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)
        );
    }
}
