package indicafacil.ui;

import indicafacil.model.PerfilTrabalhador;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

/*
 * Card pequeno pros favoritos da home.
 */
public class FavoriteProfileChip extends AppCardPanel {
    public FavoriteProfileChip(PerfilTrabalhador perfil, Runnable action) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setPreferredSize(new Dimension(92, 114));

        AvatarView avatar = new AvatarView(perfil.getNome(), 52);
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nome = new JLabel(perfil.getPrimeiroNome());
        nome.setFont(AppTheme.subtitleFont(14));
        nome.setForeground(AppTheme.PRIMARY_TEXT);
        nome.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalGlue());
        add(avatar);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(nome);
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
