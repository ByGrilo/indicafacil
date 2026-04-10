package indicafacil.model;

/*
 * Essa classe representa o perfil publico do prestador.
 * Ela leva o basico que a busca, a home e os detalhes precisam mostrar.
 */
public class PerfilTrabalhador {
    private final long id;
    private final long usuarioId;
    private final String nome;
    private final CategoriaServico categoria;
    private final String descricao;
    private final String empresa;
    private final double mediaAvaliacoes;
    private final int quantidadeAvaliacoes;

    public PerfilTrabalhador(
        long id,
        long usuarioId,
        String nome,
        CategoriaServico categoria,
        String descricao,
        String empresa,
        double mediaAvaliacoes,
        int quantidadeAvaliacoes
    ) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.categoria = categoria;
        this.descricao = descricao;
        this.empresa = empresa;
        this.mediaAvaliacoes = mediaAvaliacoes;
        this.quantidadeAvaliacoes = quantidadeAvaliacoes;
    }

    public long getId() {
        return id;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public String getNome() {
        return nome;
    }

    public CategoriaServico getCategoria() {
        return categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getEmpresa() {
        return empresa;
    }

    public double getMediaAvaliacoes() {
        return mediaAvaliacoes;
    }

    public int getQuantidadeAvaliacoes() {
        return quantidadeAvaliacoes;
    }

    public String getNotaEmEstrelas() {
        int estrelas = (int) Math.round(mediaAvaliacoes);
        StringBuilder builder = new StringBuilder();

        // Arredonda a media so pra virar uma leitura rapida na tela.
        for (int i = 0; i < estrelas; i++) {
            builder.append("*");
        }

        return builder.toString();
    }

    public String getResumo() {
        return String.format(
            "%s | %s | Empresa: %s | Media: %.2f | Avaliacoes: %d",
            nome,
            categoria.getDescricao(),
            empresa,
            mediaAvaliacoes,
            quantidadeAvaliacoes
        );
    }
}
