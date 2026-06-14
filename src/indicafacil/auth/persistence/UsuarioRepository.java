package indicafacil.auth.persistence;

import indicafacil.auth.model.UsuarioConta;
import indicafacil.auth.util.CpfUtils;
import indicafacil.auth.util.EmailUtils;
import indicafacil.auth.util.TelefoneUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * Esse repositorio fala direto com a tabela de usuario.
 * Aqui fica a parte de salvar, buscar e atualizar conta no banco.
 */
public class UsuarioRepository {
    private final DatabaseManager databaseManager;

    public UsuarioRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public boolean existeAlgumUsuario() {
        String sql = "SELECT COUNT(*) FROM usuario";

        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {
            return resultSet.next() && resultSet.getInt(1) > 0;
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel consultar os usuarios cadastrados.", exception);
        }
    }

    public Optional<UsuarioConta> buscarPorCpf(String cpf) {
        String sql = "SELECT id, nome, email, telefone, cpf, senha FROM usuario WHERE cpf = ?";

        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setString(1, CpfUtils.normalizar(cpf));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapearUsuario(resultSet));
                }
            }

            return Optional.empty();
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel buscar o usuario pelo CPF.", exception);
        }
    }

    public Optional<UsuarioConta> buscarPorEmail(String email) {
        String sql = "SELECT id, nome, email, telefone, cpf, senha FROM usuario WHERE lower(email) = ?";

        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setString(1, EmailUtils.normalizar(email));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapearUsuario(resultSet));
                }
            }

            return Optional.empty();
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel buscar o usuario pelo e-mail.", exception);
        }
    }

    public Optional<UsuarioConta> buscarPorIdentificador(String identificador) {
        String identificadorNormalizado = identificador == null ? "" : identificador.trim();

        // Primeiro tenta como e-mail. Se nao achar, cai pro CPF.
        if (EmailUtils.isValido(identificadorNormalizado)) {
            Optional<UsuarioConta> usuarioPorEmail = buscarPorEmail(identificadorNormalizado);
            if (usuarioPorEmail.isPresent()) {
                return usuarioPorEmail;
            }
        }

        return buscarPorCpf(identificadorNormalizado);
    }

    public Optional<UsuarioConta> buscarPorId(long id) {
        String sql = "SELECT id, nome, email, telefone, cpf, senha FROM usuario WHERE id = ?";

        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapearUsuario(resultSet));
                }
            }

            return Optional.empty();
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel buscar o usuario pelo ID.", exception);
        }
    }

    public UsuarioConta inserir(String nome, String email, String telefone, String cpf, String senhaHash) {
        String sql = "INSERT INTO usuario (nome, email, telefone, cpf, senha) VALUES (?, ?, ?, ?, ?)";

        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, nome);
            statement.setString(2, EmailUtils.normalizar(email));
            statement.setString(3, TelefoneUtils.normalizar(telefone));
            statement.setString(4, CpfUtils.normalizar(cpf));
            statement.setString(5, senhaHash);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new UsuarioConta(generatedKeys.getLong(1), nome, email, telefone, cpf, senhaHash);
                }
            }

            throw new IllegalStateException("Usuario cadastrado, mas o ID gerado nao foi retornado.");
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel salvar o usuario no banco de dados.", exception);
        }
    }

    public UsuarioConta atualizarDadosBasicos(long usuarioId, String nome, String email, String telefone) {
        String sql = "UPDATE usuario SET nome = ?, email = ?, telefone = ? WHERE id = ?";

        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setString(1, nome);
            statement.setString(2, EmailUtils.normalizar(email));
            statement.setString(3, TelefoneUtils.normalizar(telefone));
            statement.setLong(4, usuarioId);
            statement.executeUpdate();

            Optional<UsuarioConta> usuarioAtualizado = buscarPorId(usuarioId);
            if (usuarioAtualizado.isPresent()) {
                return usuarioAtualizado.get();
            }

            throw new IllegalStateException("Usuario atualizado, mas nao foi possivel recupera-lo.");
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel atualizar os dados da conta.", exception);
        }
    }

    public void atualizarSenha(long usuarioId, String senhaHash) {
        String sql = "UPDATE usuario SET senha = ? WHERE id = ?";

        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql)
        ) {
            statement.setString(1, senhaHash);
            statement.setLong(2, usuarioId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel atualizar a senha do usuario.", exception);
        }
    }

    public List<UsuarioConta> listarTodos() {
        String sql = "SELECT id, nome, email, telefone, cpf, senha FROM usuario ORDER BY nome ASC, id ASC";
        List<UsuarioConta> usuarios = new ArrayList<>();

        try (
            Connection conexao = databaseManager.obterConexao();
            PreparedStatement statement = conexao.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                usuarios.add(mapearUsuario(resultSet));
            }

            return usuarios;
        } catch (SQLException exception) {
            throw new IllegalStateException("Nao foi possivel listar os usuarios cadastrados.", exception);
        }
    }

    private UsuarioConta mapearUsuario(ResultSet resultSet) throws SQLException {
        // Esse trecho transforma a linha vinda do banco num objeto do sistema.
        return new UsuarioConta(
            resultSet.getLong("id"),
            resultSet.getString("nome"),
            resultSet.getString("email"),
            resultSet.getString("telefone"),
            resultSet.getString("cpf"),
            resultSet.getString("senha")
        );
    }
}
