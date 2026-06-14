package indicafacil.ui;

import indicafacil.model.CategoriaServico;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

/*
 * Card pequeno da home pras categorias mais buscadas.
 */
public class CategoryShortcutCard extends AppCardPanel {
    public CategoryShortcutCard(CategoriaServico categoria, Runnable action) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 10, 12, 10));
        setPreferredSize(new Dimension(78, 88));

        JLabel badge = new JLabel(categoria.getIconeVisual());
        badge.setFont(AppTheme.titleFont(20));
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel("<html><center>" + categoria.getDescricao() + "</center></html>");
        label.setFont(AppTheme.subtitleFont(12));
        label.setForeground(AppTheme.PRIMARY_TEXT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(JLabel.CENTER);

        add(Box.createVerticalGlue());
        add(badge);
        add(Box.createRigidArea(new Dimension(0, 8)));
        add(label);
        add(Box.createVerticalGlue());

        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                if (action != null) {
                    action.run();
                }
            }
        });
    }
}
