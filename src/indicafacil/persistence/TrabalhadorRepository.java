package indicafacil.persistence;

import indicafacil.auth.persistence.DatabaseManager;
import indicafacil.model.CategoriaServico;
import indicafacil.model.PerfilTrabalhador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * Esse repositorio fala com a tabela de trabalhador.
 * Ele salva o perfil profissional e monta as listas que aparecem na home e na busca.
 */
public class TrabalhadorRepository {
    private static final String SELECT_BASE = "SELECT "
        + "t.id AS trabalhador_id, "
        + "t.usuario_id, "
        + "u.nome, "
        + "t.categoria, "
        + "t.descricao, "
        + "t.empresa, "
        + "COALESCE(AVG(a.nota), 0) AS media_avaliacoes, "
        + "COUNT(a.id) AS quantidade_avaliacoes "
        + "FROM trabalhador t "
        + "JOIN usuario u ON u.id = t.usuario_id "
        + "LEFT JOIN avaliacao a ON a.trabalhador_id = t.id ";

    private static final String GROUP_BY = " GROUP BY "
        + "t.id, t.usuario_id, u.nome, t.categoria, t.descricao, t.empresa ";

    private final DatabaseManager databaseManager;

    public TrabalhadorRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public boolean existePerfilPorUsuario(long usuarioId) {
        String sql = "SELECT COUNT(*) FROM trabalhador WHERE usuario_id = ?";

        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setLong(1, usuarioId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel verificar o perfil de trabalhador.", exception);
        }
    }

    public PerfilTrabalhador inserir(long usuarioId, CategoriaServico categoria, String descricao, String empresa) {
        String sql = "INSERT INTO trabalhador (usuario_id, categoria, descricao, empresa) VALUES (?, ?, ?, ?)";

        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, usuarioId);
            statement.setInt(2, categoria.getCodigo());
            statement.setString(3, descricao);
            statement.setString(4, empresa);
            statement.executeUpdate();

            Optional<PerfilTrabalhador> perfilCriado = buscarPorUsuarioId(usuarioId);
            if (perfilCriado.isPresent()) {
                return perfilCriado.get();
            }

            throw new IllegalStateException("Perfil criado, mas nao foi possivel recupera-lo.");
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel salvar o perfil de trabalhador.", exception);
        }
    }

    public Optional<PerfilTrabalhador> buscarPorUsuarioId(long usuarioId) {
        String sql = SELECT_BASE + "WHERE t.usuario_id = ?" + GROUP_BY;
        return buscarPerfilUnico(sql, usuarioId);
    }

    public Optional<PerfilTrabalhador> buscarPorId(long trabalhadorId) {
        String sql = SELECT_BASE + "WHERE t.id = ?" + GROUP_BY;
        return buscarPerfilUnico(sql, trabalhadorId);
    }

    public List<PerfilTrabalhador> listarTodos() {
        String sql = SELECT_BASE + GROUP_BY + " ORDER BY u.nome ASC";
        return listarPerfis(sql);
    }

    public List<PerfilTrabalhador> listarPorCategoria(CategoriaServico categoria) {
        String sql = SELECT_BASE + "WHERE t.categoria = ?" + GROUP_BY + " ORDER BY u.nome ASC";
        return listarPerfis(sql, categoria.getCodigo());
    }

    public List<PerfilTrabalhador> listarRanking() {
        String sql = SELECT_BASE
            + GROUP_BY
            + " ORDER BY media_avaliacoes DESC, quantidade_avaliacoes DESC, u.nome ASC";
        return listarPerfis(sql);
    }

    public List<PerfilTrabalhador> listarMaisBuscados(int limite) {
        String sql = SELECT_BASE
            + GROUP_BY
            + " ORDER BY quantidade_avaliacoes DESC, media_avaliacoes DESC, u.nome ASC LIMIT ?";
        return listarPerfis(sql, limite);
    }

    private Optional<PerfilTrabalhador> buscarPerfilUnico(String sql, long parametro) {
        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setLong(1, parametro);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapearPerfil(resultSet));
                }
            }

            return Optional.empty();
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel buscar o perfil de trabalhador.", exception);
        }
    }

    private List<PerfilTrabalhador> listarPerfis(String sql, Object... parametros) {
        List<PerfilTrabalhador> perfis = new ArrayList<>();

        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            // Como essa query muda um pouco, eu deixei os parametros mais genericos aqui.
            for (int i = 0; i < parametros.length; i++) {
                Object parametro = parametros[i];
                if (parametro instanceof Integer) {
                    statement.setInt(i + 1, (Integer) parametro);
                } else if (parametro instanceof Long) {
                    statement.setLong(i + 1, (Long) parametro);
                }
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    perfis.add(mapearPerfil(resultSet));
                }
            }

            return perfis;
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel listar os perfis de trabalhador.", exception);
        }
    }

    private PerfilTrabalhador mapearPerfil(ResultSet resultSet) throws SQLException {
        // Sai do banco e ja vira o objeto pronto pro resto do sistema.
        return new PerfilTrabalhador(
            resultSet.getLong("trabalhador_id"),
            resultSet.getLong("usuario_id"),
            resultSet.getString("nome"),
            CategoriaServico.fromCodigo(resultSet.getInt("categoria")),
            resultSet.getString("descricao"),
            resultSet.getString("empresa"),
            resultSet.getDouble("media_avaliacoes"),
            resultSet.getInt("quantidade_avaliacoes")
        );
    }
}
