package indicafacil.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/*
 * Base pras telas de login, cadastro e senha.
 * Ela reaproveita o mesmo formato visual e deixa cada tela cuidar so do formulario dela.
 */
public abstract class AbstractAuthPanel extends JPanel {
    protected final IndicaFacilFrame app;

    protected AbstractAuthPanel(IndicaFacilFrame app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);
    }

    protected Component createAuthLayout(String title, String subtitle, Component formContent, Component footerContent) {
        return createAuthLayout(title, subtitle, formContent, footerContent, null);
    }

    protected Component createAuthLayout(
        String title,
        String subtitle,
        Component formContent,
        Component footerContent,
        Runnable onBack
    ) {
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.setMaximumSize(new Dimension(340, Integer.MAX_VALUE));

        if (onBack != null) {
            JButton backButton = AppTheme.createBackButton("Voltar");
            backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            backButton.addActionListener(event -> onBack.run());
            content.add(backButton);
            content.add(Box.createRigidArea(new Dimension(0, 24)));
        }

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppTheme.titleFont(32));
        titleLabel.setForeground(AppTheme.PRIMARY_TEXT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("<html><div style='width:320px;'>" + subtitle + "</div></html>");
        subtitleLabel.setFont(AppTheme.subtitleFont(18));
        subtitleLabel.setForeground(AppTheme.SECONDARY_TEXT);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (formContent instanceof javax.swing.JComponent) {
            ((javax.swing.JComponent) formContent).setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        content.add(titleLabel);
        content.add(Box.createRigidArea(new Dimension(0, 6)));
        content.add(subtitleLabel);
        content.add(Box.createRigidArea(new Dimension(0, 30)));
        content.add(formContent);
        if (footerContent != null) {
            if (footerContent instanceof javax.swing.JComponent) {
                ((javax.swing.JComponent) footerContent).setAlignmentX(Component.CENTER_ALIGNMENT);
            }
            content.add(Box.createRigidArea(new Dimension(0, 18)));
            content.add(footerContent);
        }

        wrapper.add(Box.createVerticalGlue());
        wrapper.add(content);
        wrapper.add(Box.createVerticalGlue());

        JScrollPane scrollPane = AppTheme.createScrollPane(wrapper);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    protected final void setPanelContent(Component content) {
        removeAll();
        add(content, BorderLayout.CENTER);
    }

    protected JPanel createDivider() {
        JPanel divider = new JPanel();
        divider.setOpaque(false);
        divider.setLayout(new BoxLayout(divider, BoxLayout.X_AXIS));
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));

        JPanel left = new JPanel();
        left.setOpaque(true);
        left.setBackground(AppTheme.BORDER);
        left.setPreferredSize(new Dimension(120, 1));
        left.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        JPanel right = new JPanel();
        right.setOpaque(true);
        right.setBackground(AppTheme.BORDER);
        right.setPreferredSize(new Dimension(120, 1));
        right.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        JLabel middle = new JLabel("ou");
        middle.setForeground(AppTheme.SECONDARY_TEXT);
        middle.setFont(AppTheme.subtitleFont(14));
        middle.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));

        divider.add(left);
        divider.add(middle);
        divider.add(right);
        return divider;
    }

    protected JButton createFooterLink(String htmlText, Runnable onClick) {
        JButton footer = AppTheme.createLinkButton(htmlText);
        footer.setFont(AppTheme.labelFont(16));
        footer.addActionListener(event -> {
            if (onClick != null) {
                onClick.run();
            }
        });
        return footer;
    }
}
