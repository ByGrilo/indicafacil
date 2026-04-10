package indicafacil.ui;

import indicafacil.model.AtividadeUsuario;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Essa e a tela de atividade do usuario.
 * Ela mostra o historico das avaliacoes feitas com os dados principais de cada profissional.
 */
public class ActivityPanel extends JPanel {
    private final IndicaFacilFrame app;
    private final JPanel atividadesContainer;

    public ActivityPanel(IndicaFacilFrame app) {
        this.app = app;
        this.atividadesContainer = new JPanel();
        this.atividadesContainer.setOpaque(false);
        this.atividadesContainer.setLayout(new BoxLayout(atividadesContainer, BoxLayout.Y_AXIS));

        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);
        add(criarConteudo(), BorderLayout.CENTER);
    }

    private Component criarConteudo() {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(28, 22, 28, 22));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Atividade");
        title.setFont(UITheme.titleFont(30));
        title.setForeground(UITheme.PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(atividadesContainer);

        return UITheme.createScrollPane(content);
    }

    public void refreshData() {
        atividadesContainer.removeAll();

        if (app.getUsuarioLogado() == null) {
            return;
        }

        // Toda vez que entra nessa aba eu monto a lista de novo com o estado atual.
        List<AtividadeUsuario> atividades = app.getIndicaFacilService().listarAtividadeDoUsuario(app.getUsuarioLogado().getId());
        if (atividades.isEmpty()) {
            JLabel empty = new JLabel("Voce ainda nao tem atividade registrada.");
            empty.setFont(UITheme.subtitleFont(15));
            empty.setForeground(UITheme.MUTED);
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            atividadesContainer.add(empty);
        } else {
            for (AtividadeUsuario atividade : atividades) {
                RoundedPanel card = new RoundedPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
                card.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel nome = new JLabel(atividade.getTrabalhadorNome() + "  " + String.format("%.1f estrela(s)", atividade.getMediaTrabalhador()));
                nome.setFont(UITheme.labelFont(18));
                nome.setForeground(UITheme.PRIMARY);

                JLabel categoria = new JLabel(atividade.getCategoria().getDescricao());
                categoria.setFont(UITheme.subtitleFont(16));
                categoria.setForeground(UITheme.PRIMARY);

                JLabel nota = new JLabel("Sua avaliacao: " + atividade.getNotaDada() + " estrela(s)");
                nota.setFont(UITheme.subtitleFont(15));
                nota.setForeground(UITheme.PRIMARY);

                JLabel comentario = new JLabel(atividade.getComentario());
                comentario.setFont(UITheme.subtitleFont(14));
                comentario.setForeground(UITheme.MUTED);

                JLabel data = new JLabel("Data: " + atividade.getDataAvaliacao().toString());
                data.setFont(UITheme.subtitleFont(13));
                data.setForeground(UITheme.MUTED);

                card.add(nome);
                card.add(Box.createRigidArea(new Dimension(0, 4)));
                card.add(categoria);
                card.add(Box.createRigidArea(new Dimension(0, 10)));
                card.add(nota);
                card.add(Box.createRigidArea(new Dimension(0, 6)));
                card.add(comentario);
                card.add(Box.createRigidArea(new Dimension(0, 6)));
                card.add(data);

                atividadesContainer.add(card);
                atividadesContainer.add(Box.createRigidArea(new Dimension(0, 14)));
            }
        }

        atividadesContainer.revalidate();
        atividadesContainer.repaint();
    }
}
