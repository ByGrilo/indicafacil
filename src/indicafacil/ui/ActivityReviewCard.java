package indicafacil.ui;

import indicafacil.model.AtividadeUsuario;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/*
 * Card da tela de atividade.
 */
public class ActivityReviewCard extends AppCardPanel {
    public ActivityReviewCard(AtividadeUsuario atividade) {
        setLayout(new BorderLayout(14, 0));
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        AvatarView avatar = new AvatarView(atividade.getTrabalhadorNome(), 66);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel top = new JPanel(new BorderLayout(8, 0));
        top.setOpaque(false);

        JLabel nome = new JLabel(atividade.getTrabalhadorNome());
        nome.setFont(AppTheme.labelFont(18));
        nome.setForeground(AppTheme.PRIMARY_TEXT);

        JLabel notaProfissional = new JLabel(String.format("%.1f\u2605", atividade.getMediaTrabalhador()));
        notaProfissional.setFont(AppTheme.labelFont(18));
        notaProfissional.setForeground(AppTheme.PRIMARY_TEXT);

        JLabel categoria = new JLabel(atividade.getCategoria().getDescricao());
        categoria.setFont(AppTheme.subtitleFont(15));
        categoria.setForeground(AppTheme.SECONDARY_TEXT);

        JLabel suaAvaliacao = new JLabel("Sua avalia\u00e7\u00e3o: " + atividade.getNotaEmEstrelas());
        suaAvaliacao.setFont(AppTheme.labelFont(14));
        suaAvaliacao.setForeground(AppTheme.WARNING.darker());

        JLabel comentario = new JLabel("\"" + atividade.getComentarioCurto() + "\"");
        comentario.setFont(AppTheme.subtitleFont(14));
        comentario.setForeground(AppTheme.PRIMARY_TEXT);

        top.add(nome, BorderLayout.CENTER);
        top.add(notaProfissional, BorderLayout.EAST);

        content.add(Box.createVerticalGlue());
        content.add(top);
        content.add(Box.createRigidArea(new Dimension(0, 2)));
        content.add(categoria);
        content.add(Box.createRigidArea(new Dimension(0, 12)));

        JSeparator separator = new JSeparator();
        separator.setForeground(AppTheme.BORDER);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        content.add(separator);
        content.add(Box.createRigidArea(new Dimension(0, 12)));
        content.add(suaAvaliacao);
        content.add(Box.createRigidArea(new Dimension(0, 6)));
        content.add(comentario);
        content.add(Box.createVerticalGlue());

        add(avatar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
    }
}
