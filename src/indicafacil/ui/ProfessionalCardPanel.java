package indicafacil.ui;

import indicafacil.model.PerfilTrabalhador;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Esse card mostra um profissional em listas da home, busca e favoritos.
 * Tambem ja deixa pronto o acesso rapido aos detalhes e ao botao de favorito.
 */
public class ProfessionalCardPanel extends RoundedPanel {
    public ProfessionalCardPanel(IndicaFacilFrame app, PerfilTrabalhador perfil, Runnable afterAction) {
        setLayout(new BorderLayout(12, 0));
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        RoundedPanel avatar = new RoundedPanel(48);
        avatar.setBackground(UITheme.BACKGROUND);
        avatar.setPreferredSize(new Dimension(76, 76));

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel nomeLabel = new JLabel(perfil.getNome());
        nomeLabel.setFont(UITheme.labelFont(18));
        nomeLabel.setForeground(UITheme.PRIMARY);
        nomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel categoriaLabel = new JLabel(perfil.getCategoria().getDescricao());
        categoriaLabel.setFont(UITheme.subtitleFont(16));
        categoriaLabel.setForeground(UITheme.PRIMARY);
        categoriaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descricaoLabel = new JLabel(perfil.getDescricao());
        descricaoLabel.setFont(UITheme.subtitleFont(14));
        descricaoLabel.setForeground(UITheme.MUTED);
        descricaoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel notaLabel = new JLabel(String.format("%.1f estrela(s)", perfil.getMediaAvaliacoes()));
        notaLabel.setFont(UITheme.labelFont(16));
        notaLabel.setForeground(UITheme.PRIMARY);
        notaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel actions = new JPanel();
        actions.setOpaque(false);
        actions.setLayout(new BoxLayout(actions, BoxLayout.X_AXIS));

        RoundedButton detalhesButton = UITheme.createSecondaryButton("Detalhes");
        detalhesButton.setPreferredSize(new Dimension(120, 42));
        detalhesButton.addActionListener(event -> SwingDialogs.showWorkerDetailDialog(this, app, perfil, afterAction));

        RoundedButton favoritoButton = UITheme.createPrimaryButton(
            app.getIndicaFacilService().trabalhadorEstaNosFavoritos(app.getUsuarioLogado().getId(), perfil.getId())
                ? "Desfavoritar"
                : "Favoritar"
        );
        favoritoButton.setPreferredSize(new Dimension(130, 42));
        favoritoButton.addActionListener(event -> {
            try {
                // Depois do clique eu atualizo a tela chamando o callback recebido.
                boolean favoritado = app.getIndicaFacilService().alternarFavorito(app.getUsuarioLogado(), perfil.getId());
                SwingDialogs.showInfo(this, favoritado ? "Profissional adicionado aos favoritos." : "Profissional removido dos favoritos.");
                if (afterAction != null) {
                    afterAction.run();
                }
            } catch (Exception exception) {
                SwingDialogs.showError(this, exception.getMessage());
            }
        });

        infoPanel.add(nomeLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        infoPanel.add(categoriaLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        infoPanel.add(descricaoLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(notaLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 14)));
        actions.add(detalhesButton);
        actions.add(Box.createRigidArea(new Dimension(10, 0)));
        actions.add(favoritoButton);
        infoPanel.add(actions);

        add(avatar, BorderLayout.WEST);
        add(infoPanel, BorderLayout.CENTER);
    }
}
