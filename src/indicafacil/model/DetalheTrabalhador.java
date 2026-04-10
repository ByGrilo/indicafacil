package indicafacil.model;

import java.util.Collections;
import java.util.List;

/*
 * Esse objeto junta o perfil do trabalhador com a lista de avaliacoes.
 * Ele existe mais pra facilitar a tela de detalhes.
 */
public class DetalheTrabalhador {
    private final PerfilTrabalhador perfil;
    private final List<AvaliacaoPublica> avaliacoes;

    public DetalheTrabalhador(PerfilTrabalhador perfil, List<AvaliacaoPublica> avaliacoes) {
        this.perfil = perfil;
        this.avaliacoes = Collections.unmodifiableList(avaliacoes);
    }

    public PerfilTrabalhador getPerfil() {
        return perfil;
    }

    public List<AvaliacaoPublica> getAvaliacoes() {
        return avaliacoes;
    }
}
