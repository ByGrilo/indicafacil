package indicafacil.ui;

import indicafacil.model.CategoriaServico;
import indicafacil.model.PerfilTrabalhador;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Collections;
import java.awt.GridLayout;
import java.util.concurrent.CompletableFuture;
import java.util.List;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * Home final com atalhos, destaque e favoritos.
 */
public class HomeScreen extends AbstractScreenPanel {
    private final JLabel welcomeLabel;
    private final JTextField searchField;
    private final JPanel categoriesGrid;
    private final JPanel highlightContainer;
    private final JPanel favoritesRow;

    public HomeScreen(IndicaFacilFrame app) {
        super(app);
        this.welcomeLabel = new JLabel();
        this.searchField = AppTheme.createTextField("Busque por profissionais...");
        this.categoriesGrid = new JPanel(new GridLayout(2, 4, 12, 12));
        this.highlightContainer = new JPanel(new BorderLayout());
        this.favoritesRow = new JPanel();

        categoriesGrid.setOpaque(false);
        highlightContainer.setOpaque(false);
        favoritesRow.setOpaque(false);
        favoritesRow.setLayout(new BoxLayout(favoritesRow, BoxLayout.X_AXIS));
        categoriesGrid.setAlignmentX(Component.CENTER_ALIGNMENT);
        highlightContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        favoritesRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        setScreenContent(buildContent());
    }

    @Override
    public AppScreenKey getScreenKey() {
        return AppScreenKey.HOME;
    }

    @Override
    public void refreshData() {
        if (app.getUsuarioLogado() == null) {
            return;
        }

        rebuildCategories();
        long token = nextRequestToken();
        long usuarioId = app.getUsuarioLogado().getId();
        welcomeLabel.setText(app.getUsuarioLogado().getNome() + "!");
        showLoadingState();

        UiTaskRunner.run(
            this,
            "carregar tela inicial",
            () -> {
                // Essas duas buscas sao independentes, entao rodam em paralelo pra responder mais rapido.
                CompletableFuture<Optional<PerfilTrabalhador>> melhorFuture =
                    CompletableFuture.supplyAsync(() -> app.getIndicaFacilService().buscarMelhorAvaliado(), UiTaskRunner.getExecutor());
                CompletableFuture<List<PerfilTrabalhador>> favoritosFuture =
                    CompletableFuture.supplyAsync(() -> app.getIndicaFacilService().listarFavoritos(usuarioId, 4), UiTaskRunner.getExecutor());
                return new HomeSnapshot(melhorFuture.join(), favoritosFuture.join());
            },
            () -> isCurrentRequest(token) && app.getUsuarioLogado() != null && app.getUsuarioLogado().getId() == usuarioId,
            snapshot -> applySnapshot(snapshot)
        );
    }

    private Component buildContent() {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(28, 24, 28, 24));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel preTitle = new JLabel("Bem-Vindo");
        preTitle.setFont(AppTheme.subtitleFont(18));
        preTitle.setForeground(AppTheme.SECONDARY_TEXT);
        preTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        welcomeLabel.setFont(AppTheme.titleFont(28));
        welcomeLabel.setForeground(AppTheme.PRIMARY_TEXT);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        searchField.addActionListener(event -> openSearch());

        JLabel categoryTitle = createSectionTitle("Mais buscados");
        JLabel favoritesTitle = createSectionTitle("Seus favoritos");

        content.add(preTitle);
        content.add(Box.createRigidArea(new Dimension(0, 2)));
        content.add(welcomeLabel);
        content.add(Box.createRigidArea(new Dimension(0, 26)));
        content.add(searchField);
        content.add(Box.createRigidArea(new Dimension(0, 26)));
        content.add(categoryTitle);
        content.add(Box.createRigidArea(new Dimension(0, 14)));
        content.add(categoriesGrid);
        content.add(Box.createRigidArea(new Dimension(0, 22)));
        content.add(highlightContainer);
        content.add(Box.createRigidArea(new Dimension(0, 24)));
        content.add(favoritesTitle);
        content.add(Box.createRigidArea(new Dimension(0, 12)));
        content.add(favoritesRow);

