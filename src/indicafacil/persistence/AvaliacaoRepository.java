package indicafacil.persistence;

import indicafacil.auth.persistence.DatabaseManager;
import indicafacil.model.AtividadeUsuario;
import indicafacil.model.AvaliacaoPublica;
import indicafacil.model.CategoriaServico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
 * Esse repositorio mexe so com a tabela de avaliacao.
 * Alem de salvar nota e comentario, ele tambem busca historico e atividade do usuario.
 */
public class AvaliacaoRepository {
    private final DatabaseManager databaseManager;

    public AvaliacaoRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public boolean autorJaAvaliou(long autorUsuarioId, long trabalhadorId) {
        String sql = "SELECT COUNT(*) FROM avaliacao WHERE autor_usuario_id = ? AND trabalhador_id = ?";

        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setLong(1, autorUsuarioId);
            statement.setLong(2, trabalhadorId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel verificar as avaliacoes existentes.", exception);
        }
    }

    public void inserir(long autorUsuarioId, long trabalhadorId, int nota, String comentario, boolean anonima) {
        String sql = "INSERT INTO avaliacao (autor_usuario_id, trabalhador_id, nota, comentario, anonima, data_avaliacao) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setLong(1, autorUsuarioId);
            statement.setLong(2, trabalhadorId);
            statement.setInt(3, nota);
            statement.setString(4, comentario);
            statement.setInt(5, anonima ? 1 : 0);
            statement.setString(6, LocalDate.now().toString());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel salvar a avaliacao no banco de dados.", exception);
        }
    }

    public List<AvaliacaoPublica> listarPorTrabalhador(long trabalhadorId) {
        String sql = "SELECT u.nome AS autor_nome, a.nota, a.comentario, a.anonima, a.data_avaliacao "
            + "FROM avaliacao a "
            + "JOIN usuario u ON u.id = a.autor_usuario_id "
            + "WHERE a.trabalhador_id = ? "
            + "ORDER BY a.data_avaliacao DESC, a.id DESC";

        List<AvaliacaoPublica> avaliacoes = new ArrayList<>();

        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setLong(1, trabalhadorId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    avaliacoes.add(
                        new AvaliacaoPublica(
                            resultSet.getString("autor_nome"),
                            resultSet.getInt("nota"),
                            resultSet.getString("comentario"),
                            resultSet.getInt("anonima") == 1,
                            LocalDate.parse(resultSet.getString("data_avaliacao"))
                        )
                    );
                }
            }

            return avaliacoes;
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel listar as avaliacoes do trabalhador.", exception);
        }
    }

    public List<AtividadeUsuario> listarAtividadesDoAutor(long autorUsuarioId) {
        // Aqui eu puxo a avaliacao feita pelo usuario junto com a media atual do trabalhador.
        String sql = "SELECT "
            + "t.id AS trabalhador_id, "
            + "u.nome AS trabalhador_nome, "
            + "t.categoria, "
            + "COALESCE(("
            + "  SELECT AVG(a2.nota) "
            + "  FROM avaliacao a2 "
            + "  WHERE a2.trabalhador_id = t.id"
            + "), 0) AS media_trabalhador, "
            + "a.nota AS nota_dada, "
            + "a.comentario, "
            + "a.data_avaliacao "
            + "FROM avaliacao a "
            + "JOIN trabalhador t ON t.id = a.trabalhador_id "
            + "JOIN usuario u ON u.id = t.usuario_id "
            + "WHERE a.autor_usuario_id = ? "
            + "ORDER BY a.data_avaliacao DESC, a.id DESC";

        List<AtividadeUsuario> atividades = new ArrayList<>();

        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setLong(1, autorUsuarioId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    atividades.add(
                        new AtividadeUsuario(
                            resultSet.getLong("trabalhador_id"),
                            resultSet.getString("trabalhador_nome"),
                            CategoriaServico.fromCodigo(resultSet.getInt("categoria")),
                            resultSet.getDouble("media_trabalhador"),
                            resultSet.getInt("nota_dada"),
                            resultSet.getString("comentario"),
                            LocalDate.parse(resultSet.getString("data_avaliacao"))
                        )
                    );
                }
            }

            return atividades;
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel listar a atividade do usuario.", exception);
        }
    }
}
