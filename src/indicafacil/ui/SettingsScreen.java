package indicafacil.ui;

import indicafacil.model.PerfilTrabalhador;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Tela principal da conta/configuracoes.
 */
public class SettingsScreen extends AbstractScreenPanel {
    private final JLabel nomeLabel;
    private final JLabel emailLabel;
    private final JLabel telefoneLabel;
    private final JLabel perfilLabel;
    private final JPanel avatarHolder;

    public SettingsScreen(IndicaFacilFrame app) {
        super(app);
        this.nomeLabel = new JLabel();
        this.emailLabel = new JLabel();
        this.telefoneLabel = new JLabel();
        this.perfilLabel = new JLabel();
        this.avatarHolder = new JPanel();
        avatarHolder.setOpaque(false);
        setScreenContent(buildContent());
    }

    @Override
    public AppScreenKey getScreenKey() {
        return AppScreenKey.SETTINGS;
    }

    @Override
    public void refreshData() {
        if (app.getUsuarioLogado() == null) {
            return;
        }

        long token = nextRequestToken();
        long usuarioId = app.getUsuarioLogado().getId();

        // Essa carga vai pro fundo porque busca dados no banco toda vez que a tela abre.
        UiTaskRunner.run(
            this,
            "carregar configuracoes",
            () -> {
                Optional<indicafacil.auth.model.UsuarioConta> usuarioAtualizado = app.getAutenticacaoService().buscarPorId(usuarioId);
                Optional<PerfilTrabalhador> perfil = app.getIndicaFacilService().buscarMeuPerfilTrabalhador(usuarioId);
                return new SettingsSnapshot(usuarioAtualizado.orElseThrow(() ->
                    new IllegalArgumentException("Conta nao encontrada.")), perfil);
            },
            () -> isCurrentRequest(token) && app.getUsuarioLogado() != null && app.getUsuarioLogado().getId() == usuarioId,
            snapshot -> applySnapshot(snapshot)
        );
    }

