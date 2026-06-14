package indicafacil.ui;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/*
 * Cadastro seguindo a tela final.
 * Nome, telefone e CPF entram num dialog depois pra nao pesar a tela.
 */
public class CadastroScreen extends AbstractAuthPanel {
    private final JTextField emailField;
    private final JPasswordField senhaField;
    private final JPasswordField confirmarSenhaField;
    private final AppButton registerButton;

    public CadastroScreen(IndicaFacilFrame app) {
        super(app);
        this.emailField = AppTheme.createTextField("Email");
        this.senhaField = AppTheme.createPasswordField("Senha");
        this.confirmarSenhaField = AppTheme.createPasswordField("Confirmar senha");
        this.registerButton = new AppButton("Registrar");
        setPanelContent(createAuthLayout(
            "Cadastre-se",
            "Comece agora mesmo!",
            createFormContent(),
            null,
            app::showLogin
        ));
    }

    public void resetForm() {
        emailField.setText("");
        senhaField.setText("");
        confirmarSenhaField.setText("");
    }

    private Component createFormContent() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        registerButton.addActionListener(event -> handleRegister());

        AppButton googleButton = new AppButton("<html><span style='font-weight:bold;color:#ea4335;'>G</span>&nbsp;&nbsp;Cadastre-se com Google</html>", AppButton.Style.SECONDARY);
        googleButton.addActionListener(event -> AppAlerts.showInfo(this, "Cadastro social ainda nao foi implementado."));

        AppButton facebookButton = new AppButton("<html><span style='font-weight:bold;color:#1877f2;'>f</span>&nbsp;&nbsp;Cadastre-se com Facebook</html>", AppButton.Style.SECONDARY);
        facebookButton.addActionListener(event -> AppAlerts.showInfo(this, "Cadastro social ainda nao foi implementado."));

        Component entrarLink = createFooterContent();

        panel.add(emailField);
        panel.add(Box.createRigidArea(new Dimension(0, 14)));
        panel.add(senhaField);
        panel.add(Box.createRigidArea(new Dimension(0, 14)));
        panel.add(confirmarSenhaField);
        panel.add(Box.createRigidArea(new Dimension(0, 24)));
        panel.add(registerButton);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(entrarLink);
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
            "<html>J\u00e1 tem uma conta? <span style='color:#76c91f;font-weight:bold;'>Entre</span></html>",
            app::showLogin
        );
    }

    private void handleRegister() {
        AppDialogs.RegistrationDetails details = AppDialogs.collectRegistrationDetails(this);
        if (details == null) {
            return;
        }

        registerButton.setEnabled(false);

        UiTaskRunner.run(
            this,
            "criar conta",
            () -> app.getAutenticacaoService().criarConta(
                details.nome(),
                emailField.getText(),
                details.telefone(),
                details.cpf(),
                new String(senhaField.getPassword()),
                new String(confirmarSenhaField.getPassword())
            ),
            usuario -> app.setUsuarioLogado(usuario),
            () -> registerButton.setEnabled(true)
        );
    }

}
