package indicafacil.ui;

import indicafacil.model.PerfilTrabalhador;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Essa tela junta os dados privados da conta com o perfil profissional e os favoritos.
 * Ela ficou como um resumo mais completo do usuario logado.
 */
public class ProfilePanel extends JPanel {
    private final IndicaFacilFrame app;
    private final MainMenuPanel menuPanel;
    private final JLabel nomeLabel;
    private final JLabel emailLabel;
    private final JLabel telefoneLabel;
    private final JLabel cpfLabel;
    private final JLabel tipoContaLabel;
    private final JLabel perfilProfissionalLabel;
    private final JPanel favoritosContainer;

    public ProfilePanel(IndicaFacilFrame app, MainMenuPanel menuPanel) {
        this.app = app;
        this.menuPanel = menuPanel;
        this.nomeLabel = new JLabel();
        this.emailLabel = new JLabel();
        this.telefoneLabel = new JLabel();
        this.cpfLabel = new JLabel();
        this.tipoContaLabel = new JLabel();
        this.perfilProfissionalLabel = new JLabel();
        this.favoritosContainer = new JPanel();
        this.favoritosContainer.setOpaque(false);
        this.favoritosContainer.setLayout(new BoxLayout(favoritosContainer, BoxLayout.Y_AXIS));

        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);
        add(criarConteudo(), BorderLayout.CENTER);
    }

    private Component criarConteudo() {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(28, 22, 28, 22));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Perfil");
        title.setFont(UITheme.titleFont(30));
        title.setForeground(UITheme.PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundedPanel accountCard = new RoundedPanel();
        accountCard.setLayout(new BoxLayout(accountCard, BoxLayout.Y_AXIS));
        accountCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        accountCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        nomeLabel.setFont(UITheme.titleFont(28));
        emailLabel.setFont(UITheme.subtitleFont(15));
        telefoneLabel.setFont(UITheme.subtitleFont(15));
        cpfLabel.setFont(UITheme.subtitleFont(15));
        tipoContaLabel.setFont(UITheme.subtitleFont(15));
        perfilProfissionalLabel.setFont(UITheme.subtitleFont(15));

        RoundedButton editarContaButton = UITheme.createSecondaryButton("Editar conta");
        editarContaButton.addActionListener(event -> {
            SwingDialogs.showEditAccountDialog(this, app);
            refreshData();
        });

        RoundedButton perfilButton = UITheme.createPrimaryButton("Seja um prestador de servico");
        perfilButton.addActionListener(event -> {
            SwingDialogs.showCreateWorkerProfileDialog(this, app);
            refreshData();
        });

        RoundedButton favoritosButton = UITheme.createSecondaryButton("Ir para favoritos");
        favoritosButton.addActionListener(event -> menuPanel.showSection(MainMenuPanel.HOME));

        accountCard.add(nomeLabel);
        accountCard.add(Box.createRigidArea(new Dimension(0, 12)));
        accountCard.add(emailLabel);
        accountCard.add(Box.createRigidArea(new Dimension(0, 6)));
        accountCard.add(telefoneLabel);
        accountCard.add(Box.createRigidArea(new Dimension(0, 6)));
        accountCard.add(cpfLabel);
        accountCard.add(Box.createRigidArea(new Dimension(0, 6)));
        accountCard.add(tipoContaLabel);
        accountCard.add(Box.createRigidArea(new Dimension(0, 10)));
        accountCard.add(perfilProfissionalLabel);
        accountCard.add(Box.createRigidArea(new Dimension(0, 18)));
        accountCard.add(editarContaButton);
        accountCard.add(Box.createRigidArea(new Dimension(0, 10)));
        accountCard.add(perfilButton);
        accountCard.add(Box.createRigidArea(new Dimension(0, 10)));
        accountCard.add(favoritosButton);

        JLabel favoritosTitle = new JLabel("Seus favoritos");
        favoritosTitle.setFont(UITheme.titleFont(18));
        favoritosTitle.setForeground(UITheme.PRIMARY);
        favoritosTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(accountCard);
        content.add(Box.createRigidArea(new Dimension(0, 22)));
        content.add(favoritosTitle);
        content.add(Box.createRigidArea(new Dimension(0, 12)));
        content.add(favoritosContainer);

        return UITheme.createScrollPane(content);
    }

    public void refreshData() {
        if (app.getUsuarioLogado() == null) {
            return;
        }

        // Primeiro atualiza o usuario logado pra refletir qualquer edicao recente.
        app.atualizarUsuarioLogado();
        nomeLabel.setText(app.getUsuarioLogado().getNome());
        emailLabel.setText("E-mail: " + valorOuNaoInformado(app.getUsuarioLogado().getEmail()));
        telefoneLabel.setText("Telefone: " + valorOuNaoInformado(app.getUsuarioLogado().getTelefoneFormatado()));
        cpfLabel.setText("CPF: " + app.getUsuarioLogado().getCpfMascarado());
        tipoContaLabel.setText("Tipo de conta: " + getTipoConta());

        Optional<PerfilTrabalhador> perfil = app.getIndicaFacilService().buscarMeuPerfilTrabalhador(app.getUsuarioLogado().getId());
        if (perfil.isPresent()) {
            perfilProfissionalLabel.setText("Perfil profissional: "
                + perfil.get().getCategoria().getDescricao()
                + " | "
                + perfil.get().getEmpresa());
        } else {
            perfilProfissionalLabel.setText("Perfil profissional: ainda nao cadastrado");
        }

        rebuildFavoritos();
    }

    private void rebuildFavoritos() {
        favoritosContainer.removeAll();
        List<PerfilTrabalhador> favoritos = app.getIndicaFacilService().listarFavoritos(app.getUsuarioLogado().getId());

        // Reaproveitei o mesmo card da home pra manter a interface igual.
        if (favoritos.isEmpty()) {
            JLabel empty = new JLabel("Voce ainda nao favoritou profissionais.");
            empty.setFont(UITheme.subtitleFont(14));
            empty.setForeground(UITheme.MUTED);
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            favoritosContainer.add(empty);
        } else {
            for (PerfilTrabalhador perfil : favoritos) {
                ProfessionalCardPanel card = new ProfessionalCardPanel(app, perfil, this::refreshData);
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                favoritosContainer.add(card);
                favoritosContainer.add(Box.createRigidArea(new Dimension(0, 14)));
            }
        }

        favoritosContainer.revalidate();
        favoritosContainer.repaint();
    }

    private String getTipoConta() {
        return app.getIndicaFacilService().usuarioPossuiPerfilTrabalhador(app.getUsuarioLogado().getId())
            ? "Prestador de servico"
            : "Cliente";
    }

    private String valorOuNaoInformado(String valor) {
        return valor == null || valor.trim().isEmpty() ? "Nao informado" : valor;
    }
}
