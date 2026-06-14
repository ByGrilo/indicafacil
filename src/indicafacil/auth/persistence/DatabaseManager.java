package indicafacil.auth.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Essa classe cuida da conexao com o SQLite.
 * Ela monta o caminho do banco, cria as tabelas e faz os ajustes basicos quando o app sobe.
 */
public class DatabaseManager {
    private final String jdbcUrl;

    public DatabaseManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            Path diretorioBanco = Paths.get("data");
            Files.createDirectories(diretorioBanco);
            Path caminhoBanco = diretorioBanco.resolve("indicafacil.db").toAbsolutePath();
            this.jdbcUrl = "jdbc:sqlite:" + caminhoBanco;
            inicializarBanco();
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("Driver SQLite nao encontrado no projeto.", exception);
        } catch (IOException exception) {
            throw new IllegalStateException("Nao foi possivel preparar o diretorio do banco de dados.", exception);
        }
    }

    public Connection obterConexao() throws SQLException {
        Connection conexao = DriverManager.getConnection(jdbcUrl);
        try (Statement statement = conexao.createStatement()) {
            // Isso ativa as regras de chave estrangeira do SQLite.
            statement.execute("PRAGMA foreign_keys = ON");
        }
        return conexao;
    }

    private void inicializarBanco() {
        // Essas queries deixam o banco pronto mesmo se ele ainda nao existir.
        String sqlUsuario = "CREATE TABLE IF NOT EXISTS usuario ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "nome TEXT NOT NULL, "
            + "email TEXT, "
            + "telefone TEXT, "
            + "cpf TEXT NOT NULL UNIQUE, "
            + "senha TEXT NOT NULL"
            + ")";

        String sqlTrabalhador = "CREATE TABLE IF NOT EXISTS trabalhador ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "usuario_id INTEGER NOT NULL UNIQUE, "
            + "categoria INTEGER NOT NULL, "
            + "descricao TEXT NOT NULL, "
            + "empresa TEXT NOT NULL, "
            + "FOREIGN KEY(usuario_id) REFERENCES usuario(id) ON DELETE CASCADE"
            + ")";

        String sqlAvaliacao = "CREATE TABLE IF NOT EXISTS avaliacao ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "autor_usuario_id INTEGER NOT NULL, "
            + "trabalhador_id INTEGER NOT NULL, "
            + "nota INTEGER NOT NULL CHECK(nota BETWEEN 1 AND 5), "
            + "comentario TEXT NOT NULL, "
            + "anonima INTEGER NOT NULL, "
            + "data_avaliacao TEXT NOT NULL, "
            + "UNIQUE(autor_usuario_id, trabalhador_id), "
            + "FOREIGN KEY(autor_usuario_id) REFERENCES usuario(id) ON DELETE CASCADE, "
            + "FOREIGN KEY(trabalhador_id) REFERENCES trabalhador(id) ON DELETE CASCADE"
            + ")";

        String sqlFavorito = "CREATE TABLE IF NOT EXISTS favorito ("
            + "usuario_id INTEGER NOT NULL, "
            + "trabalhador_id INTEGER NOT NULL, "
            + "data_favorito TEXT NOT NULL, "
            + "PRIMARY KEY(usuario_id, trabalhador_id), "
            + "FOREIGN KEY(usuario_id) REFERENCES usuario(id) ON DELETE CASCADE, "
            + "FOREIGN KEY(trabalhador_id) REFERENCES trabalhador(id) ON DELETE CASCADE"
            + ")";

        try (Connection conexao = obterConexao(); Statement statement = conexao.createStatement()) {
            statement.executeUpdate(sqlUsuario);
            garantirColunaUsuario(conexao, "email", "TEXT");
            garantirColunaUsuario(conexao, "telefone", "TEXT");
            statement.executeUpdate("CREATE UNIQUE INDEX IF NOT EXISTS idx_usuario_email ON usuario(email)");
            statement.executeUpdate(sqlTrabalhador);
            statement.executeUpdate(sqlAvaliacao);
            statement.executeUpdate(sqlFavorito);
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel inicializar o banco de dados.", exception);
        }
    }

    private void garantirColunaUsuario(Connection conexao, String nomeColuna, String definicao) throws SQLException {
        if (colunaExiste(conexao, "usuario", nomeColuna)) {
            return;
        }

        try (Statement statement = conexao.createStatement()) {
            // Isso ajuda quando o banco vem de uma versao mais antiga do projeto.
            statement.executeUpdate("ALTER TABLE usuario ADD COLUMN " + nomeColuna + " " + definicao);
        }
    }

    private boolean colunaExiste(Connection conexao, String tabela, String coluna) throws SQLException {
        try (
            Statement statement = conexao.createStatement();
            ResultSet resultSet = statement.executeQuery("PRAGMA table_info(" + tabela + ")")
        ) {
            while (resultSet.next()) {
                if (coluna.equalsIgnoreCase(resultSet.getString("name"))) {
                    return true;
                }
            }
        }

        return false;
    }
}
