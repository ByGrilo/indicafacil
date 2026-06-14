package indicafacil.ui;

import indicafacil.model.AtividadeUsuario;
import indicafacil.model.PerfilTrabalhador;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/*
 * Tela de avaliacao do profissional.
 */
public class ProfessionalReviewScreen extends AbstractScreenPanel {
    private final JPanel root;
    private PerfilTrabalhador perfilAtual;
    private ReviewSnapshot snapshotAtual;

    public ProfessionalReviewScreen(IndicaFacilFrame app) {
        super(app);
        this.root = new JPanel();
        root.setOpaque(false);
        root.setBorder(BorderFactory.createEmptyBorder(30, 24, 24, 24));
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        setScreenContent(AppTheme.createScrollPane(root));
    }

    @Override
    public AppScreenKey getScreenKey() {
        return AppScreenKey.REVIEW;
    }

    @Override
    public boolean usesBottomNavigation() {
        return false;
    }

    @Override
    public void onScreenOpened(Object context) {
        if (context instanceof PerfilTrabalhador) {
            perfilAtual = (PerfilTrabalhador) context;
        }
        carregarDadosDaTela();
    }

    private void carregarDadosDaTela() {
        root.removeAll();

        if (perfilAtual == null || app.getUsuarioLogado() == null) {
            root.add(new JLabel("Profissional nao encontrado."));
            root.revalidate();
            root.repaint();
            return;
        }

        long token = nextRequestToken();
        PerfilTrabalhador perfilSnapshot = perfilAtual;
        long usuarioId = app.getUsuarioLogado().getId();
        showLoading(perfilSnapshot);

        UiTaskRunner.run(
            this,
            "carregar avaliacao do profissional",
            () -> new ReviewSnapshot(
                app.getIndicaFacilService().buscarAvaliacaoDoUsuario(usuarioId, perfilSnapshot.getId()),
                app.getIndicaFacilService().trabalhadorEstaNosFavoritos(usuarioId, perfilSnapshot.getId())
            ),
            () -> isCurrentRequest(token)
                && perfilAtual != null
                && perfilAtual.getId() == perfilSnapshot.getId()
                && app.getUsuarioLogado() != null
                && app.getUsuarioLogado().getId() == usuarioId,
            snapshot -> {
                snapshotAtual = snapshot;
                rebuild();
            }
        );
    }

    private void rebuild() {
        root.removeAll();

        if (perfilAtual == null) {
            root.add(new JLabel("Profissional nao encontrado."));
            return;
        }

        JButton backButton = createScreenBackButton("Voltar", () -> getShell().showProfessionals(perfilAtual.getCategoria()));

        AvatarView avatar = new AvatarView(perfilAtual.getNome(), 120, AppTheme.ACCENT);
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nome = new JLabel(perfilAtual.getNome());
        nome.setFont(AppTheme.titleFont(24));
        nome.setForeground(AppTheme.PRIMARY_TEXT);
        nome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel categoria = new JLabel(perfilAtual.getCategoria().getDescricao());
        categoria.setFont(AppTheme.subtitleFont(17));
        categoria.setForeground(AppTheme.SECONDARY_TEXT);
        categoria.setAlignmentX(Component.CENTER_ALIGNMENT);

        AppCardPanel card = new AppCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(26, 22, 24, 22));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel instrucoes = new JLabel("Avalie o profissional:");
        instrucoes.setFont(AppTheme.labelFont(18));
        instrucoes.setForeground(AppTheme.PRIMARY_TEXT);
        instrucoes.setAlignmentX(Component.CENTER_ALIGNMENT);

        Optional<AtividadeUsuario> avaliacaoExistente = snapshotAtual == null
            ? Optional.empty()
            : snapshotAtual.avaliacaoExistente();

        root.add(backButton);
        root.add(Box.createRigidArea(new Dimension(0, 18)));
        root.add(avatar);
        root.add(Box.createRigidArea(new Dimension(0, 14)));
        root.add(nome);
        root.add(Box.createRigidArea(new Dimension(0, 4)));
        root.add(categoria);
        root.add(Box.createRigidArea(new Dimension(0, 28)));

        card.add(instrucoes);
        card.add(Box.createRigidArea(new Dimension(0, 14)));

        if (avaliacaoExistente.isPresent()) {
            addReadonlyReview(card, avaliacaoExistente.get());
        } else {
            addEditableReview(card);
        }

        root.add(card);

