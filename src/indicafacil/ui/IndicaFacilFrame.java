package indicafacil.ui;

import indicafacil.app.IndicaFacilApplicationContext;
import indicafacil.auth.model.UsuarioConta;
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
    private static final String FORGOT = "FORGOT";
    private static final String APP = "APP";

    private final IndicaFacilApplicationContext applicationContext;
    private final AutenticacaoService autenticacaoService;
    private final IndicaFacilService indicaFacilService;
    private final CardLayout rootLayout;
    private final JPanel rootPanel;
    private final LoginScreen loginScreen;
    private final CadastroScreen cadastroScreen;
    private final ForgotPasswordScreen forgotPasswordScreen;
    private final AppShellPanel appShellPanel;

    // Deixei volatile porque esse dado pode ser lido pelas tarefas em segundo plano.
    private volatile UsuarioConta usuarioLogado;

    public IndicaFacilFrame() {
        this(new IndicaFacilApplicationContext());
    }

    public IndicaFacilFrame(IndicaFacilApplicationContext applicationContext) {
        // Aqui eu junto banco, services e telas numa janela so.
        this.applicationContext = applicationContext;
        this.autenticacaoService = applicationContext.getAutenticacaoService();
        this.indicaFacilService = applicationContext.getIndicaFacilService();
        this.rootLayout = new CardLayout();
        this.rootPanel = new JPanel(rootLayout);
        this.loginScreen = new LoginScreen(this);
        this.cadastroScreen = new CadastroScreen(this);
        this.forgotPasswordScreen = new ForgotPasswordScreen(this);
        this.appShellPanel = new AppShellPanel(this);

        configurarJanela();
        configurarTelas();
        mostrarTelaInicial();
    }

    private void configurarJanela() {
        setTitle("IndicaFacil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 860);
        setLocationRelativeTo(null);
        setMinimumSize(getSize());
        rootPanel.setBackground(AppTheme.BACKGROUND);
        setContentPane(rootPanel);
    }

    private void configurarTelas() {
        rootPanel.add(loginScreen, LOGIN);
        rootPanel.add(cadastroScreen, CADASTRO);
        rootPanel.add(forgotPasswordScreen, FORGOT);
        rootPanel.add(appShellPanel, APP);
    }

    private void mostrarTelaInicial() {
        // O fluxo final sempre comeca no login.
        showLogin();
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
            // Quando loga, ja manda pra home. As outras telas recarregam so quando forem abertas.
            appShellPanel.showHome();
            rootLayout.show(rootPanel, APP);
        } else {
            showLogin();
        }
    }

    public void replaceUsuarioLogadoSilently(UsuarioConta usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public void atualizarUsuarioLogado() {
        if (usuarioLogado == null) {
            return;
        }

        autenticacaoService.buscarPorId(usuarioLogado.getId()).ifPresent(usuarioAtualizado -> this.usuarioLogado = usuarioAtualizado);
    }

    public void showLogin() {
        loginScreen.resetForm();
        rootLayout.show(rootPanel, LOGIN);
    }

    public void showCadastro() {
        cadastroScreen.resetForm();
        rootLayout.show(rootPanel, CADASTRO);
    }

    public void showForgotPassword() {
        forgotPasswordScreen.resetForm();
        rootLayout.show(rootPanel, FORGOT);
    }

    public void logout() {
        this.usuarioLogado = null;
        showLogin();
    }

    public void showAppScreen(AppScreenKey screenKey) {
        if (usuarioLogado == null) {
            return;
        }
        rootLayout.show(rootPanel, APP);
        appShellPanel.showScreen(screenKey);
    }

    public void toggleAppearance(AppScreenKey returnScreen) {
        UsuarioConta usuarioAtual = usuarioLogado;
        java.awt.Point currentLocation = getLocation();
        java.awt.Dimension currentSize = getSize();

        AppTheme.toggleTheme();

        IndicaFacilFrame updatedFrame = new IndicaFacilFrame(applicationContext);
        updatedFrame.setSize(currentSize);
        updatedFrame.setLocation(currentLocation);

        if (usuarioAtual != null) {
            updatedFrame.setUsuarioLogado(usuarioAtual);
            if (returnScreen != null) {
                updatedFrame.showAppScreen(returnScreen);
            }
        } else {
            updatedFrame.showLogin();
        }

        updatedFrame.setVisible(true);
        dispose();
    }
}
