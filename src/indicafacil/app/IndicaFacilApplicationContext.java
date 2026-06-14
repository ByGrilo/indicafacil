package indicafacil.app;

import indicafacil.auth.persistence.DatabaseManager;
import indicafacil.auth.persistence.UsuarioRepository;
import indicafacil.auth.service.AutenticacaoService;
import indicafacil.service.IndicaFacilService;

/*
 * Esse contexto junta os servicos principais do app.
 * A ideia aqui foi preparar a parte pesada antes de abrir a janela.
 */
public class IndicaFacilApplicationContext {
    private final DatabaseManager databaseManager;
    private final UsuarioRepository usuarioRepository;
    private final AutenticacaoService autenticacaoService;
    private final IndicaFacilService indicaFacilService;

    public IndicaFacilApplicationContext() {
        this.databaseManager = new DatabaseManager();
        this.usuarioRepository = new UsuarioRepository(databaseManager);
        this.autenticacaoService = new AutenticacaoService(usuarioRepository);
        this.indicaFacilService = new IndicaFacilService(databaseManager);
    }

    public AutenticacaoService getAutenticacaoService() {
        return autenticacaoService;
    }

    public IndicaFacilService getIndicaFacilService() {
        return indicaFacilService;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public UsuarioRepository getUsuarioRepository() {
        return usuarioRepository;
    }
}
