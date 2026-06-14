package indicafacil.ui;

import indicafacil.model.PerfilTrabalhador;
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
 * Card da lista de profissionais.
 */
public class ProfessionalRowCard extends AppCardPanel {
    public ProfessionalRowCard(
        PerfilTrabalhador perfil,
        String detalheExtra,
        Runnable onOpen
    ) {
        setLayout(new BorderLayout(14, 0));
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        AvatarView avatar = new AvatarView(perfil.getNome(), 72);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JPanel top = new JPanel(new BorderLayout(8, 0));
        top.setOpaque(false);

        JLabel nome = new JLabel(perfil.getNome());
        nome.setFont(AppTheme.labelFont(19));
        nome.setForeground(AppTheme.PRIMARY_TEXT);

        JLabel nota = new JLabel(perfil.getMediaFormatada() + "\u2605");
        nota.setFont(AppTheme.labelFont(18));
        nota.setForeground(AppTheme.PRIMARY_TEXT);

        JLabel categoria = new JLabel(perfil.getCategoria().getDescricao());
        categoria.setFont(AppTheme.subtitleFont(16));
        categoria.setForeground(AppTheme.SECONDARY_TEXT);
        categoria.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel detalhe = new JLabel(detalheExtra);
        detalhe.setFont(AppTheme.subtitleFont(13));
        detalhe.setForeground(AppTheme.SECONDARY_TEXT);
        detalhe.setAlignmentX(Component.LEFT_ALIGNMENT);

        top.add(nome, BorderLayout.CENTER);
        top.add(nota, BorderLayout.EAST);

        info.add(Box.createVerticalGlue());
        info.add(top);
        info.add(Box.createRigidArea(new Dimension(0, 4)));
        info.add(categoria);
        if (detalheExtra != null && !detalheExtra.isBlank()) {
            info.add(Box.createRigidArea(new Dimension(0, 4)));
            info.add(detalhe);
        }
        info.add(Box.createVerticalGlue());

        add(avatar, BorderLayout.WEST);
        add(info, BorderLayout.CENTER);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                if (onOpen != null) {
                    onOpen.run();
                }
            }
        });
    }
}
