package indicafacil.model;

/*
 * Esse enum centraliza as categorias que o app aceita.
 * Fica melhor do que soltar texto aleatorio pelo sistema inteiro.
 */
public enum CategoriaServico {
    ELETRICISTA(1, "Eletricista"),
    ENCANADOR(2, "Encanador"),
    PEDREIRO(3, "Pedreiro"),
    DIARISTA(4, "Diarista"),
    PINTOR(5, "Pintor"),
    TECNICO_INFORMATICA(6, "Tecnico de Informatica");

    private final int codigo;
    private final String descricao;

    CategoriaServico(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static CategoriaServico fromCodigo(int codigo) {
        for (CategoriaServico categoria : values()) {
            if (categoria.codigo == codigo) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoria invalida.");
    }

    public static String gerarMenuCategorias() {
        StringBuilder builder = new StringBuilder();

        for (CategoriaServico categoria : values()) {
            builder.append(categoria.codigo)
                .append(" - ")
                .append(categoria.descricao)
                .append(System.lineSeparator());
        }

        return builder.toString();
    }
}
