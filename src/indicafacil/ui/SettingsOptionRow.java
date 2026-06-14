package indicafacil.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Linha padrao das telas de configuracao e acessibilidade.
 */
public class SettingsOptionRow extends JPanel {
    public SettingsOptionRow(
        String iconText,
        String title,
        String subtitle,
        java.awt.Color titleColor,
        Runnable onClick
    ) {
        this(iconText, title, subtitle, titleColor, onClick, true);
    }

    public SettingsOptionRow(
        String iconText,
        String title,
        String subtitle,
        java.awt.Color titleColor,
        Runnable onClick,
        boolean showChevron
    ) {
        setOpaque(false);
        setLayout(new BorderLayout(10, 0));
        setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel icon = new JLabel(iconText);
        icon.setFont(AppTheme.labelFont(18));
        icon.setForeground(titleColor);

        JPanel texts = new JPanel();
        texts.setOpaque(false);
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppTheme.labelFont(17));
        titleLabel.setForeground(titleColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        texts.add(titleLabel);
        if (subtitle != null && !subtitle.trim().isEmpty()) {
            JLabel subtitleLabel = new JLabel(subtitle);
            subtitleLabel.setFont(AppTheme.subtitleFont(13));
            subtitleLabel.setForeground(AppTheme.SECONDARY_TEXT);
            subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            texts.add(Box.createRigidArea(new Dimension(0, 2)));
            texts.add(subtitleLabel);
        }

        JLabel chevron = new JLabel(">");
        chevron.setFont(AppTheme.subtitleFont(16));
        chevron.setForeground(AppTheme.SECONDARY_TEXT);

        add(icon, BorderLayout.WEST);
        add(texts, BorderLayout.CENTER);
        if (showChevron) {
            add(chevron, BorderLayout.EAST);
        }

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                if (onClick != null) {
                    onClick.run();
                }
            }
        });
    }
}
