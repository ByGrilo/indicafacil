package indicafacil.ui;

import indicafacil.model.PerfilTrabalhador;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Card verde de destaque da home.
 */
public class HeroHighlightCard extends AppCardPanel {
    public HeroHighlightCard(PerfilTrabalhador perfil, Runnable onClick) {
        setLayout(new BorderLayout(18, 0));
        setBackground(AppTheme.ACCENT);
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setPreferredSize(new Dimension(340, 168));

        AvatarView avatar = new AvatarView(perfil.getNome(), 118, new Color(241, 142, 39));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("<html>" + perfil.getCategoria().getDescricao() + " melhor<br>avaliado do<br>m\u00eas</html>");
        label.setFont(AppTheme.labelFont(17));
        label.setForeground(Color.WHITE);

        JLabel nome = new JLabel(perfil.getPrimeiroNome() + "  " + perfil.getMediaFormatada() + "\u2605");
        nome.setFont(AppTheme.labelFont(18));
        nome.setForeground(Color.WHITE);

        content.add(Box.createVerticalGlue());
        content.add(label);
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        content.add(nome);
        content.add(Box.createVerticalGlue());

        add(avatar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);

        if (onClick != null) {
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent event) {
                    onClick.run();
                }
            });
        }
    }
}
