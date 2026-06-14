package indicafacil.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/*
 * Aqui fica o tema visual do app.
 * O modo claro e escuro passam por essa classe pra nao virar bagunca.
 */
public final class AppTheme {
    public enum ThemeMode {
        LIGHT,
        DARK
    }

    public static Color BACKGROUND;
    public static Color SURFACE;
    public static Color PRIMARY_TEXT;
    public static Color SECONDARY_TEXT;
    public static Color BORDER;
    public static Color ACCENT;
    public static Color ACCENT_DARK;
    public static Color DANGER;
    public static Color WARNING;

    private static ThemeMode currentMode;

    static {
        applyTheme(ThemeMode.LIGHT);
    }

    private AppTheme() {
    }

    public static void toggleTheme() {
        applyTheme(currentMode == ThemeMode.DARK ? ThemeMode.LIGHT : ThemeMode.DARK);
    }

    public static void applyTheme(ThemeMode mode) {
        currentMode = mode;

        if (mode == ThemeMode.DARK) {
            BACKGROUND = new Color(24, 27, 33);
            SURFACE = new Color(39, 44, 53);
            PRIMARY_TEXT = new Color(243, 245, 247);
            SECONDARY_TEXT = new Color(170, 178, 189);
            BORDER = new Color(73, 79, 90);
            ACCENT = new Color(118, 201, 31);
            ACCENT_DARK = new Color(95, 164, 22);
            DANGER = new Color(244, 98, 98);
            WARNING = new Color(255, 201, 51);
            return;
        }

        BACKGROUND = new Color(243, 242, 239);
        SURFACE = Color.WHITE;
        PRIMARY_TEXT = new Color(23, 23, 23);
        SECONDARY_TEXT = new Color(112, 112, 112);
        BORDER = new Color(229, 229, 229);
        ACCENT = new Color(118, 201, 31);
        ACCENT_DARK = new Color(95, 164, 22);
        DANGER = new Color(218, 55, 55);
        WARNING = new Color(255, 201, 51);
    }

    public static boolean isDarkMode() {
        return currentMode == ThemeMode.DARK;
    }

    public static String getAppearanceLabel() {
        return isDarkMode() ? "Modo escuro" : "Modo claro";
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

    public static JTextField createTextField() {
        return createTextField("");
    }

    public static JTextField createTextField(String placeholder) {
        JTextField field = new PlaceholderTextField(placeholder);
        styleInput(field);
        return field;
    }

    public static JPasswordField createPasswordField() {
        return createPasswordField("");
    }

    public static JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new PlaceholderPasswordField(placeholder);
        styleInput(field);
        return field;
    }

    public static JButton createLinkButton(String text) {
        JButton button = new JButton(text);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(SECONDARY_TEXT);
        button.setFont(subtitleFont(15));
        button.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return button;
    }

    public static JButton createBackButton(String text) {
        JButton button = new JButton("\u2190  " + text);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(PRIMARY_TEXT);
        button.setFont(labelFont(15));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        return button;
    }

    public static <T> void styleComboBox(JComboBox<T> comboBox) {
        comboBox.setFont(subtitleFont(14));
        comboBox.setForeground(PRIMARY_TEXT);
        comboBox.setBackground(SURFACE);
        comboBox.setBorder(createInputBorder());
        comboBox.setFocusable(false);
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus
            ) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                label.setBackground(isSelected ? ACCENT : SURFACE);
                label.setForeground(isSelected ? Color.WHITE : PRIMARY_TEXT);
                return label;
            }
        });
    }

    public static JScrollPane createScrollPane(JComponent component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BACKGROUND);
        scrollPane.setBackground(BACKGROUND);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    public static Border createInputBorder() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)
        );
    }

    private static void styleInput(JTextField field) {
        field.setFont(subtitleFont(15));
        field.setForeground(PRIMARY_TEXT);
        field.setBackground(SURFACE);
        field.setCaretColor(PRIMARY_TEXT);
        field.setPreferredSize(new Dimension(320, 50));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        field.setBorder(createInputBorder());
    }
}
