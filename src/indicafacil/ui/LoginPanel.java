package indicafacil.ui;

import indicafacil.auth.model.UsuarioConta;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/*
 * Essa tela faz o login visual do usuario.
 * Ela aceita e-mail ou CPF e manda a validacao pro service.
 */
public class LoginPanel extends JPanel {
    private final IndicaFacilFrame app;
    private final JTextField identificadorField;
    private final JPasswordField senhaField;

    public LoginPanel(IndicaFacilFrame app) {
        this.app = app;
        this.identificadorField = UITheme.createTextField();
        this.senhaField = UITheme.createPasswordField();

        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);
        add(criarConteudo(), BorderLayout.CENTER);
    }

    private Component criarConteudo() {
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(48, 38, 48, 38));
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Entre");
        titulo.setFont(UITheme.titleFont(44));
        titulo.setForeground(UITheme.PRIMARY);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel("Acesse sua conta para encontrar profissionais.");
        subtitulo.setFont(UITheme.subtitleFont(18));
        subtitulo.setForeground(UITheme.MUTED);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        identificadorField.setToolTipText("E-mail ou CPF");
        senhaField.setToolTipText("Senha");

        RoundedButton entrarButton = UITheme.createPrimaryButton("Entrar");
        entrarButton.addActionListener(this::fazerLogin);

        RoundedButton googleButton = UITheme.createSecondaryButton("Entre com Google");
        googleButton.addActionListener(event -> SwingDialogs.showInfo(this, "Login com Google ainda nao foi implementado."));

        RoundedButton facebookButton = UITheme.createSecondaryButton("Entre com Facebook");
        facebookButton.addActionListener(event -> SwingDialogs.showInfo(this, "Login com Facebook ainda nao foi implementado."));

        javax.swing.JButton esquecerSenhaButton = UITheme.createLinkButton("Esqueceu sua senha?");
        esquecerSenhaButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        esquecerSenhaButton.addActionListener(event -> SwingDialogs.showForgotPasswordDialog(this, app));

        javax.swing.JButton cadastroButton = UITheme.createLinkButton("Nao tem uma conta? Cadastre-se");
        cadastroButton.addActionListener(event -> app.showCadastro());

        wrapper.add(Box.createVerticalGlue());
        wrapper.add(titulo);
        wrapper.add(Box.createRigidArea(new Dimension(0, 6)));
        wrapper.add(subtitulo);
        wrapper.add(Box.createRigidArea(new Dimension(0, 40)));
        wrapper.add(identificadorField);
        wrapper.add(Box.createRigidArea(new Dimension(0, 18)));
        wrapper.add(senhaField);
        wrapper.add(Box.createRigidArea(new Dimension(0, 12)));
        wrapper.add(esquecerSenhaButton);
        wrapper.add(Box.createRigidArea(new Dimension(0, 22)));
        wrapper.add(entrarButton);
        wrapper.add(Box.createRigidArea(new Dimension(0, 34)));
        wrapper.add(googleButton);
        wrapper.add(Box.createRigidArea(new Dimension(0, 16)));
        wrapper.add(facebookButton);
        wrapper.add(Box.createVerticalGlue());
        wrapper.add(cadastroButton);

        return wrapper;
    }

    private void fazerLogin(ActionEvent event) {
        try {
            // A tela nao valida regra pesada, ela so envia os dados digitados.
            UsuarioConta usuario = app.getAutenticacaoService().autenticar(
                identificadorField.getText(),
                new String(senhaField.getPassword())
            );
            app.setUsuarioLogado(usuario);
            SwingDialogs.showInfo(this, "Login realizado com sucesso.");
        } catch (Exception exception) {
            SwingDialogs.showError(this, exception.getMessage());
        }
    }

    public void resetForm() {
        identificadorField.setText("");
        senhaField.setText("");
    }
}
