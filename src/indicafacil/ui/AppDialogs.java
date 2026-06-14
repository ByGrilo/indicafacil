package indicafacil.ui;

import indicafacil.auth.model.UsuarioConta;
import indicafacil.model.CategoriaServico;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * Dialogos pequenos de apoio. Mantive separado porque ainda sao a forma mais simples pra essas acoes secundarias.
 */
public final class AppDialogs {
    public record RegistrationDetails(String nome, String telefone, String cpf) {
    }

    public record AccountEditDetails(String nome, String email, String telefone) {
    }

    public record WorkerProfileDetails(CategoriaServico categoria, String empresa, String descricao) {
    }

    private AppDialogs() {
    }

    public static RegistrationDetails collectRegistrationDetails(java.awt.Component parent) {
        JTextField nomeField = AppTheme.createTextField();
        JTextField telefoneField = AppTheme.createTextField();
        JTextField cpfField = AppTheme.createTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 10));
        styleDialogPanel(panel);
        panel.add(createDialogLabel("Nome completo"));
        panel.add(nomeField);
        panel.add(createDialogLabel("Telefone"));
        panel.add(telefoneField);
        panel.add(createDialogLabel("CPF"));
        panel.add(cpfField);

        int option = JOptionPane.showConfirmDialog(parent, panel, "Complete seu cadastro", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return null;
        }

        return new RegistrationDetails(nomeField.getText(), telefoneField.getText(), cpfField.getText());
    }

    public static String collectNewPassword(java.awt.Component parent) {
        JTextField novaSenhaField = AppTheme.createTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 10));
        styleDialogPanel(panel);
        panel.add(createDialogLabel("Nova senha"));
        panel.add(novaSenhaField);

        int option = JOptionPane.showConfirmDialog(parent, panel, "Redefinir senha", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return null;
        }

        return novaSenhaField.getText();
    }

    public static AccountEditDetails collectEditAccountDetails(java.awt.Component parent, UsuarioConta usuario) {
        JTextField nomeField = AppTheme.createTextField();
        JTextField emailField = AppTheme.createTextField();
        JTextField telefoneField = AppTheme.createTextField();

        nomeField.setText(usuario.getNome());
        emailField.setText(usuario.getEmail());
        telefoneField.setText(usuario.getTelefoneFormatado());

        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 10));
        styleDialogPanel(panel);
        panel.add(createDialogLabel("Nome"));
        panel.add(nomeField);
        panel.add(createDialogLabel("E-mail"));
        panel.add(emailField);
        panel.add(createDialogLabel("Telefone"));
        panel.add(telefoneField);

        int option = JOptionPane.showConfirmDialog(parent, panel, "Editar conta", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return null;
        }

        return new AccountEditDetails(nomeField.getText(), emailField.getText(), telefoneField.getText());
    }

    public static WorkerProfileDetails collectWorkerProfileDetails(java.awt.Component parent) {
        JComboBox<CategoriaServico> categoriaCombo = new JComboBox<>(CategoriaServico.values());
        AppTheme.styleComboBox(categoriaCombo);
        JTextField empresaField = AppTheme.createTextField();
        JTextArea descricaoArea = new JTextArea(5, 20);
        descricaoArea.setLineWrap(true);
        descricaoArea.setWrapStyleWord(true);
        descricaoArea.setBackground(AppTheme.SURFACE);
        descricaoArea.setForeground(AppTheme.PRIMARY_TEXT);
        descricaoArea.setCaretColor(AppTheme.PRIMARY_TEXT);
        descricaoArea.setBorder(AppTheme.createInputBorder());
        javax.swing.JScrollPane descricaoScroll = AppTheme.createScrollPane(descricaoArea);
        descricaoScroll.setPreferredSize(new Dimension(240, 120));

        JPanel panel = new JPanel();
        styleDialogPanel(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createDialogLabel("Categoria"));
        panel.add(categoriaCombo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createDialogLabel("Empresa ou nome profissional"));
        panel.add(empresaField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createDialogLabel("Descricao do servico"));
        panel.add(descricaoScroll);

        int option = JOptionPane.showConfirmDialog(parent, panel, "Novo perfil profissional", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return null;
        }

        return new WorkerProfileDetails(
            (CategoriaServico) categoriaCombo.getSelectedItem(),
            empresaField.getText(),
            descricaoArea.getText()
        );
    }

    private static void styleDialogPanel(JPanel panel) {
        panel.setBackground(AppTheme.BACKGROUND);
    }

    private static JLabel createDialogLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(AppTheme.PRIMARY_TEXT);
        label.setFont(AppTheme.labelFont(13));
        return label;
    }
}