        return AppTheme.createScrollPane(content);
    }

    private JLabel createSectionTitle(String title) {
        JLabel label = new JLabel(title);
        label.setFont(AppTheme.titleFont(18));
        label.setForeground(AppTheme.PRIMARY_TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void rebuildCategories() {
        categoriesGrid.removeAll();

        for (CategoriaServico categoria : CategoriaServico.listarAtalhosDaHome()) {
            categoriesGrid.add(new CategoryShortcutCard(categoria, () -> getShell().showProfessionals(categoria)));
        }

        categoriesGrid.revalidate();
        categoriesGrid.repaint();
    }

    private void rebuildHighlight() {
        rebuildHighlight(Optional.empty());
    }

    private void rebuildHighlight(Optional<PerfilTrabalhador> melhor) {
        highlightContainer.removeAll();

        if (melhor.isPresent()) {
            highlightContainer.add(new HeroHighlightCard(melhor.get(), () -> getShell().showReview(melhor.get())), BorderLayout.CENTER);
        } else {
            JLabel empty = new JLabel("Ainda nao ha profissionais avaliados.");
            empty.setFont(AppTheme.subtitleFont(14));
            empty.setForeground(AppTheme.SECONDARY_TEXT);
            highlightContainer.add(empty, BorderLayout.CENTER);
        }

        highlightContainer.revalidate();
        highlightContainer.repaint();
    }

    private void rebuildFavorites() {
        rebuildFavorites(Collections.emptyList());
    }

    private void rebuildFavorites(List<PerfilTrabalhador> favoritos) {
        favoritesRow.removeAll();

        if (favoritos.isEmpty()) {
            JLabel empty = new JLabel("Seus favoritos vao aparecer aqui.");
            empty.setFont(AppTheme.subtitleFont(14));
            empty.setForeground(AppTheme.SECONDARY_TEXT);
            favoritesRow.add(empty);
        } else {
            for (int i = 0; i < favoritos.size(); i++) {
                PerfilTrabalhador perfil = favoritos.get(i);
                favoritesRow.add(new FavoriteProfileChip(perfil, () -> getShell().showReview(perfil)));
                if (i < favoritos.size() - 1) {
                    favoritesRow.add(Box.createRigidArea(new Dimension(12, 0)));
                }
            }
        }

        favoritesRow.revalidate();
        favoritesRow.repaint();
    }

    private void openSearch() {
        String termo = searchField.getText();
        if (termo == null || termo.trim().isEmpty()) {
            AppAlerts.showInfo(this, "Digite algo para buscar profissionais.");
            return;
        }

        getShell().showProfessionals(termo);
    }

    private void showLoadingState() {
        highlightContainer.removeAll();
        favoritesRow.removeAll();

        JLabel carregandoDestaque = new JLabel("Carregando destaque...");
        carregandoDestaque.setFont(AppTheme.subtitleFont(14));
        carregandoDestaque.setForeground(AppTheme.SECONDARY_TEXT);
        highlightContainer.add(carregandoDestaque, BorderLayout.CENTER);

        JLabel carregandoFavoritos = new JLabel("Carregando favoritos...");
        carregandoFavoritos.setFont(AppTheme.subtitleFont(14));
        carregandoFavoritos.setForeground(AppTheme.SECONDARY_TEXT);
        favoritesRow.add(carregandoFavoritos);

        highlightContainer.revalidate();
        highlightContainer.repaint();
        favoritesRow.revalidate();
        favoritesRow.repaint();
    }

    private void applySnapshot(HomeSnapshot snapshot) {
        rebuildHighlight(snapshot.melhorAvaliado());
        rebuildFavorites(snapshot.favoritos());
    }

    private record HomeSnapshot(
        Optional<PerfilTrabalhador> melhorAvaliado,
        List<PerfilTrabalhador> favoritos
    ) {
    }
}
