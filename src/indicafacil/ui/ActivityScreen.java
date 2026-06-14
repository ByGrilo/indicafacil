package indicafacil.ui;

import indicafacil.model.AtividadeUsuario;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Tela final de atividade.
 */
public class ActivityScreen extends AbstractScreenPanel {
    private final JPanel listContainer;

    public ActivityScreen(IndicaFacilFrame app) {
        super(app);
        this.listContainer = new JPanel();
        listContainer.setOpaque(false);
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        setScreenContent(buildContent());
    }

    @Override
    public AppScreenKey getScreenKey() {
        return AppScreenKey.ACTIVITY;
    }

    @Override
    public void refreshData() {
        listContainer.removeAll();

        if (app.getUsuarioLogado() == null) {
            return;
        }

        long token = nextRequestToken();
        long usuarioId = app.getUsuarioLogado().getId();
        showLoading();

        UiTaskRunner.run(
            this,
            "carregar atividade",
            () -> app.getIndicaFacilService().listarAtividadeDoUsuario(usuarioId),
            () -> isCurrentRequest(token) && app.getUsuarioLogado() != null && app.getUsuarioLogado().getId() == usuarioId,
            this::applyActivities
        );
    }

    private Component buildContent() {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(28, 24, 24, 24));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Atividade");
        title.setFont(AppTheme.titleFont(26));
        title.setForeground(AppTheme.PRIMARY_TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Suas avaliacoes");
        subtitle.setFont(AppTheme.subtitleFont(16));
        subtitle.setForeground(AppTheme.SECONDARY_TEXT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(subtitle);
        content.add(Box.createRigidArea(new Dimension(0, 16)));
        content.add(listContainer);

        return AppTheme.createScrollPane(content);
    }

    private void showLoading() {
        listContainer.removeAll();
        JLabel loading = new JLabel("Carregando suas avaliacoes...");
        loading.setFont(AppTheme.subtitleFont(15));
        loading.setForeground(AppTheme.SECONDARY_TEXT);
        listContainer.add(loading);
        listContainer.revalidate();
        listContainer.repaint();
    }

    private void applyActivities(List<AtividadeUsuario> atividades) {
        listContainer.removeAll();

        if (atividades.isEmpty()) {
            JLabel empty = new JLabel("Suas avaliacoes vao aparecer aqui.");
            empty.setFont(AppTheme.subtitleFont(15));
            empty.setForeground(AppTheme.SECONDARY_TEXT);
            listContainer.add(empty);
        } else {
            for (AtividadeUsuario atividade : atividades) {
                listContainer.add(new ActivityReviewCard(atividade));
                listContainer.add(Box.createRigidArea(new Dimension(0, 16)));
            }
        }

        listContainer.revalidate();
        listContainer.repaint();
    }
}
