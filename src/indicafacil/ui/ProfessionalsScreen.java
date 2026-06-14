package indicafacil.ui;

import indicafacil.model.CategoriaServico;
import indicafacil.model.PerfilTrabalhador;
import indicafacil.service.ordenacao.OrdenacaoPorAvaliacao;
import indicafacil.service.ordenacao.OrdenacaoPorNome;
import indicafacil.service.ordenacao.OrdenacaoPorPopularidade;
import indicafacil.service.ordenacao.OrdenacaoProfissional;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Tela da lista de profissionais.
 */
public class ProfessionalsScreen extends AbstractScreenPanel {
    private final JLabel titleLabel;
    private final JComboBox<OrdenacaoProfissional> orderCombo;
    private final JComboBox<DistanceFilter> distanceCombo;
    private final JComboBox<RatingFilter> ratingCombo;
    private final JPanel listContainer;

    private CategoriaServico currentCategory;
    private String currentSearch;

    public ProfessionalsScreen(IndicaFacilFrame app) {
        super(app);
        this.titleLabel = new JLabel("Profissionais");
        this.orderCombo = new JComboBox<>(new OrdenacaoProfissional[]{
            new OrdenacaoPorAvaliacao(),
            new OrdenacaoPorPopularidade(),
            new OrdenacaoPorNome()
        });
        this.distanceCombo = new JComboBox<>(DistanceFilter.values());
        this.ratingCombo = new JComboBox<>(RatingFilter.values());
        this.listContainer = new JPanel();

        listContainer.setOpaque(false);
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));

        AppTheme.styleComboBox(orderCombo);
        AppTheme.styleComboBox(distanceCombo);
        AppTheme.styleComboBox(ratingCombo);

        orderCombo.addActionListener(event -> rebuildList());
        distanceCombo.addActionListener(event -> rebuildList());
        ratingCombo.addActionListener(event -> rebuildList());

        setScreenContent(buildContent());
    }

    @Override
    public AppScreenKey getScreenKey() {
        return AppScreenKey.PROFESSIONALS;
    }

    @Override
    public boolean usesBottomNavigation() {
        return false;
    }

    @Override
    public void onScreenOpened(Object context) {
        currentCategory = context instanceof CategoriaServico ? (CategoriaServico) context : null;
        currentSearch = context instanceof String ? (String) context : null;
        titleLabel.setText(resolveTitle());
        rebuildList();
    }

    private Component buildContent() {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(18, 18, 24, 18));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JButton backButton = createScreenBackButton("Voltar", () -> getShell().showHome());

        titleLabel.setFont(AppTheme.titleFont(24));
        titleLabel.setForeground(AppTheme.PRIMARY_TEXT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        orderCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        distanceCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        ratingCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JPanel filters = new JPanel();
        filters.setOpaque(false);
        filters.setLayout(new BoxLayout(filters, BoxLayout.X_AXIS));
        filters.add(orderCombo);
        filters.add(Box.createRigidArea(new Dimension(10, 0)));
        filters.add(distanceCombo);
        filters.add(Box.createRigidArea(new Dimension(10, 0)));
        filters.add(ratingCombo);

        content.add(backButton);
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        content.add(titleLabel);
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(filters);
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(listContainer);

        return AppTheme.createScrollPane(content);
    }

    private void rebuildList() {
        long token = nextRequestToken();
        String searchSnapshot = currentSearch;
        CategoriaServico categorySnapshot = currentCategory;
        OrdenacaoProfissional ordenacaoSnapshot = (OrdenacaoProfissional) orderCombo.getSelectedItem();
        DistanceFilter distanceSnapshot = (DistanceFilter) distanceCombo.getSelectedItem();
        RatingFilter ratingSnapshot = (RatingFilter) ratingCombo.getSelectedItem();
        showLoading();

        UiTaskRunner.run(
            this,
            "carregar profissionais",
            () -> loadProfessionals(categorySnapshot, searchSnapshot, ordenacaoSnapshot, distanceSnapshot, ratingSnapshot),
            () -> isCurrentRequest(token)
                && currentCategory == categorySnapshot
                && ((currentSearch == null && searchSnapshot == null)
                    || (currentSearch != null && currentSearch.equals(searchSnapshot))),
            profissionais -> applyProfessionals(profissionais, distanceSnapshot)
        );
    }

    private List<PerfilTrabalhador> loadProfessionals(
        CategoriaServico categoria,
        String busca,
        OrdenacaoProfissional ordenacao,
        DistanceFilter distanceFilter,
        RatingFilter ratingFilter
    ) {
        List<PerfilTrabalhador> base;

        if (categoria != null) {
            base = app.getIndicaFacilService().buscarPorCategoria(categoria, ordenacao);
        } else if (busca != null && !busca.trim().isEmpty()) {
            base = app.getIndicaFacilService().buscarPorTexto(busca, ordenacao);
        } else {
            base = app.getIndicaFacilService().listarTrabalhadores(ordenacao);
        }

        List<PerfilTrabalhador> filtered = new ArrayList<>();

        for (PerfilTrabalhador perfil : base) {
            if (distanceFilter.accepts(perfil) && ratingFilter.accepts(perfil)) {
                filtered.add(perfil);
            }
        }

        return filtered;
    }

    private void showLoading() {
        listContainer.removeAll();
        JLabel loading = new JLabel("Carregando profissionais...");
        loading.setFont(AppTheme.subtitleFont(15));
        loading.setForeground(AppTheme.SECONDARY_TEXT);
        loading.setAlignmentX(Component.LEFT_ALIGNMENT);
        listContainer.add(loading);
        listContainer.revalidate();
        listContainer.repaint();
    }

    private void applyProfessionals(List<PerfilTrabalhador> profissionais, DistanceFilter distanceFilter) {
        listContainer.removeAll();

        for (PerfilTrabalhador perfil : profissionais) {
            String detalhe = distanceFilter.estimateKm(perfil) + " km";
            listContainer.add(new ProfessionalRowCard(perfil, detalhe, () -> getShell().showReview(perfil)));
            listContainer.add(Box.createRigidArea(new Dimension(0, 14)));
        }

        if (listContainer.getComponentCount() == 0) {
            JLabel empty = new JLabel("Nenhum profissional encontrado.");
            empty.setFont(AppTheme.subtitleFont(15));
            empty.setForeground(AppTheme.SECONDARY_TEXT);
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            listContainer.add(empty);
        }

        listContainer.revalidate();
        listContainer.repaint();
    }

    private String resolveTitle() {
        if (currentCategory != null) {
            return currentCategory.getDescricaoPlural();
        }
        if (currentSearch != null && !currentSearch.trim().isEmpty()) {
            return "Resultados";
        }
        return "Profissionais";
    }
}
