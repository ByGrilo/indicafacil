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
 * Essa tela cuida do cadastro visual da conta.
 * Ela coleta os dados, manda pro service e mostra o retorno pro usuario.
 */
public class CadastroPanel extends JPanel {
    private final IndicaFacilFrame app;
    private final JTextField nomeField;
    private final JTextField emailField;
    private final JTextField telefoneField;
    private final JTextField cpfField;
    private final JPasswordField senhaField;

    public CadastroPanel(IndicaFacilFrame app) {
        this.app = app;
        this.nomeField = UITheme.createTextField();
        this.emailField = UITheme.createTextField();
        this.telefoneField = UITheme.createTextField();
        this.cpfField = UITheme.createTextField();
        this.senhaField = UITheme.createPasswordField();

        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND);
        add(criarConteudo(), BorderLayout.CENTER);
    }

    private Component criarConteudo() {
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(36, 38, 36, 38));
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Cadastre-se");
        titulo.setFont(UITheme.titleFont(42));
        titulo.setForeground(UITheme.PRIMARY);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel("Crie sua conta e entre no IndicaFacil.");
        subtitulo.setFont(UITheme.subtitleFont(18));
        subtitulo.setForeground(UITheme.MUTED);
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        nomeField.setToolTipText("Nome completo");
        emailField.setToolTipText("E-mail");
        telefoneField.setToolTipText("Telefone");
        cpfField.setToolTipText("CPF");
        senhaField.setToolTipText("Senha");

        RoundedButton cadastrarButton = UITheme.createPrimaryButton("Criar conta");
        cadastrarButton.addActionListener(this::criarConta);

        RoundedButton googleButton = UITheme.createSecondaryButton("Cadastre-se com Google");
        googleButton.addActionListener(event -> SwingDialogs.showInfo(this, "Cadastro com Google ainda nao foi implementado."));

        RoundedButton facebookButton = UITheme.createSecondaryButton("Cadastre-se com Facebook");
        facebookButton.addActionListener(event -> SwingDialogs.showInfo(this, "Cadastro com Facebook ainda nao foi implementado."));

        javax.swing.JButton loginButton = UITheme.createLinkButton("Ja tem uma conta? Entre");
        loginButton.addActionListener(event -> app.showLogin());

        wrapper.add(Box.createVerticalGlue());
        wrapper.add(titulo);
        wrapper.add(Box.createRigidArea(new Dimension(0, 6)));
        wrapper.add(subtitulo);
        wrapper.add(Box.createRigidArea(new Dimension(0, 34)));
        wrapper.add(nomeField);
        wrapper.add(Box.createRigidArea(new Dimension(0, 14)));
        wrapper.add(emailField);
        wrapper.add(Box.createRigidArea(new Dimension(0, 14)));
        wrapper.add(telefoneField);
        wrapper.add(Box.createRigidArea(new Dimension(0, 14)));
        wrapper.add(cpfField);
        wrapper.add(Box.createRigidArea(new Dimension(0, 14)));
        wrapper.add(senhaField);
        wrapper.add(Box.createRigidArea(new Dimension(0, 24)));
        wrapper.add(cadastrarButton);
        wrapper.add(Box.createRigidArea(new Dimension(0, 28)));
        wrapper.add(googleButton);
        wrapper.add(Box.createRigidArea(new Dimension(0, 16)));
        wrapper.add(facebookButton);
        wrapper.add(Box.createVerticalGlue());
        wrapper.add(loginButton);

        return wrapper;
    }

    private void criarConta(ActionEvent event) {
        try {
            // A tela so pega os campos e deixa as validacoes com a regra de negocio.
            UsuarioConta usuarioCriado = app.getAutenticacaoService().criarConta(
                nomeField.getText(),
                emailField.getText(),
                telefoneField.getText(),
                cpfField.getText(),
                new String(senhaField.getPassword())
            );
            app.setUsuarioLogado(usuarioCriado);
            SwingDialogs.showInfo(this, "Conta criada com sucesso.");
        } catch (Exception exception) {
            SwingDialogs.showError(this, exception.getMessage());
        }
    }

    public void resetForm() {
        nomeField.setText("");
        emailField.setText("");
        telefoneField.setText("");
        cpfField.setText("");
        senhaField.setText("");
    }
}
