package indicafacil.ui;

import indicafacil.auth.model.UsuarioConta;
import indicafacil.auth.persistence.DatabaseManager;
import indicafacil.auth.persistence.UsuarioRepository;
import indicafacil.auth.service.AutenticacaoService;
import indicafacil.service.IndicaFacilService;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * Essa janela e o container principal do app.
 * Ela liga servicos, usuario logado e troca de telas sem depender do console.
 */
public class IndicaFacilFrame extends JFrame {
    private static final String LOGIN = "LOGIN";
    private static final String CADASTRO = "CADASTRO";
    private static final String APP = "APP";

    private final AutenticacaoService autenticacaoService;
    private final IndicaFacilService indicaFacilService;
    private final CardLayout rootLayout;
    private final JPanel rootPanel;
    private final LoginPanel loginPanel;
    private final CadastroPanel cadastroPanel;
    private final MainMenuPanel mainMenuPanel;

    private UsuarioConta usuarioLogado;

    public IndicaFacilFrame() {
        // Aqui eu junto banco, services e telas numa janela so.
        DatabaseManager databaseManager = new DatabaseManager();
        UsuarioRepository usuarioRepository = new UsuarioRepository(databaseManager);
        this.autenticacaoService = new AutenticacaoService(usuarioRepository);
        this.indicaFacilService = new IndicaFacilService(databaseManager);
        this.rootLayout = new CardLayout();
        this.rootPanel = new JPanel(rootLayout);
        this.loginPanel = new LoginPanel(this);
        this.cadastroPanel = new CadastroPanel(this);
        this.mainMenuPanel = new MainMenuPanel(this);

        configurarJanela();
        configurarTelas();
        mostrarTelaInicial();
    }

    private void configurarJanela() {
        setTitle("IndicaFacil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(430, 860);
        setLocationRelativeTo(null);
        setMinimumSize(getSize());
        setContentPane(rootPanel);
    }

    private void configurarTelas() {
        rootPanel.add(loginPanel, LOGIN);
        rootPanel.add(cadastroPanel, CADASTRO);
        rootPanel.add(mainMenuPanel, APP);
    }

    private void mostrarTelaInicial() {
        if (autenticacaoService.possuiUsuariosCadastrados()) {
            showLogin();
        } else {
            showCadastro();
        }
    }

    public AutenticacaoService getAutenticacaoService() {
        return autenticacaoService;
    }

    public IndicaFacilService getIndicaFacilService() {
        return indicaFacilService;
    }

    public UsuarioConta getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(UsuarioConta usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        if (usuarioLogado != null) {
            // Quando loga, ja manda pra home e recarrega tudo.
            mainMenuPanel.showHome();
            mainMenuPanel.refreshAll();
            rootLayout.show(rootPanel, APP);
        } else {
            showLogin();
        }
    }

    public void atualizarUsuarioLogado() {
        if (usuarioLogado == null) {
            return;
        }

        autenticacaoService.buscarPorId(usuarioLogado.getId()).ifPresent(usuarioAtualizado -> this.usuarioLogado = usuarioAtualizado);
    }

    public void showLogin() {
        loginPanel.resetForm();
        rootLayout.show(rootPanel, LOGIN);
    }

    public void showCadastro() {
        cadastroPanel.resetForm();
        rootLayout.show(rootPanel, CADASTRO);
    }

    public void logout() {
        this.usuarioLogado = null;
        showLogin();
    }
}
