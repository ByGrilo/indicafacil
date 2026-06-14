package indicafacil.ui;

import indicafacil.model.PerfilTrabalhador;

/*
 * Como o projeto ainda nao usa mapa real, deixei uma distancia estimada so pra demonstracao da tela.
 */
public enum DistanceFilter {
    TODAS("Distancia", Integer.MAX_VALUE),
    ATE_3("Ate 3 km", 3),
    ATE_5("Ate 5 km", 5),
    ATE_10("Ate 10 km", 10);

    private final String label;
    private final int maxKm;

    DistanceFilter(String label, int maxKm) {
        this.label = label;
        this.maxKm = maxKm;
    }

    public boolean accepts(PerfilTrabalhador perfil) {
        return estimateKm(perfil) <= maxKm;
    }

    public int estimateKm(PerfilTrabalhador perfil) {
        return ((int) ((perfil.getId() + perfil.getQuantidadeAvaliacoes()) % 9)) + 2;
    }

    @Override
    public String toString() {
        return label;
    }
}
