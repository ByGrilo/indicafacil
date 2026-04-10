package indicafacil.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/*
 * Esse modelo guarda um item da aba de atividade.
 * Ele junta a avaliacao feita com alguns dados do trabalhador pra tela montar tudo sem complicar.
 */
public class AtividadeUsuario {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final long trabalhadorId;
    private final String trabalhadorNome;
    private final CategoriaServico categoria;
    private final double mediaTrabalhador;
    private final int notaDada;
    private final String comentario;
    private final LocalDate dataAvaliacao;

    public AtividadeUsuario(
        long trabalhadorId,
        String trabalhadorNome,
        CategoriaServico categoria,
        double mediaTrabalhador,
        int notaDada,
        String comentario,
        LocalDate dataAvaliacao
    ) {
        this.trabalhadorId = trabalhadorId;
        this.trabalhadorNome = trabalhadorNome;
        this.categoria = categoria;
        this.mediaTrabalhador = mediaTrabalhador;
        this.notaDada = notaDada;
        this.comentario = comentario;
        this.dataAvaliacao = dataAvaliacao;
    }

    public long getTrabalhadorId() {
        return trabalhadorId;
    }

    public String getTrabalhadorNome() {
        return trabalhadorNome;
    }

    public CategoriaServico getCategoria() {
        return categoria;
    }

    public double getMediaTrabalhador() {
        return mediaTrabalhador;
    }

    public int getNotaDada() {
        return notaDada;
    }

    public String getComentario() {
        return comentario;
    }

    public LocalDate getDataAvaliacao() {
        return dataAvaliacao;
    }

    public String getResumo() {
        return trabalhadorNome
            + " | "
            + categoria.getDescricao()
            + " | Media: "
            + String.format("%.1f", mediaTrabalhador)
            + " | Sua nota: "
            + notaDada
            + " | "
            + comentario
            + " | "
            + dataAvaliacao.format(FORMATTER);
    }
}
