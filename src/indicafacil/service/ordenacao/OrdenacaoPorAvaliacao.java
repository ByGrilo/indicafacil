package indicafacil.service.ordenacao;

import indicafacil.model.PerfilTrabalhador;
import java.util.List;

public class OrdenacaoPorAvaliacao extends AbstractOrdenacaoProfissional {
    @Override
    public String getRotulo() {
        return "Melhor avaliacao";
    }

    @Override
    public List<PerfilTrabalhador> ordenar(List<PerfilTrabalhador> profissionais) {
        List<PerfilTrabalhador> copia = copiarLista(profissionais);
        copia.sort((primeiro, segundo) -> {
            int compareNota = Double.compare(segundo.getMediaAvaliacoes(), primeiro.getMediaAvaliacoes());
            if (compareNota != 0) {
                return compareNota;
            }

            int compareQuantidade = Integer.compare(segundo.getQuantidadeAvaliacoes(), primeiro.getQuantidadeAvaliacoes());
            if (compareQuantidade != 0) {
                return compareQuantidade;
            }

            return primeiro.getNome().compareToIgnoreCase(segundo.getNome());
        });
        return copia;
    }
}
