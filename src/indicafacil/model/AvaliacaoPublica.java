package indicafacil.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/*
 * Essa classe representa a avaliacao que aparece publicamente.
 * Ela respeita o anonimato e ja monta um resumo pronto pra mostrar na interface.
 */
public class AvaliacaoPublica {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final String autorNomeExibicao;
    private final int nota;
    private final String comentario;
    private final boolean anonima;
    private final LocalDate dataAvaliacao;

    public AvaliacaoPublica(String autorNomeExibicao, int nota, String comentario, boolean anonima, LocalDate dataAvaliacao) {
        this.autorNomeExibicao = autorNomeExibicao;
        this.nota = nota;
        this.comentario = comentario;
        this.anonima = anonima;
        this.dataAvaliacao = dataAvaliacao;
    }

    public String getAutorNomeExibicao() {
        return autorNomeExibicao;
    }

    public int getNota() {
        return nota;
    }

    public String getComentario() {
        return comentario;
    }

    public boolean isAnonima() {
        return anonima;
    }

    public LocalDate getDataAvaliacao() {
        return dataAvaliacao;
    }

    public String getEstrelas() {
        StringBuilder builder = new StringBuilder();

        // Mantive simples com asterisco pra funcionar em qualquer fonte.
        for (int i = 0; i < nota; i++) {
            builder.append("*");
        }

        return builder.toString();
    }

    public String getResumo() {
        String autor = anonima ? "Cliente anonimo" : autorNomeExibicao;
        return getEstrelas() + " por " + autor + " em " + dataAvaliacao.format(FORMATTER)
            + " - " + comentario;
    }
}
