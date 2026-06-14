package indicafacil.ui;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/*
 * Tela de login baseada na versao final.
 */
public class LoginScreen extends AbstractAuthPanel {
    private final JTextField identificadorField;
    private final JPasswordField senhaField;
    private final AppButton loginButton;

    public LoginScreen(IndicaFacilFrame app) {
        super(app);
        this.identificadorField = AppTheme.createTextField("Email");
        this.senhaField = AppTheme.createPasswordField("Senha");
        this.loginButton = new AppButton("Login");
        setPanelContent(createAuthLayout(
            "Fa\u00e7a login",
            "Fa\u00e7a login com sua conta",
            createFormContent(),
            null
        ));
    }

    public void resetForm() {
        identificadorField.setText("");
        senhaField.setText("");
    }

    private Component createFormContent() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        loginButton.addActionListener(event -> handleLogin());

        javax.swing.JButton forgotButton = AppTheme.createLinkButton("Esqueceu a senha?");
        forgotButton.addActionListener(event -> app.showForgotPassword());
        forgotButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        AppButton googleButton = new AppButton("<html><span style='font-weight:bold;color:#ea4335;'>G</span>&nbsp;&nbsp;Entre com Google</html>", AppButton.Style.SECONDARY);
        googleButton.addActionListener(event -> AppAlerts.showInfo(this, "Login social ainda nao foi implementado."));

        AppButton facebookButton = new AppButton("<html><span style='font-weight:bold;color:#1877f2;'>f</span>&nbsp;&nbsp;Entre com Facebook</html>", AppButton.Style.SECONDARY);
        facebookButton.addActionListener(event -> AppAlerts.showInfo(this, "Login social ainda nao foi implementado."));

        Component cadastroLink = createFooterContent();

        panel.add(identificadorField);
        panel.add(Box.createRigidArea(new Dimension(0, 16)));
        panel.add(senhaField);
        panel.add(Box.createRigidArea(new Dimension(0, 24)));
        panel.add(loginButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(forgotButton);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(cadastroLink);
        panel.add(Box.createRigidArea(new Dimension(0, 26)));
        panel.add(createDivider());
        panel.add(Box.createRigidArea(new Dimension(0, 24)));
        panel.add(googleButton);
        panel.add(Box.createRigidArea(new Dimension(0, 14)));
        panel.add(facebookButton);

        return panel;
    }

    private Component createFooterContent() {
        return createFooterLink(
            "<html>N\u00e3o tem uma conta? <span style='color:#76c91f;font-weight:bold;'>Cadastre-se</span></html>",
            app::showCadastro
        );
    }

    private void handleLogin() {
        loginButton.setEnabled(false);

        // O login vai pra thread de fundo porque consulta o banco.
        UiTaskRunner.run(
            this,
            "fazer login",
            () -> {
                if (!app.getAutenticacaoService().possuiUsuariosCadastrados()) {
                    throw new IllegalArgumentException("Ainda nao existe conta cadastrada. Abra o cadastro primeiro.");
                }
                return app.getAutenticacaoService().autenticar(
                    identificadorField.getText(),
                    senhaField.getPassword()
                );
            },
            usuario -> app.setUsuarioLogado(usuario),
            () -> loginButton.setEnabled(true)
        );
    }

}
