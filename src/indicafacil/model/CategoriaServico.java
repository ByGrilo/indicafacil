package indicafacil.model;

import java.util.ArrayList;
import java.util.List;

/*
 * Esse enum centraliza as categorias que o app aceita.
 * Fica melhor do que soltar texto aleatorio pelo sistema inteiro.
 */
public enum CategoriaServico {
    ELETRICISTA(1, "Eletricista", "Eletricistas", true),
    ENCANADOR(2, "Encanador", "Encanadores", true),
    PEDREIRO(3, "Pedreiro", "Pedreiros", true),
    FAXINEIRO(4, "Faxineiro", "Faxineiros", true),
    PINTOR(5, "Pintor", "Pintores", true),
    TECNICO_INFORMATICA(6, "Tecnico de Informatica"),
    JARDINEIRO(7, "Jardineiro", "Jardineiros", true),
    MECANICO(8, "Mecanico", "Mecanicos", true),
    BABA(9, "Baba", "Babas", true);

    private final int codigo;
    private final String descricao;
    private final String descricaoPlural;
    private final boolean exibirNaHome;

    CategoriaServico(int codigo, String descricao) {
        this(codigo, descricao, descricao + "s", false);
    }

    CategoriaServico(int codigo, String descricao, String descricaoPlural, boolean exibirNaHome) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.descricaoPlural = descricaoPlural;
        this.exibirNaHome = exibirNaHome;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDescricaoPlural() {
        return descricaoPlural;
    }

    public boolean isExibirNaHome() {
        return exibirNaHome;
    }

    public String getAtalhoVisual() {
        String[] partes = descricao.split(" ");
        if (partes.length == 1) {
            return descricao.substring(0, Math.min(2, descricao.length())).toUpperCase();
        }

        return ("" + partes[0].charAt(0) + partes[1].charAt(0)).toUpperCase();
    }

    public String getIconeVisual() {
        if (this == FAXINEIRO) {
            return "\uD83E\uDDF9";
        }
        if (this == ELETRICISTA) {
            return "\uD83D\uDD0C";
        }
        if (this == MECANICO) {
            return "\u2699";
        }
        if (this == ENCANADOR) {
            return "\uD83D\uDD27";
        }
        if (this == JARDINEIRO) {
            return "\uD83C\uDF33";
        }
        if (this == BABA) {
            return "\uD83C\uDF7C";
        }
        if (this == PINTOR) {
            return "\uD83C\uDFA8";
        }
        if (this == PEDREIRO) {
            return "\uD83E\uDDF1";
        }
        return getAtalhoVisual();
    }

    public static CategoriaServico fromCodigo(int codigo) {
        for (CategoriaServico categoria : values()) {
            if (categoria.codigo == codigo) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoria invalida.");
    }

    public static List<CategoriaServico> listarAtalhosDaHome() {
        List<CategoriaServico> atalhos = new ArrayList<>();
        atalhos.add(FAXINEIRO);
        atalhos.add(ELETRICISTA);
        atalhos.add(MECANICO);
        atalhos.add(ENCANADOR);
        atalhos.add(JARDINEIRO);
        atalhos.add(BABA);
        atalhos.add(PINTOR);
        atalhos.add(PEDREIRO);
        return atalhos;
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
