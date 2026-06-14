package indicafacil.auth.model;

import indicafacil.auth.util.CpfUtils;
import indicafacil.auth.util.EmailUtils;
import indicafacil.auth.util.TelefoneUtils;

/*
 * Essa classe representa a conta do usuario ja pronta pra usar no sistema.
 * Ela guarda os dados principais e ja devolve CPF e telefone tratados quando precisa.
 */
public class UsuarioConta {
    private final long id;
    private final String nome;
    private final String email;
    private final String telefone;
    private final String cpf;
    private final String senhaHash;

    public UsuarioConta(long id, String nome, String email, String telefone, String cpf, String senhaHash) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do usuario nao pode ficar vazio.");
        }
        if (senhaHash == null || senhaHash.trim().isEmpty()) {
            throw new IllegalArgumentException("A senha do usuario nao pode ficar vazia.");
        }

        this.id = id;
        this.nome = nome.trim();
        // Ja normaliza tudo aqui pra nao ficar tratando dado em varios lugares.
        this.email = EmailUtils.normalizar(email);
        this.telefone = TelefoneUtils.normalizar(telefone);
        this.cpf = CpfUtils.normalizar(cpf);
        this.senhaHash = senhaHash;
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public String getCpfFormatado() {
        return CpfUtils.formatar(cpf);
    }

    public String getCpfMascarado() {
        return CpfUtils.mascarar(cpf);
    }

    public String getTelefoneFormatado() {
        return TelefoneUtils.formatar(telefone);
    }

    public boolean possuiEmail() {
        return !email.isEmpty();
    }

    public boolean possuiTelefone() {
        return !telefone.isEmpty();
    }

    @Override
    public String toString() {
        if (possuiEmail()) {
            return nome + " - E-mail: " + email;
        }

        return nome + " - CPF: " + getCpfMascarado();
    }
}
