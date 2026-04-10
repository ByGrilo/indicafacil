package indicafacil.ui;

import indicafacil.model.AvaliacaoPublica;
import indicafacil.model.CategoriaServico;
import indicafacil.model.DetalheTrabalhador;
import indicafacil.model.PerfilTrabalhador;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * Essa classe junta os dialogos menores do app.
 * Ela ajuda a nao espalhar JOptionPane e formularios pequenos por todas as telas.
 */
public final class SwingDialogs {
    private SwingDialogs() {
    }

    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "IndicaFacil", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "IndicaFacil", JOptionPane.ERROR_MESSAGE);
    }

    public static void showForgotPasswordDialog(Component parent, IndicaFacilFrame app) {
        JTextField emailField = UITheme.createTextField();
        JPasswordField senhaField = UITheme.createPasswordField();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("E-mail"));
        panel.add(emailField);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(new JLabel("Nova senha"));
        panel.add(senhaField);

        int option = JOptionPane.showConfirmDialog(parent, panel, "Esqueceu sua senha?", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            // Aqui eu simulo a recuperacao dentro do proprio app, sem e-mail real.
            app.getAutenticacaoService().solicitarRecuperacaoSenha(emailField.getText());
            app.getAutenticacaoService().redefinirSenhaPorEmail(emailField.getText(), new String(senhaField.getPassword()));
            showInfo(parent, "Senha redefinida com sucesso.");
        } catch (Exception exception) {
            showError(parent, exception.getMessage());
        }
    }

    public static void showEditAccountDialog(Component parent, IndicaFacilFrame app) {
        JTextField nomeField = UITheme.createTextField();
        JTextField emailField = UITheme.createTextField();
        JTextField telefoneField = UITheme.createTextField();

        nomeField.setText(app.getUsuarioLogado().getNome());
        emailField.setText(app.getUsuarioLogado().getEmail());
        telefoneField.setText(app.getUsuarioLogado().getTelefoneFormatado());

        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 10));
        panel.add(new JLabel("Nome"));
        panel.add(nomeField);
        panel.add(new JLabel("E-mail"));
        panel.add(emailField);
        panel.add(new JLabel("Telefone"));
        panel.add(telefoneField);

        int option = JOptionPane.showConfirmDialog(parent, panel, "Editar conta", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            app.setUsuarioLogado(app.getAutenticacaoService().atualizarDadosDaConta(
                app.getUsuarioLogado().getId(),
                nomeField.getText(),
                emailField.getText(),
                telefoneField.getText()
            ));
            showInfo(parent, "Dados atualizados com sucesso.");
        } catch (Exception exception) {
            showError(parent, exception.getMessage());
        }
    }

    public static void showCreateWorkerProfileDialog(Component parent, IndicaFacilFrame app) {
        if (app.getIndicaFacilService().usuarioPossuiPerfilTrabalhador(app.getUsuarioLogado().getId())) {
            showInfo(parent, "Sua conta ja possui um perfil de trabalhador.");
            return;
        }

        JComboBox<CategoriaServico> categoriaCombo = new JComboBox<>(CategoriaServico.values());
        JTextField empresaField = UITheme.createTextField();
        JTextArea descricaoArea = new JTextArea(5, 20);
        descricaoArea.setLineWrap(true);
        descricaoArea.setWrapStyleWord(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Categoria"));
        panel.add(categoriaCombo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(new JLabel("Empresa ou nome profissional"));
        panel.add(empresaField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(new JLabel("Descricao do servico"));
        panel.add(new JScrollPane(descricaoArea));

        int option = JOptionPane.showConfirmDialog(parent, panel, "Perfil de prestador", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            app.getIndicaFacilService().cadastrarMeuPerfilTrabalhador(
                app.getUsuarioLogado(),
                (CategoriaServico) categoriaCombo.getSelectedItem(),
                descricaoArea.getText(),
                empresaField.getText()
            );
            app.atualizarUsuarioLogado();
            showInfo(parent, "Perfil de prestador criado com sucesso.");
        } catch (Exception exception) {
            showError(parent, exception.getMessage());
        }
    }

    public static void showEvaluationDialog(Component parent, IndicaFacilFrame app, PerfilTrabalhador trabalhador, Runnable afterSuccess) {
        JComboBox<Integer> notaCombo = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        JCheckBox anonimaCheck = new JCheckBox("Publicar como anonima");
        JTextArea comentarioArea = new JTextArea(5, 20);
        comentarioArea.setLineWrap(true);
        comentarioArea.setWrapStyleWord(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Profissional: " + trabalhador.getNome()));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(new JLabel("Nota"));
        panel.add(notaCombo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(anonimaCheck);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(new JLabel("Comentario"));
        panel.add(new JScrollPane(comentarioArea));

        int option = JOptionPane.showConfirmDialog(parent, panel, "Avaliar profissional", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            app.getIndicaFacilService().registrarAvaliacao(
                app.getUsuarioLogado(),
                trabalhador.getId(),
                (Integer) notaCombo.getSelectedItem(),
                comentarioArea.getText(),
                anonimaCheck.isSelected()
            );
            if (afterSuccess != null) {
                afterSuccess.run();
            }
            showInfo(parent, "Avaliacao registrada com sucesso.");
        } catch (Exception exception) {
            showError(parent, exception.getMessage());
        }
    }

    public static void showWorkerDetailDialog(Component parent, IndicaFacilFrame app, PerfilTrabalhador trabalhador, Runnable afterChange) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Detalhes do profissional");
        dialog.setModal(true);
        dialog.setSize(420, 640);
        dialog.setLocationRelativeTo(parent);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BACKGROUND);
        root.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        RoundedPanel card = new RoundedPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        DetalheTrabalhador detalhe = app.getIndicaFacilService().detalharTrabalhador(trabalhador.getId());

        JLabel nome = new JLabel(detalhe.getPerfil().getNome());
        nome.setFont(UITheme.titleFont(26));
        JLabel categoria = new JLabel(detalhe.getPerfil().getCategoria().getDescricao());
        categoria.setFont(UITheme.subtitleFont(18));
        JLabel empresa = new JLabel(detalhe.getPerfil().getEmpresa());
        empresa.setFont(UITheme.subtitleFont(16));
        JLabel media = new JLabel(String.format("%.1f estrela(s)", detalhe.getPerfil().getMediaAvaliacoes()));
        media.setFont(UITheme.labelFont(16));

        JTextArea descricaoArea = new JTextArea(detalhe.getPerfil().getDescricao());
        descricaoArea.setEditable(false);
        descricaoArea.setLineWrap(true);
        descricaoArea.setWrapStyleWord(true);
        descricaoArea.setBackground(UITheme.SURFACE);
        descricaoArea.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        card.add(nome);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(categoria);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(empresa);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(media);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(descricaoArea);
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        card.add(new JLabel("Avaliacoes"));
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        if (detalhe.getAvaliacoes().isEmpty()) {
            JLabel empty = new JLabel("Sem avaliacoes no momento.");
            empty.setForeground(UITheme.MUTED);
            card.add(empty);
        } else {
            for (AvaliacaoPublica avaliacao : detalhe.getAvaliacoes()) {
                JLabel avaliacaoLabel = new JLabel("<html>" + avaliacao.getResumo() + "</html>");
                avaliacaoLabel.setFont(UITheme.subtitleFont(13));
                avaliacaoLabel.setForeground(UITheme.PRIMARY);
                card.add(avaliacaoLabel);
                card.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        JPanel buttons = new JPanel(new GridLayout(1, 3, 10, 0));
        buttons.setOpaque(false);
        RoundedButton avaliarButton = UITheme.createPrimaryButton("Avaliar");
        avaliarButton.addActionListener(event -> {
            showEvaluationDialog(dialog, app, detalhe.getPerfil(), afterChange);
            dialog.dispose();
        });

        RoundedButton favoritoButton = UITheme.createSecondaryButton(
            app.getIndicaFacilService().trabalhadorEstaNosFavoritos(app.getUsuarioLogado().getId(), detalhe.getPerfil().getId())
                ? "Desfavoritar"
                : "Favoritar"
        );
        favoritoButton.addActionListener(event -> {
            try {
                // Se mudar favorito aqui, eu aviso a tela chamadora pra ela se redesenhar.
                boolean favoritado = app.getIndicaFacilService().alternarFavorito(app.getUsuarioLogado(), detalhe.getPerfil().getId());
                if (afterChange != null) {
                    afterChange.run();
                }
                showInfo(dialog, favoritado ? "Profissional adicionado aos favoritos." : "Profissional removido dos favoritos.");
                dialog.dispose();
            } catch (Exception exception) {
                showError(dialog, exception.getMessage());
            }
        });

        RoundedButton fecharButton = UITheme.createSecondaryButton("Fechar");
        fecharButton.addActionListener(event -> dialog.dispose());

        buttons.add(avaliarButton);
        buttons.add(favoritoButton);
        buttons.add(fecharButton);

        root.add(UITheme.createScrollPane(card), BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setVisible(true);
    }
}
