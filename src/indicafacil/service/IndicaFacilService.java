package indicafacil.service;

import indicafacil.auth.model.UsuarioConta;
import indicafacil.auth.persistence.DatabaseManager;
import indicafacil.model.AtividadeUsuario;
import indicafacil.model.AvaliacaoPublica;
import indicafacil.model.CategoriaServico;
import indicafacil.model.DetalheTrabalhador;
import indicafacil.model.PerfilTrabalhador;
import indicafacil.persistence.AvaliacaoRepository;
import indicafacil.persistence.FavoritoRepository;
import indicafacil.persistence.TrabalhadorRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * Esse service segura as regras do resto do app depois do login.
 * Busca, perfil profissional, favoritos e avaliacoes passam por aqui.
 */
public class IndicaFacilService {
    private final TrabalhadorRepository trabalhadorRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private final FavoritoRepository favoritoRepository;

    public IndicaFacilService(DatabaseManager databaseManager) {
        this.trabalhadorRepository = new TrabalhadorRepository(databaseManager);
        this.avaliacaoRepository = new AvaliacaoRepository(databaseManager);
        this.favoritoRepository = new FavoritoRepository(databaseManager);
    }

    public boolean usuarioPossuiPerfilTrabalhador(long usuarioId) {
        return trabalhadorRepository.existePerfilPorUsuario(usuarioId);
    }

    public PerfilTrabalhador cadastrarMeuPerfilTrabalhador(
        UsuarioConta usuarioLogado,
        CategoriaServico categoria,
        String descricao,
        String empresa
    ) {
        // Aqui ele trava os casos que nao fazem sentido antes de salvar o perfil.
        if (usuarioLogado == null) {
            throw new IllegalArgumentException("Voce precisa estar logado para cadastrar um perfil.");
        }
        if (trabalhadorRepository.existePerfilPorUsuario(usuarioLogado.getId())) {
            throw new IllegalArgumentException("Sua conta ja possui um perfil de trabalhador cadastrado.");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("Escolha uma categoria de servico.");
        }
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe a descricao do servico.");
        }

        String empresaTratada = empresa == null || empresa.trim().isEmpty() ? "Autonomo" : empresa.trim();
        return trabalhadorRepository.inserir(usuarioLogado.getId(), categoria, descricao.trim(), empresaTratada);
    }

    public Optional<PerfilTrabalhador> buscarMeuPerfilTrabalhador(long usuarioId) {
        return trabalhadorRepository.buscarPorUsuarioId(usuarioId);
    }

    public List<PerfilTrabalhador> listarTrabalhadores() {
        return trabalhadorRepository.listarTodos();
    }

    public List<PerfilTrabalhador> buscarPorCategoria(CategoriaServico categoria) {
        return trabalhadorRepository.listarPorCategoria(categoria);
    }

    public List<PerfilTrabalhador> listarRanking() {
        return trabalhadorRepository.listarRanking();
    }

    public List<PerfilTrabalhador> listarMaisBuscados() {
        return trabalhadorRepository.listarMaisBuscados(4);
    }

    public List<PerfilTrabalhador> buscarPorTexto(String termo) {
        String termoTratado = termo == null ? "" : termo.trim().toLowerCase();
        List<PerfilTrabalhador> encontrados = new ArrayList<>();

        // A busca ainda esta simples, entao compara o termo com os campos principais.
        for (PerfilTrabalhador trabalhador : listarTrabalhadores()) {
            if (termoTratado.isEmpty()
                || trabalhador.getNome().toLowerCase().contains(termoTratado)
                || trabalhador.getCategoria().getDescricao().toLowerCase().contains(termoTratado)
                || trabalhador.getDescricao().toLowerCase().contains(termoTratado)
                || trabalhador.getEmpresa().toLowerCase().contains(termoTratado)) {
                encontrados.add(trabalhador);
            }
        }

        return encontrados;
    }

    public DetalheTrabalhador detalharTrabalhador(long trabalhadorId) {
        Optional<PerfilTrabalhador> trabalhador = trabalhadorRepository.buscarPorId(trabalhadorId);
        if (!trabalhador.isPresent()) {
            throw new IllegalArgumentException("Trabalhador nao encontrado.");
        }

        List<AvaliacaoPublica> avaliacoes = avaliacaoRepository.listarPorTrabalhador(trabalhadorId);
        return new DetalheTrabalhador(trabalhador.get(), avaliacoes);
    }

    public void registrarAvaliacao(
        UsuarioConta autor,
        long trabalhadorId,
        int nota,
        String comentario,
        boolean anonima
    ) {
        // Antes de salvar, passa por todas as travas de seguranca da avaliacao.
        if (autor == null) {
            throw new IllegalArgumentException("Voce precisa estar logado para avaliar.");
        }
        if (trabalhadorRepository.existePerfilPorUsuario(autor.getId())) {
            throw new IllegalArgumentException("Contas com perfil de trabalhador nao podem avaliar outros trabalhadores.");
        }
        if (nota < 1 || nota > 5) {
            throw new IllegalArgumentException("A nota precisa estar entre 1 e 5.");
        }

        Optional<PerfilTrabalhador> trabalhador = trabalhadorRepository.buscarPorId(trabalhadorId);
        if (!trabalhador.isPresent()) {
            throw new IllegalArgumentException("Trabalhador nao encontrado.");
        }
        if (trabalhador.get().getUsuarioId() == autor.getId()) {
            throw new IllegalArgumentException("Voce nao pode avaliar seu proprio perfil.");
        }
        if (avaliacaoRepository.autorJaAvaliou(autor.getId(), trabalhadorId)) {
            throw new IllegalArgumentException("Voce ja avaliou esse trabalhador.");
        }

        String comentarioTratado = comentario == null || comentario.trim().isEmpty()
            ? "Sem comentario."
            : comentario.trim();

        avaliacaoRepository.inserir(autor.getId(), trabalhadorId, nota, comentarioTratado, anonima);
    }

    public List<PerfilTrabalhador> listarFavoritos(long usuarioId) {
        return favoritoRepository.listarFavoritos(usuarioId);
    }

    public boolean trabalhadorEstaNosFavoritos(long usuarioId, long trabalhadorId) {
        return favoritoRepository.usuarioFavoritouTrabalhador(usuarioId, trabalhadorId);
    }

    public boolean alternarFavorito(UsuarioConta usuarioLogado, long trabalhadorId) {
        if (usuarioLogado == null) {
            throw new IllegalArgumentException("Voce precisa estar logado para favoritar.");
        }

        Optional<PerfilTrabalhador> trabalhador = trabalhadorRepository.buscarPorId(trabalhadorId);
        if (!trabalhador.isPresent()) {
            throw new IllegalArgumentException("Trabalhador nao encontrado.");
        }
        if (trabalhador.get().getUsuarioId() == usuarioLogado.getId()) {
            throw new IllegalArgumentException("Voce nao pode favoritar o seu proprio perfil.");
        }

        // Esse metodo faz o toggle: se ja favoritou remove, senao adiciona.
        if (favoritoRepository.usuarioFavoritouTrabalhador(usuarioLogado.getId(), trabalhadorId)) {
            favoritoRepository.removerFavorito(usuarioLogado.getId(), trabalhadorId);
            return false;
        }

        favoritoRepository.adicionarFavorito(usuarioLogado.getId(), trabalhadorId);
        return true;
    }

    public List<AtividadeUsuario> listarAtividadeDoUsuario(long usuarioId) {
        return avaliacaoRepository.listarAtividadesDoAutor(usuarioId);
    }
}