        root.revalidate();
        root.repaint();
    }

    private void addReadonlyReview(JPanel card, AtividadeUsuario atividade) {
        StarRatingView stars = new StarRatingView(atividade.getNotaDada());
        stars.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea comentarioArea = new JTextArea(atividade.getComentario());
        configurarArea(comentarioArea, false);

        javax.swing.JButton favoriteLink = AppTheme.createLinkButton(resolveFavoriteText());
        favoriteLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        favoriteLink.addActionListener(event -> toggleFavorite());

        card.add(stars);
        card.add(Box.createRigidArea(new Dimension(0, 18)));
        card.add(wrapArea(comentarioArea));
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(favoriteLink);
    }

    private void addEditableReview(JPanel card) {
        StarRatingView stars = new StarRatingView();
        stars.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea comentarioArea = new PlaceholderTextArea("Deixe um coment\u00e1rio...");
        configurarArea(comentarioArea, true);

        AppButton saveButton = new AppButton("Salvar avalia\u00e7\u00e3o");
        saveButton.addActionListener(event -> saveReview(stars, comentarioArea));

        javax.swing.JButton favoriteLink = AppTheme.createLinkButton(resolveFavoriteText());
        favoriteLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        favoriteLink.addActionListener(event -> toggleFavorite());

        card.add(stars);
        card.add(Box.createRigidArea(new Dimension(0, 18)));
        card.add(wrapArea(comentarioArea));
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        card.add(saveButton);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(favoriteLink);
    }

    private void configurarArea(JTextArea area, boolean editavel) {
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(editavel);
        area.setFont(AppTheme.subtitleFont(15));
        area.setBorder(AppTheme.createInputBorder());
        area.setBackground(AppTheme.SURFACE);
        if (editavel) {
            area.setText("");
        }
    }

    private JPanel wrapArea(JTextArea area) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
        wrapper.add(new javax.swing.JScrollPane(area), BorderLayout.CENTER);
        return wrapper;
    }

    private String resolveFavoriteText() {
        boolean favorito = snapshotAtual != null && snapshotAtual.favorito();
        return favorito ? "Remover dos favoritos" : "Adicionar aos favoritos";
    }

    private void saveReview(StarRatingView stars, JTextArea comentarioArea) {
        PerfilTrabalhador perfilSnapshot = perfilAtual;
        indicafacil.auth.model.UsuarioConta usuarioSnapshot = app.getUsuarioLogado();
        if (perfilSnapshot == null || usuarioSnapshot == null) {
            return;
        }

        UiTaskRunner.run(
            this,
            "salvar avaliacao",
            () -> {
                app.getIndicaFacilService().registrarAvaliacao(
                    usuarioSnapshot,
                    perfilSnapshot.getId(),
                    stars.getRating(),
                    comentarioArea.getText(),
                    false
                );
                return null;
            },
            ignored -> {
                AppAlerts.showInfo(this, "Avalia\u00e7\u00e3o registrada com sucesso.");
                carregarDadosDaTela();
            }
        );
    }

    private void toggleFavorite() {
        PerfilTrabalhador perfilSnapshot = perfilAtual;
        indicafacil.auth.model.UsuarioConta usuarioSnapshot = app.getUsuarioLogado();
        if (perfilSnapshot == null || usuarioSnapshot == null) {
            return;
        }

        UiTaskRunner.run(
            this,
            "alternar favorito",
            () -> app.getIndicaFacilService().alternarFavorito(usuarioSnapshot, perfilSnapshot.getId()),
            favoritado -> {
                AppAlerts.showInfo(this, favoritado ? "Profissional adicionado aos favoritos." : "Profissional removido dos favoritos.");
                carregarDadosDaTela();
            }
        );
    }

    private void showLoading(PerfilTrabalhador perfil) {
        JButton backButton = createScreenBackButton("Voltar", () -> getShell().showProfessionals(perfil.getCategoria()));
        root.add(backButton);
        root.add(Box.createRigidArea(new Dimension(0, 24)));

        JLabel loading = new JLabel("Carregando dados do profissional...");
        loading.setFont(AppTheme.subtitleFont(15));
        loading.setForeground(AppTheme.SECONDARY_TEXT);
        loading.setAlignmentX(Component.CENTER_ALIGNMENT);
        root.add(loading);
        root.revalidate();
        root.repaint();
    }

    private record ReviewSnapshot(
        Optional<AtividadeUsuario> avaliacaoExistente,
        boolean favorito
    ) {
    }
}
