package indicafacil.ui;

import indicafacil.model.CategoriaServico;
import indicafacil.model.PerfilTrabalhador;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * Essa tela faz a busca de profissionais.
 * Ela mistura texto digitado com filtro de categoria e mostra o resultado em cards.
 */
public class SearchPanel extends JPanel {
    private final IndicaFacilFrame app;
    private final JTextField buscaField;
    private final JComboBox<Object> categoriaCombo;
    private final JPanel resultadosContainer;

    public SearchPanel(IndicaFacilFrame app, MainMenuPanel menuPanel) {
        this.app = app;
        this.buscaField = UITheme.createTextField();
        this.categoriaCombo = new JComboBox<>();
        this.resultadosContainer = new JPanel();

        categoriaCombo.addItem("Todas as categorias");
        for (CategoriaServico categoria : CategoriaServico.values()) {
            categoriaCombo.addItem(categoria);
        }

        resultadosContainer.setOpaque(false);
        resultadosContainer.setLayout(new BoxLayout(resultadosContainer, BoxLayout.Y_AXIS));

        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);
        add(criarConteudo(), BorderLayout.CENTER);
    }

    private Component criarConteudo() {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(28, 22, 28, 22));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Buscar profissionais");
        title.setFont(UITheme.titleFont(30));
        title.setForeground(UITheme.PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        categoriaCombo.setFont(UITheme.bodyFont(14));
        categoriaCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        RoundedButton buscarButton = UITheme.createPrimaryButton("Pesquisar");
        buscarButton.addActionListener(event -> executeSearch());

        RoundedButton limparButton = UITheme.createSecondaryButton("Limpar filtros");
        limparButton.addActionListener(event -> {
            buscaField.setText("");
            categoriaCombo.setSelectedIndex(0);
            refreshData();
        });

        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(buscaField);
        content.add(Box.createRigidArea(new Dimension(0, 12)));
        content.add(categoriaCombo);
        content.add(Box.createRigidArea(new Dimension(0, 14)));
        content.add(buscarButton);
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        content.add(limparButton);
        content.add(Box.createRigidArea(new Dimension(0, 22)));
        content.add(resultadosContainer);

        return UITheme.createScrollPane(content);
    }

    public void setSearchTerm(String termo) {
        buscaField.setText(termo == null ? "" : termo);
    }

    public void executeSearch() {
        // Se nao digitar nada, ela mostra a lista toda. Se digitar, faz a busca por texto.
        List<PerfilTrabalhador> resultados = buscaField.getText().trim().isEmpty()
            ? app.getIndicaFacilService().listarTrabalhadores()
            : app.getIndicaFacilService().buscarPorTexto(buscaField.getText());

        Object selecionado = categoriaCombo.getSelectedItem();
        if (selecionado instanceof CategoriaServico) {
            CategoriaServico categoria = (CategoriaServico) selecionado;
            List<PerfilTrabalhador> filtrados = new ArrayList<>();
            // Esse filtro extra entra depois da busca principal.
            for (PerfilTrabalhador trabalhador : resultados) {
                if (trabalhador.getCategoria() == categoria) {
                    filtrados.add(trabalhador);
                }
            }
            resultados = filtrados;
        }

        rebuildResults(resultados);
    }

    public void refreshData() {
        executeSearch();
    }

    private void rebuildResults(List<PerfilTrabalhador> resultados) {
        resultadosContainer.removeAll();

        if (resultados.isEmpty()) {
            JLabel empty = new JLabel("Nenhum profissional encontrado.");
            empty.setFont(UITheme.subtitleFont(15));
            empty.setForeground(UITheme.MUTED);
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            resultadosContainer.add(empty);
        } else {
            for (PerfilTrabalhador perfil : resultados) {
                ProfessionalCardPanel card = new ProfessionalCardPanel(app, perfil, this::refreshData);
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                resultadosContainer.add(card);
                resultadosContainer.add(Box.createRigidArea(new Dimension(0, 14)));
            }
        }

        resultadosContainer.revalidate();
        resultadosContainer.repaint();
    }
}
