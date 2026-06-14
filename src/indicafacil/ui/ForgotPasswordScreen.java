package indicafacil.ui;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * Tela de recuperacao seguindo o visual final.
 */
public class ForgotPasswordScreen extends AbstractAuthPanel {
    private final JTextField emailField;
    private final AppButton sendButton;

    public ForgotPasswordScreen(IndicaFacilFrame app) {
        super(app);
        this.emailField = AppTheme.createTextField("Digite seu e-mail");
        this.sendButton = new AppButton("Enviar email");
        setPanelContent(createAuthLayout(
            "Esqueceu sua senha?",
            "Sem problemas, vamos te enviar as instru\u00e7\u00f5es de reset de senha via email",
            createFormContent(),
            null,
            app::showLogin
        ));
    }

    public void resetForm() {
        emailField.setText("");
    }

    private Component createFormContent() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel("\u25ce");
        icon.setFont(AppTheme.titleFont(24));
        icon.setForeground(AppTheme.PRIMARY_TEXT);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        sendButton.addActionListener(event -> handleReset());

        panel.add(icon);
        panel.add(Box.createRigidArea(new Dimension(0, 22)));
        panel.add(new JLabel("Email"));
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(emailField);
        panel.add(Box.createRigidArea(new Dimension(0, 24)));
        panel.add(sendButton);

        return panel;
    }

    private void handleReset() {
        sendButton.setEnabled(false);

        UiTaskRunner.run(
            this,
            "validar recuperacao de senha",
            () -> {
                app.getAutenticacaoService().solicitarRecuperacaoSenha(emailField.getText());
                return emailField.getText();
            },
            emailValidado -> {
                String novaSenha = AppDialogs.collectNewPassword(this);
                if (novaSenha == null) {
                    return;
                }

                sendButton.setEnabled(false);
                UiTaskRunner.run(
                    this,
                    "redefinir senha",
                    () -> {
                        app.getAutenticacaoService().redefinirSenhaPorEmail(emailValidado, novaSenha);
                        return null;
                    },
                    ignored -> {
                        AppAlerts.showInfo(this, "Senha redefinida com sucesso.");
                        app.showLogin();
                    },
                    () -> sendButton.setEnabled(true)
                );
            },
            () -> sendButton.setEnabled(true)
        );
    }

}
