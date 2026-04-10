package indicafacil.persistence;

import indicafacil.auth.persistence.DatabaseManager;
import indicafacil.model.PerfilTrabalhador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/*
 * Esse repositorio cuida da lista de favoritos.
 * Ele faz o toggle no banco e depois monta os perfis favoritos pra interface.
 */
public class FavoritoRepository {
    private final DatabaseManager databaseManager;
    private final TrabalhadorRepository trabalhadorRepository;

    public FavoritoRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.trabalhadorRepository = new TrabalhadorRepository(databaseManager);
    }

    public boolean usuarioFavoritouTrabalhador(long usuarioId, long trabalhadorId) {
        String sql = "SELECT COUNT(*) FROM favorito WHERE usuario_id = ? AND trabalhador_id = ?";

        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setLong(1, usuarioId);
            statement.setLong(2, trabalhadorId);

            try (java.sql.ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel consultar os favoritos do usuario.", exception);
        }
    }

    public void adicionarFavorito(long usuarioId, long trabalhadorId) {
        String sql = "INSERT INTO favorito (usuario_id, trabalhador_id, data_favorito) VALUES (?, ?, ?)";

        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setLong(1, usuarioId);
            statement.setLong(2, trabalhadorId);
            statement.setString(3, LocalDate.now().toString());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel adicionar o trabalhador aos favoritos.", exception);
        }
    }

    public void removerFavorito(long usuarioId, long trabalhadorId) {
        String sql = "DELETE FROM favorito WHERE usuario_id = ? AND trabalhador_id = ?";

        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setLong(1, usuarioId);
            statement.setLong(2, trabalhadorId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel remover o trabalhador dos favoritos.", exception);
        }
    }

    public List<PerfilTrabalhador> listarFavoritos(long usuarioId) {
        String sql = "SELECT trabalhador_id FROM favorito WHERE usuario_id = ? ORDER BY data_favorito DESC";
        java.util.ArrayList<PerfilTrabalhador> favoritos = new java.util.ArrayList<>();

        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setLong(1, usuarioId);

            try (java.sql.ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    trabalhadorRepository.buscarPorId(resultSet.getLong("trabalhador_id")).ifPresent(favoritos::add);
                }
            }

            return favoritos;
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel listar os trabalhadores favoritos.", exception);
        }
    }
}
