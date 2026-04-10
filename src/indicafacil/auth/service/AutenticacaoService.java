package indicafacil.auth.service;

import indicafacil.auth.model.UsuarioConta;
import indicafacil.auth.persistence.UsuarioRepository;
import indicafacil.auth.util.CpfUtils;
import indicafacil.auth.util.EmailUtils;
import indicafacil.auth.util.PasswordUtils;
import indicafacil.auth.util.TelefoneUtils;
import java.util.List;
import java.util.Optional;

/*
 * Aqui ficam as regras de cadastro, login e recuperacao de senha.
 * A tela chama esse service e ele decide o que pode ou nao pode passar.
 */
public class AutenticacaoService {
    private final UsuarioRepository usuarioRepository;

    public AutenticacaoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean possuiUsuariosCadastrados() {
        return usuarioRepository.existeAlgumUsuario();
    }

    public UsuarioConta criarConta(String nome, String email, String telefone, String cpf, String senha) {
        // Antes de gravar, passa por todas as validacoes principais.
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o nome do usuario.");
        }
        if (!EmailUtils.isValido(email)) {
            throw new IllegalArgumentException("Informe um e-mail valido.");
        }
        if (!TelefoneUtils.isValido(telefone)) {
            throw new IllegalArgumentException("Informe um telefone valido com DDD.");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe uma senha para a conta.");
        }
        if (senha.trim().length() < 6) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres.");
        }
        if (!CpfUtils.isValido(cpf)) {
            throw new IllegalArgumentException("CPF invalido. Digite um CPF valido.");
        }

        String emailNormalizado = EmailUtils.normalizar(email);
        if (usuarioRepository.buscarPorEmail(emailNormalizado).isPresent()) {
            throw new IllegalArgumentException("Ja existe uma conta cadastrada com esse e-mail.");
        }

        String cpfNormalizado = CpfUtils.normalizar(cpf);
        if (usuarioRepository.buscarPorCpf(cpfNormalizado).isPresent()) {
            throw new IllegalArgumentException("Ja existe uma conta cadastrada com esse CPF.");
        }

        return usuarioRepository.inserir(
            nome.trim(),
            emailNormalizado,
            TelefoneUtils.normalizar(telefone),
            cpfNormalizado,
            PasswordUtils.gerarHash(senha)
        );
    }

    public UsuarioConta autenticar(String identificador, String senha) {
        if (identificador == null || identificador.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o e-mail ou CPF para entrar.");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe a senha para entrar.");
        }
        if (!EmailUtils.isValido(identificador) && !CpfUtils.isValido(identificador)) {
            throw new IllegalArgumentException("Informe um e-mail valido ou um CPF valido.");
        }

        // O login aceita dois caminhos de entrada, mas a senha sempre e comparada pelo hash salvo.
        Optional<UsuarioConta> usuarioEncontrado = usuarioRepository.buscarPorIdentificador(identificador);
        if (!usuarioEncontrado.isPresent()) {
            throw new IllegalArgumentException("Conta nao encontrada. Verifique os dados informados.");
        }
        if (!PasswordUtils.verificarSenha(senha, usuarioEncontrado.get().getSenhaHash())) {
            throw new IllegalArgumentException("Senha incorreta. Tente novamente.");
        }

        return usuarioEncontrado.get();
    }

    public void solicitarRecuperacaoSenha(String email) {
        if (!EmailUtils.isValido(email)) {
            throw new IllegalArgumentException("Informe um e-mail valido para recuperar a senha.");
        }
        if (!usuarioRepository.buscarPorEmail(email).isPresent()) {
            throw new IllegalArgumentException("Nao existe conta cadastrada com esse e-mail.");
        }
    }

    public UsuarioConta atualizarDadosDaConta(long usuarioId, String nome, String email, String telefone) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o nome do usuario.");
        }
        if (!EmailUtils.isValido(email)) {
            throw new IllegalArgumentException("Informe um e-mail valido.");
        }
        if (!TelefoneUtils.isValido(telefone)) {
            throw new IllegalArgumentException("Informe um telefone valido com DDD.");
        }

        Optional<UsuarioConta> usuarioAtual = usuarioRepository.buscarPorId(usuarioId);
        if (!usuarioAtual.isPresent()) {
            throw new IllegalArgumentException("Conta nao encontrada.");
        }

        String emailNormalizado = EmailUtils.normalizar(email);
        // Isso evita que duas contas acabem com o mesmo e-mail.
        Optional<UsuarioConta> contaComMesmoEmail = usuarioRepository.buscarPorEmail(emailNormalizado);
        if (contaComMesmoEmail.isPresent() && contaComMesmoEmail.get().getId() != usuarioId) {
            throw new IllegalArgumentException("Ja existe uma conta cadastrada com esse e-mail.");
        }

        return usuarioRepository.atualizarDadosBasicos(usuarioId, nome.trim(), emailNormalizado, telefone);
    }

    public void redefinirSenhaPorEmail(String email, String novaSenha) {
        if (!EmailUtils.isValido(email)) {
            throw new IllegalArgumentException("Informe um e-mail valido.");
        }
        if (novaSenha == null || novaSenha.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe uma nova senha.");
        }
        if (novaSenha.trim().length() < 6) {
            throw new IllegalArgumentException("A nova senha deve ter pelo menos 6 caracteres.");
        }

        Optional<UsuarioConta> usuario = usuarioRepository.buscarPorEmail(email);
        if (!usuario.isPresent()) {
            throw new IllegalArgumentException("Nao existe conta cadastrada com esse e-mail.");
        }

        // A senha nova tambem vai pro banco ja em hash, nunca texto puro.
        usuarioRepository.atualizarSenha(usuario.get().getId(), PasswordUtils.gerarHash(novaSenha));
    }

    public Optional<UsuarioConta> buscarPorId(long usuarioId) {
        return usuarioRepository.buscarPorId(usuarioId);
    }

    public List<UsuarioConta> listarUsuarios() {
        return usuarioRepository.listarTodos();
    }
}
