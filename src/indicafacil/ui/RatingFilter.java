package indicafacil.ui;

import indicafacil.model.PerfilTrabalhador;

/*
 * Filtro simples da tela de profissionais.
 */
public enum RatingFilter {
    TODAS("Avaliacao", 0.0),
    ACIMA_DE_4("4.0+", 4.0),
    ACIMA_DE_45("4.5+", 4.5);

    private final String label;
    private final double minRating;

    RatingFilter(String label, double minRating) {
        this.label = label;
        this.minRating = minRating;
    }

    public boolean accepts(PerfilTrabalhador perfil) {
        return perfil.getMediaAvaliacoes() >= minRating;
    }

    @Override
    public String toString() {
        return label;
    }
}