    private Component buildContent() {
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Configura\u00e7\u00f5es");
        title.setFont(AppTheme.titleFont(26));
        title.setForeground(AppTheme.PRIMARY_TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel avatarWrapper = new JPanel();
        avatarWrapper.setOpaque(false);
        avatarWrapper.setLayout(new BoxLayout(avatarWrapper, BoxLayout.Y_AXIS));

        avatarHolder.setLayout(new BoxLayout(avatarHolder, BoxLayout.Y_AXIS));
        avatarHolder.setAlignmentX(Component.CENTER_ALIGNMENT);

        AppButton editPhotoButton = new AppButton("\u270e", AppButton.Style.SECONDARY, 38);
        editPhotoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        editPhotoButton.addActionListener(event -> AppAlerts.showInfo(this, "Edicao de foto ainda nao foi implementada."));

        AppCardPanel card = new AppCardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(22, 20, 22, 20));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel infoTitle = new JLabel("Informa\u00e7\u00f5es pessoais");
        infoTitle.setFont(AppTheme.labelFont(20));
        infoTitle.setForeground(AppTheme.PRIMARY_TEXT);

        AppButton editButton = new AppButton("Editar", AppButton.Style.SECONDARY, 36);
        editButton.setPreferredSize(new Dimension(92, 36));
        editButton.addActionListener(event -> handleEditAccount());

        header.add(infoTitle, BorderLayout.WEST);
        header.add(editButton, BorderLayout.EAST);

        nomeLabel.setFont(AppTheme.subtitleFont(15));
        nomeLabel.setForeground(AppTheme.PRIMARY_TEXT);
        emailLabel.setFont(AppTheme.subtitleFont(15));
        emailLabel.setForeground(AppTheme.PRIMARY_TEXT);
        telefoneLabel.setFont(AppTheme.subtitleFont(15));
        telefoneLabel.setForeground(AppTheme.PRIMARY_TEXT);
        perfilLabel.setFont(AppTheme.subtitleFont(14));
        perfilLabel.setForeground(AppTheme.SECONDARY_TEXT);

        card.add(header);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(buildInfoLine("Nome", nomeLabel));
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(buildInfoLine("E-mail", emailLabel));
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(buildInfoLine("Telefone", telefoneLabel));
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(buildInfoLine("Tipo", perfilLabel));
        card.add(Box.createRigidArea(new Dimension(0, 18)));
        card.add(new javax.swing.JSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        card.add(new SettingsOptionRow("\u263d", "Apar\u00eancia e acessibilidade", AppTheme.getAppearanceLabel(), AppTheme.PRIMARY_TEXT, () -> getShell().showAccessibility()));
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(new SettingsOptionRow("\u21aa", "Sair", "", AppTheme.DANGER, app::logout, false));
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(new SettingsOptionRow("\u2692", "Seja um prestador de servi\u00e7o.", "Cadastre seu perfil profissional", AppTheme.PRIMARY_TEXT, this::handleCreateWorkerProfile));

        avatarWrapper.add(avatarHolder);
        avatarWrapper.add(Box.createRigidArea(new Dimension(0, 8)));
        avatarWrapper.add(editPhotoButton);

        content.add(title);
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(avatarWrapper);
        content.add(Box.createRigidArea(new Dimension(0, 18)));
        content.add(card);

        return AppTheme.createScrollPane(content);
    }

    private void rebuildAvatar() {
        avatarHolder.removeAll();

        AvatarView avatar = new AvatarView(app.getUsuarioLogado().getNome(), 124, AppTheme.ACCENT_DARK);
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarHolder.add(avatar);
        avatarHolder.revalidate();
        avatarHolder.repaint();
    }

    private JPanel buildInfoLine(String label, JLabel valueLabel) {
        JPanel line = new JPanel(new BorderLayout(10, 0));
        line.setOpaque(false);

        JLabel icon = new JLabel(resolveInfoIcon(label));
        icon.setFont(AppTheme.labelFont(16));
        icon.setForeground(AppTheme.PRIMARY_TEXT);

        JPanel texts = new JPanel();
        texts.setOpaque(false);
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(label);
        title.setFont(AppTheme.subtitleFont(12));
        title.setForeground(AppTheme.SECONDARY_TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        texts.add(title);
        texts.add(Box.createRigidArea(new Dimension(0, 3)));
        texts.add(valueLabel);

        line.add(icon, BorderLayout.WEST);
        line.add(texts, BorderLayout.CENTER);
        return line;
    }

    private String resolveInfoIcon(String label) {
        if ("Nome".equals(label)) {
            return "\u263a";
        }
        if ("E-mail".equals(label)) {
            return "\u2709";
        }
        if ("Telefone".equals(label)) {
            return "\u260e";
        }
        return "\u25cf";
    }

    private void applySnapshot(SettingsSnapshot snapshot) {
        app.replaceUsuarioLogadoSilently(snapshot.usuario());
        nomeLabel.setText(snapshot.usuario().getNome());
        emailLabel.setText(snapshot.usuario().getEmail());
        telefoneLabel.setText(snapshot.usuario().getTelefoneFormatado());
        perfilLabel.setText(snapshot.perfil().isPresent()
            ? "Prestador: " + snapshot.perfil().get().getCategoria().getDescricao()
            : "Conta comum");
        rebuildAvatar();
    }

    private void handleEditAccount() {
        if (app.getUsuarioLogado() == null) {
            return;
        }

        AppDialogs.AccountEditDetails details = AppDialogs.collectEditAccountDetails(this, app.getUsuarioLogado());
        if (details == null) {
            return;
        }

        long usuarioId = app.getUsuarioLogado().getId();
        UiTaskRunner.run(
            this,
            "atualizar conta",
            () -> app.getAutenticacaoService().atualizarDadosDaConta(
                usuarioId,
                details.nome(),
                details.email(),
                details.telefone()
            ),
            usuarioAtualizado -> {
                app.replaceUsuarioLogadoSilently(usuarioAtualizado);
                AppAlerts.showInfo(this, "Dados atualizados com sucesso.");
                refreshData();
            }
        );
    }

    private void handleCreateWorkerProfile() {
        if (app.getUsuarioLogado() == null) {
            return;
        }

        indicafacil.auth.model.UsuarioConta usuarioSnapshot = app.getUsuarioLogado();
        AppDialogs.WorkerProfileDetails details = AppDialogs.collectWorkerProfileDetails(this);
        if (details == null) {
            return;
        }

        UiTaskRunner.run(
            this,
            "criar perfil profissional",
            () -> {
                app.getIndicaFacilService().cadastrarMeuPerfilTrabalhador(
                    usuarioSnapshot,
                    details.categoria(),
                    details.descricao(),
                    details.empresa()
                );
                return null;
            },
            ignored -> {
                AppAlerts.showInfo(this, "Perfil profissional criado com sucesso.");
                refreshData();
            }
        );
    }

    private record SettingsSnapshot(
        indicafacil.auth.model.UsuarioConta usuario,
        Optional<PerfilTrabalhador> perfil
    ) {
    }
}
